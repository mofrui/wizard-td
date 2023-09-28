package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE+TOPBAR;

    public static final int FPS = 60;

    public String configPath;

    public Random random = new Random();

	
    // initialise the layout content
    private char[][] layout = new char[20][20];

    // initialise all image resources for mapping
    private HashMap<String,PImage> mapElement = new HashMap<String , PImage>();

    // stores waves objects
    private ArrayList<Wave> wavesList = new ArrayList<>();

    private Wave currentWave;
    private int currentWaveNumber = 0;
    private int totalWaveNumber;
    private boolean started = true; // the game will immediately start


    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images.
     * Initialise the elements such as the player, enemies and map elements.
     */
	@Override
    public void setup() {

        // set frame rate
        frameRate(FPS);

        JSONObject json = loadJSONObject(configPath);

        // retrieve json file info
        String layoutFile = json.getString("layout");
        // retrieve the waves array
        // get infomation for each wave and store in an array "waves"
        JSONArray wavesContents = json.getJSONArray("waves");
        for (int i = 0; i < wavesContents.size(); i++) {

            // get each wave
            JSONObject waveEach = wavesContents.getJSONObject(i);
            totalWaveNumber = wavesContents.size();

            int waveNumber = i + 1;
            double duration = waveEach.getDouble("duration");
            double preWavePause = waveEach.getDouble("pre_wave_pause");

            // // access and iterate over monsters within the wave
            // JSONArray monsters = waveEach.getJSONArray("monsters");
            // for (int j = 0; j < monsters.size(); j++) {
            //     JSONObject monster = monsters.getJSONObject(j);

            //     // Access monster properties
            //     String type = monster.getString("type");
            //     int hp = monster.getInt("hp");
            //     float speed = monster.getFloat("speed");
            //     float armour = monster.getFloat("armour");
            //     int manaGainedOnKill = monster.getInt("mana_gained_on_kill");
            //     int quantity = monster.getInt("quantity");

            //     println("  Monster " + (j + 1) + ":");
            //     println("  Type: " + type);
            //     println("  HP: " + hp);
            //     println("  Speed: " + speed);
            //     println("  Armour: " + armour);
            //     println("  Mana gained on kill: " + manaGainedOnKill);
            //     println("  Quantity: " + quantity);
            // }

            Wave waveObj = new Wave(waveNumber, duration, preWavePause);
            wavesList.add(waveObj);
        }

        // extract content from the layout file (mapping layout)
        String[] layoutTemp = loadStrings(layoutFile);
        for (int i = 0; i < layoutTemp.length; i++) {
            String row = layoutTemp[i];
            for (int j = 0; j < row.length(); j++) {
                layout[i][j] = row.charAt(j);
            }
        }

        // load images
        mapElement.put("GRASS", loadImage("src/main/resources/WizardTD/grass.png"));
        mapElement.put("PATH_HORIZONTAL", loadImage("src/main/resources/WizardTD/path0.png"));
        mapElement.put("PATH_VERTICAL", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path0.png"), 90));
        mapElement.put("PATH_TURN_RU", loadImage("src/main/resources/WizardTD/path1.png"));
        mapElement.put("PATH_TURN_RD", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"), 90));
        mapElement.put("PATH_TURN_LD", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"), 180));
        mapElement.put("PATH_TURN_LU", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"), 270));
        mapElement.put("PATH_T_DOWN", loadImage("src/main/resources/WizardTD/path2.png"));
        mapElement.put("PATH_T_LEFT", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"), 90));
        mapElement.put("PATH_T_UP", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"), 180));
        mapElement.put("PATH_T_RIGHT", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"), 270));
        mapElement.put("PATH_CROSS", loadImage("src/main/resources/WizardTD/path3.png"));
        mapElement.put("SHRUB", loadImage("src/main/resources/WizardTD/shrub.png"));
        mapElement.put("WIZARD_HOUSE", loadImage("src/main/resources/WizardTD/wizard_house.png"));
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(){
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * @Override
     * public void mouseDragged(MouseEvent e) {
     * }
     */

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {

        // the status background
        noStroke();
        fill(137,115,76);
        rect(0, 0, 760, 40);
        // the menu background
        noStroke();
        fill(137,115,76);
        rect(640, 40, 680, 680);
        
        drawMap();
        drawTimer();
        drawManaStatus();
        drawMenuBar();

    }

    /**
     * Method is called in the draw() function
     * The map of the gameboard is drawn here
     */
    private void drawMap() {

        int wizard_house_x = 0, wizard_house_y = 0;

        // y and x here correspond with the x and y coordinate
        for (int y = 0; y < layout.length; y += 1) {
            for (int x = 0; x < layout[y].length; x += 1) {
                // every tile is 32*32
                // 40 pixels for the menu bar
                int pixel_x = x * 32;
                int pixel_y = y * 32 + 40;

                if (layout[y][x] == ' ') {
                    image(mapElement.get("GRASS"), pixel_x, pixel_y);
                }
                else if (layout[y][x] == 'S') {
                    image(mapElement.get("SHRUB"), pixel_x, pixel_y);
                }
                else if (layout[y][x] == 'W') {
                    image(mapElement.get("GRASS"), pixel_x, pixel_y);
                    // store the position of wizard house
                    wizard_house_x = pixel_x;
                    wizard_house_y = pixel_y;
                }
                else { // map the path
                    char up = ' ', down = ' ', left = ' ', right = ' ';

                    // check map edge
                    if (y-1 >= 0) { // check up edge
                        up = layout[y-1][x];
                    }
                    if (y+1 < 20) { // check down edge
                        down = layout[y+1][x];
                    }
                    if (x-1 >= 0) { // check left edge
                        left = layout[y][x-1];
                    }
                    if (x+1 < 20) { // check right edge
                       right = layout[y][x+1]; 
                    }

                    if (up == 'X' && down == 'X' && left == 'X' && right == 'X') {
                        image(mapElement.get("PATH_CROSS"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && down == 'X' && left == 'X') {
                        image(mapElement.get("PATH_T_LEFT"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && down == 'X' && right == 'X') {
                        image(mapElement.get("PATH_T_RIGHT"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && left == 'X' && right == 'X') {
                        image(mapElement.get("PATH_T_UP"), pixel_x, pixel_y);
                    }
                    else if (down == 'X' && left == 'X' && right == 'X') {
                        image(mapElement.get("PATH_T_DOWN"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && left == 'X') {
                        image(mapElement.get("PATH_TURN_RD"), pixel_x, pixel_y);
                    }
                    else if (down == 'X' && left == 'X') {
                        image(mapElement.get("PATH_TURN_RU"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && right == 'X') {
                        image(mapElement.get("PATH_TURN_LD"), pixel_x, pixel_y);
                    }
                    else if (down == 'X' && right == 'X') {
                        image(mapElement.get("PATH_TURN_LU"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' || down == 'X') {
                        image(mapElement.get("PATH_VERTICAL"), pixel_x, pixel_y);
                    }
                    else if (left == 'X' || right == 'X') {
                        image(mapElement.get("PATH_HORIZONTAL"), pixel_x, pixel_y);
                    }
                }
            }
        }
        // load the wizard house at the end
        image(mapElement.get("WIZARD_HOUSE"), wizard_house_x, wizard_house_y - 8);
    }

    /**
     * Method is called in the draw() function
     * The wave status and timer of the gameboard is drawn here
     */
    private void drawTimer() {

        if(currentWaveNumber < totalWaveNumber) {
            currentWave = wavesList.get(currentWaveNumber);

            if (started) {
                if (currentWave.hasFinishedSpawning()) {
                    started = false;
                    currentWaveNumber += 1;
                } else {
                    currentWave.setRemainingTime(currentWave.getRemainingTime() - (1.0 / frameRate));
                }
            } else {
                if (currentWave.hasFinishedPausing()) {
                    started = true;
                } else {
                    currentWave.setRemainingPause(currentWave.getRemainingPause() - (1.0 / frameRate));
                }
            }

        }

        textSize(20);
        fill(0, 0, 0);
        text("Wave " + currentWave.getWaveNumber() + " starts: " + (int)currentWave.getRemainingPause() , 15, 30);
    }

    private void drawManaStatus() {

    }


    /**
     * Method is called in the draw() function
     * The selection menu of the gameboard is drawn here
     */
    private void drawMenuBar() {

    }

    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }

    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     * @param pimg The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, RGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}
