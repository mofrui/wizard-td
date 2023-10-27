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


/**
 * App class is the main class that contains all critical methods to build the game.
 * 
 * Methods in this class setup game, draw gameboard and detect user i/o from mouse and keyboard.
 * 
 * @author Junrui Kang
 * @version 1.0.0
 */
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

    private char[][] mapLayout;

    // initialise all image resources for mapping
    private HashMap<String, PImage> mapElement;
    private HashMap<String, Button> buttonElement;
    
    private HashMap<String, PImage> monsterElement;
    private HashMap<String, List<PImage>> monsterDeathElement;
    
    private HashMap<Integer, PImage> towerElement;

    // stores waves objects
    private List<Wave> wavesList;

    private PImage bulletImage;

    private GameInterface gameInterface;

    private boolean menuPage = true;
    private HashMap<String, Button> menuButtons;

    private boolean firstRun = true;
    private boolean endless = false;
    private boolean firstEndless = true;


    /**
     * Set up a configuration file path when the app is launched.
     */
    public App() {
        this.configPath = "easy.json";
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
     * Instantiate game interface.
     */
	@Override
    public void setup() {

        // set frame rate
        frameRate(FPS);

        if (firstRun) {

            mapElement = new HashMap<String , PImage>();

            buttonElement = new HashMap<String, Button>();

            monsterElement = new HashMap<String, PImage>();
            monsterDeathElement = new HashMap<String, List<PImage>>();

            towerElement = new HashMap<Integer, PImage>();

            menuButtons = new HashMap<String, Button>();
            menuButtons.put("easy", new Button("EASY GAME", "", 'e', 90, 360));
            menuButtons.put("hard", new Button("HARD GAME", "", 'h', 300, 360));
            menuButtons.put("endless", new Button("ENDLESS", "", 'x', 510, 360));

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

            buttonElement.put("FF", new Button("FF", "2x speed", 'f', 645, 63));
            buttonElement.put("P", new Button("P", "PAUSE", 'p', 645, 123));
            buttonElement.put("T", new Button("T", "Build\ntower", 't', 645, 183));
            buttonElement.put("U1", new Button("U1", "Upgrade\nrange", '1', 645, 243));
            buttonElement.put("U2", new Button("U2", "Upgrade\nspeed", '2', 645, 303));
            buttonElement.put("U3", new Button("U3", "Upgrade\ndamage", '3', 645, 363));
            buttonElement.put("M", new Button("M", "Mana pool\ncost:", 'm', 645, 423));

            monsterElement.put("gremlin", loadImage("src/main/resources/WizardTD/gremlin.png"));
            monsterElement.put("beetle", loadImage("src/main/resources/WizardTD/beetle.png"));
            monsterElement.put("worm", loadImage("src/main/resources/WizardTD/worm.png"));

            monsterDeathElement.put("gremlin", Arrays.asList(
                loadImage("src/main/resources/WizardTD/gremlin1.png"),
                loadImage("src/main/resources/WizardTD/gremlin2.png"),
                loadImage("src/main/resources/WizardTD/gremlin3.png"),
                loadImage("src/main/resources/WizardTD/gremlin4.png"),
                loadImage("src/main/resources/WizardTD/gremlin5.png")
            ));
            monsterDeathElement.put("beetle", Arrays.asList(
                loadImage("src/main/resources/WizardTD/gremlin1.png"),
                loadImage("src/main/resources/WizardTD/gremlin2.png"),
                loadImage("src/main/resources/WizardTD/gremlin3.png"),
                loadImage("src/main/resources/WizardTD/gremlin4.png"),
                loadImage("src/main/resources/WizardTD/gremlin5.png")
            ));
            monsterDeathElement.put("worm", Arrays.asList(
                loadImage("src/main/resources/WizardTD/gremlin1.png"),
                loadImage("src/main/resources/WizardTD/gremlin2.png"),
                loadImage("src/main/resources/WizardTD/gremlin3.png"),
                loadImage("src/main/resources/WizardTD/gremlin4.png"),
                loadImage("src/main/resources/WizardTD/gremlin5.png")
            ));

            towerElement.put(0, loadImage("src/main/resources/WizardTD/tower0.png"));
            towerElement.put(1, loadImage("src/main/resources/WizardTD/tower1.png"));
            towerElement.put(2, loadImage("src/main/resources/WizardTD/tower2.png"));
            towerElement.put(3, loadImage("src/main/resources/WizardTD/tower3.png"));
            towerElement.put(4, loadImage("src/main/resources/WizardTD/tower4.png"));
            towerElement.put(5, loadImage("src/main/resources/WizardTD/tower5.png"));
            towerElement.put(6, loadImage("src/main/resources/WizardTD/tower6.png"));
            towerElement.put(7, loadImage("src/main/resources/WizardTD/tower7.png"));

            bulletImage = loadImage("src/main/resources/WizardTD/fireball.png");

            firstRun = false;
        }


        if (menuButtons.get("endless").isOn() || endless) {
            
            endless = true;

            if (firstEndless) {
                
                configPath = "endless.json";
                menuPage = false;
                firstEndless = false;
            
            } else {

                List<Wave> newWavesList = new ArrayList<Wave>();

                for (Wave wave : gameInterface.getWavesList()) {

                    int waveNumber = wave.getWaveNumber() + 5;
                    double duration = wave.getDuration() + 1;
                    double preWavePause = wave.getPreWavePause() + 1;

                    HashMap<Monster, Integer> newMonsterDict = new HashMap<Monster, Integer>();
                    for (Monster monster : wave.getMonsterDict().keySet()) {

                        Random random = new Random();

                        String type = monster.getType();
                        PImage image = monster.getImage();
                        List<PImage> deathAnimation = monster.getDeathAnimation();
                        int hp = (int)(monster.getFullHp() * 1.5);
                        double speed = monster.getSpeed();
                        double armour = monster.getArmour() / 1.1;
                        int manaGainedOnKill = monster.getManaGainedOnKill() + 5;

                        int quantity = wave.getMonsterDict().get(monster) + random.nextInt(9) + 5;

                        newMonsterDict.put(new Monster(type, image, deathAnimation, hp, speed, armour, manaGainedOnKill, mapLayout), quantity);    
                    }

                    newWavesList.add(new Wave(waveNumber, duration, preWavePause, newMonsterDict));
                }

                gameInterface.updateEndlessInformation(newWavesList);
                return;
            }

        } else {
            if (menuButtons.get("easy").isOn()) {
                configPath = "easy.json";
                menuPage = false;
            } else if (menuButtons.get("hard").isOn()) {
                configPath = "hard.json";
                menuPage = false;
            }

        }

        mapLayout = new char[20][20];
        wavesList = new ArrayList<>();


        JSONObject configFile = loadJSONObject(configPath);

        // get map layout
        String layoutFile = configFile.getString("layout");
        // extract content from the layout file (mapping layout)
        String[] layoutTemp = loadStrings(layoutFile);
        for (int i = 0; i < layoutTemp.length; i += 1) {
            String row = layoutTemp[i];
            for (int j = 0; j < row.length(); j += 1) {
                mapLayout[i][j] = row.charAt(j);
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

        Mana mana = new Mana(initialMana, initialManaCap, initialManaGainedPerSecond, manaPoolSpellInitialCost, manaPoolSpellCostIncreasePerUse, manaPoolSpellCapMultiplier, manaPoolSpellManaGainedMultiplier);

        // retrieve the waves array
        // get infomation for each wave and store in an array "waves"
        JSONArray wavesContents = configFile.getJSONArray("waves");
        for (int i = 0; i < wavesContents.size(); i++) {

            // get each wave
            JSONObject waveEach = wavesContents.getJSONObject(i);

            int waveNumber = i + 1;
            double duration = waveEach.getDouble("duration");
            double preWavePause = waveEach.getDouble("pre_wave_pause");

            // access and iterate over monsters within the wave
            JSONArray monsters = waveEach.getJSONArray("monsters");
            HashMap<Monster, Integer> monsterDict = new HashMap<>();

            for (int j = 0; j < monsters.size(); j++) {
                JSONObject monsterInfo = monsters.getJSONObject(j);

                // Access monster properties
                String type = monsterInfo.getString("type");
                int hp = monsterInfo.getInt("hp");
                float speed = monsterInfo.getFloat("speed");
                float armour = monsterInfo.getFloat("armour");
                int manaGainedOnKill = monsterInfo.getInt("mana_gained_on_kill");
                int quantity = monsterInfo.getInt("quantity");

                Monster monster = new Monster(type, monsterElement.get(type), monsterDeathElement.get(type), hp, speed, armour, manaGainedOnKill, mapLayout);
                monsterDict.put(monster, quantity);
            }

            Wave wave = new Wave(waveNumber, duration, preWavePause, monsterDict);
            wavesList.add(wave);
        }
        
        gameInterface = new GameInterface(this, endless, menuButtons, mapLayout, mapElement, wavesList, 0, 0, mana, buttonElement, towerElement, initialTowerRange, initialTowerFiringSpeed, initialTowerDamage, towerCost, bulletImage);
        menuButtons.get("easy").setButtonStatus(false);
        menuButtons.get("hard").setButtonStatus(false);
        menuButtons.get("endless").setButtonStatus(false);
        buttonElement.get("FF").setButtonStatus(false);
        buttonElement.get("P").setButtonStatus(false);
        buttonElement.get("T").setButtonStatus(false);
        buttonElement.get("U1").setButtonStatus(false);
        buttonElement.get("U2").setButtonStatus(false);
        buttonElement.get("U3").setButtonStatus(false);
        buttonElement.get("M").setButtonStatus(false);

    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(){
        if (gameInterface.isGameLost()){
            if (key == 'r' || key == 'R') {
                if (endless) {
                    firstEndless = true;
                }
                setup();
            } else if (key == 'b' || key == 'B') {
                endless = false;
                firstEndless = true;
                menuPage = true;
                setup();
            }
        }

        if (gameInterface.isGameWin()) {
            firstEndless = true;
            menuPage = true;
        } 

        for (Map.Entry<String, Button> entry : buttonElement.entrySet()) {
            Button button = entry.getValue();

            if (key == button.getHotKey()) {
                button.updateStatus();
            }
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
    }

    /**
     * Receive mouse pressed signal from the mouse.
     */
    @Override
    public void mousePressed(MouseEvent e) {

        if (menuPage) {
            for (Map.Entry<String, Button> entry : menuButtons.entrySet()) {
                Button button = entry.getValue();
                if (button.hasMouseOver()) {
                    button.updateStatus();
                    setup();
                }
            }
        }


        for (Map.Entry<String, Button> entry : buttonElement.entrySet()) {
            Button button = entry.getValue();

            if (button.hasMouseOver()) {
                button.updateStatus();
            }
        }

        if (buttonElement.get("T").isOn() && gameInterface.checkMousePosition(0, 40, 640, 640)) {
            gameInterface.buildNewTower();
        }

        if ((buttonElement.get("U1").isOn() || buttonElement.get("U2").isOn() || buttonElement.get("U3").isOn()) && gameInterface.checkMousePosition(0, 40, 640, 640)) {
            gameInterface.upgradeTower();
        }
    }


    /**
     * Receive mouse released signal from the mouse.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }


    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {

        if (menuPage) {

            gameInterface.drawStartMenu();

        } else {

            gameInterface.drawMap();
            gameInterface.drawTowerBullet();
            gameInterface.drawBackground();

            gameInterface.drawTimer();
            gameInterface.drawMana();
            gameInterface.drawMenu();
            gameInterface.drawTowerUpgradeInfo();

            gameInterface.drawMonster();
            gameInterface.drawWizardHouse();

            if (gameInterface.isGameWin() && !endless) {
                gameInterface.drawGameWin();
            }

            if (gameInterface.isGameLost()) {
                gameInterface.drawGameLost();
            }

            if (endless && gameInterface.isReadyForNextRound()) {
                gameInterface.updateReadyForNextRound();
                setup();
            }

            
        }

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
