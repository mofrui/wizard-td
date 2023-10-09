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
    private HashMap<String, PImage> mapElement = new HashMap<String , PImage>();
    private HashMap<String, Button> buttonElement = new HashMap<String, Button>();

    // stores waves objects
    private List<Wave> wavesList = new ArrayList<>();

    // status of the menu bar buttons
    private boolean FF, P, T, U1, U2, U3, M;

    private GameInterface gameInterface;

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

        JSONObject configFile = loadJSONObject(configPath);

        // retrieve config file info

        // retrieve the waves array
        // get infomation for each wave and store in an array "waves"
        JSONArray wavesContents = configFile.getJSONArray("waves");
        for (int i = 0; i < wavesContents.size(); i++) {

            // get each wave
            JSONObject waveEach = wavesContents.getJSONObject(i);

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

            gameInterface = new GameInterface(this, layout, mapElement, wavesList, buttonElement);
        }


        // get map layout
        String layoutFile = configFile.getString("layout");
        // extract content from the layout file (mapping layout)
        String[] layoutTemp = loadStrings(layoutFile);
        for (int i = 0; i < layoutTemp.length; i += 1) {
            String row = layoutTemp[i];
            for (int j = 0; j < row.length(); j += 1) {
                layout[i][j] = row.charAt(j);
            }
        }


        // get tower info 
        int initialTowerRange = configFile.getInt("initial_tower_range");
        double initialTowerFiringSpeed = configFile.getDouble("initial_tower_firing_speed");
        int initialTowerDamage = configFile.getInt("initial_tower_damage");
        int towerCost = configFile.getInt("tower_cost");


        // get mana info
        int initialMana = configFile.getInt("initial_mana");
        int initialManaCap = configFile.getInt("initial_mana_cap");
        double initialManaGainedPerSecond = configFile.getDouble("initial_mana_gained_per_second");
        int manaPoolSpellInitialCost = configFile.getInt("mana_pool_spell_initial_cost");
        int manaPoolSpellCostIncreasePerUse = configFile.getInt("mana_pool_spell_cost_increase_per_use");
        double manaPoolSpellCapMultiplier = configFile.getDouble("mana_pool_spell_cap_multiplier");
        double manaPoolSpellManaGainedMultiplier = configFile.getDouble("mana_pool_spell_mana_gained_multiplier");


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

        // Button ff = new Button("FF", "2x speed", 645, 63);
        buttonElement.put("FF", new Button("FF", "2x speed", 645, 63));
        buttonElement.put("P", new Button("P", "PAUSE", 645, 123));
        buttonElement.put("T", new Button("T", "Build\ntower", 645, 183));
        buttonElement.put("U1", new Button("U1", "Upgrade\nrange", 645, 243));
        buttonElement.put("U2", new Button("U2", "Upgrade\nspeed", 645, 303));
        buttonElement.put("U3", new Button("U3", "Upgrade\ndamage", 645, 363));
        buttonElement.put("M", new Button("M", "Mana pool\ncost: 100", 645, 423));
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

        for (Map.Entry<String, Button> entry : buttonElement.entrySet()) {
            Button button = entry.getValue();

            if (button.hasOver()) {
                button.setClicked();
            }
        }
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

        gameInterface.drawBackground();
        gameInterface.drawMap();

        // if (currentWaveNumber < totalWaveNumber) {
        //     currentWave = wavesList.get(currentWaveNumber);
        //     if (currentWaveNumber + 1 < totalWaveNumber) {
        //         nextWave = wavesList.get(currentWaveNumber + 1);
        //     } else {
        //         nextWave = null;
        //     }
        // }
        // if (nextWave != null) {
        //     if (nextWave.hasStarted()) {
        //         currentWaveNumber += 1;
        //     }
        // }

        gameInterface.drawTimer();

        gameInterface.drawMana();
        
        gameInterface.drawMenu();

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
