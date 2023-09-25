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

    // initialise
    public char[][] layout = new char[20][20];

    public Random random = new Random();
	
    public App() {
        this.configPath = "config.json";
    }

    // map layout content
    List<String> layoutContent = new ArrayList<>();

    private HashMap<String,PImage> MAP_ELEMENT = new HashMap<String , PImage>();

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

        // get json file info
        JSONObject json = loadJSONObject(configPath);
        String layoutFile = json.getString("layout");

        // extract content from the layout file
        // store as a 2d array 
        String[] layoutTemp = loadStrings(layoutFile);
        for (int i = 0; i < layoutTemp.length; i++) {
            String row = layoutTemp[i];
            for (int j = 0; j < row.length(); j++) {
                layout[i][j] = row.charAt(j);
            }
        }

        // load images
        MAP_ELEMENT.put("GRASS", loadImage("src/main/resources/WizardTD/grass.png"));
        MAP_ELEMENT.put("PATH_HORIZONTAL", loadImage("src/main/resources/WizardTD/path0.png"));
        MAP_ELEMENT.put("PATH_VERTICAL", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path0.png"), 90));
        MAP_ELEMENT.put("PATH_TURN_RU", loadImage("src/main/resources/WizardTD/path1.png"));
        MAP_ELEMENT.put("PATH_TURN_RD", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"), 90));
        MAP_ELEMENT.put("PATH_TURN_LD", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"), 180));
        MAP_ELEMENT.put("PATH_TURN_LU", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path1.png"), 270));
        MAP_ELEMENT.put("PATH_T_DOWN", loadImage("src/main/resources/WizardTD/path2.png"));
        MAP_ELEMENT.put("PATH_T_LEFT", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"), 90));
        MAP_ELEMENT.put("PATH_T_UP", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"), 180));
        MAP_ELEMENT.put("PATH_T_RIGHT", rotateImageByDegrees(loadImage("src/main/resources/WizardTD/path2.png"), 270));
        MAP_ELEMENT.put("PATH_CROSS", loadImage("src/main/resources/WizardTD/path3.png"));
        MAP_ELEMENT.put("SHRUB", loadImage("src/main/resources/WizardTD/shrub.png"));
        MAP_ELEMENT.put("WIZARD_HOUSE", loadImage("src/main/resources/WizardTD/wizard_house.png"));
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
        drapMap();    
    }

    private void drapMap() {
        int wizard_house_x = 0, wizard_house_y = 0;

        for (int y = 0; y < layout.length; y += 1) {
            for (int x = 0; x < layout[y].length; x += 1) {
                // every tile is 32*32
                // 40 pixels for the menu bar
                int pixel_x = x * 32;
                int pixel_y = y * 32 + 40;

                if (layout[y][x] == ' ') {
                    image(MAP_ELEMENT.get("GRASS"), pixel_x, pixel_y);
                }
                else if (layout[y][x] == 'S') {
                    image(MAP_ELEMENT.get("SHRUB"), pixel_x, pixel_y);
                }
                else if (layout[y][x] == 'W') {
                    image(MAP_ELEMENT.get("GRASS"), pixel_x, pixel_y);
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
                        image(MAP_ELEMENT.get("PATH_CROSS"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && down == 'X' && left == 'X') {
                        image(MAP_ELEMENT.get("PATH_T_LEFT"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && down == 'X' && right == 'X') {
                        image(MAP_ELEMENT.get("PATH_T_RIGHT"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && left == 'X' && right == 'X') {
                        image(MAP_ELEMENT.get("PATH_T_UP"), pixel_x, pixel_y);
                    }
                    else if (down == 'X' && left == 'X' && right == 'X') {
                        image(MAP_ELEMENT.get("PATH_T_DOWN"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && left == 'X') {
                        image(MAP_ELEMENT.get("PATH_TURN_RD"), pixel_x, pixel_y);
                    }
                    else if (down == 'X' && left == 'X') {
                        image(MAP_ELEMENT.get("PATH_TURN_RU"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && right == 'X') {
                        image(MAP_ELEMENT.get("PATH_TURN_LD"), pixel_x, pixel_y);
                    }
                    else if (down == 'X' && right == 'X') {
                        image(MAP_ELEMENT.get("PATH_TURN_LU"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' || down == 'X') {
                        image(MAP_ELEMENT.get("PATH_VERTICAL"), pixel_x, pixel_y);
                    }
                    else if (left == 'X' || right == 'X') {
                        image(MAP_ELEMENT.get("PATH_HORIZONTAL"), pixel_x, pixel_y);
                    }
                }
            }
        }
        // load the wizard house at the end
        image(MAP_ELEMENT.get("WIZARD_HOUSE"), wizard_house_x, wizard_house_y - 8);
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
