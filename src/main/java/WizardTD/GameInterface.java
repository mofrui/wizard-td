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
 * GameInterface class handles the interactions between different components of the game.
 * 
 * It is a collection of draw methods, as well as handling object interactions.
 * 
 * @author Junrui Kang
 * @version 1.0.0
 */
public class GameInterface {

    /**
     * Frame rate of the game.
     */
	private final int frameRate = 60;

    /**
     * PApplet object of the main app.java class.
     */
	private PApplet app;

    /**
     * A boolean indicating the gamemode is endless or not.
     */
    private boolean endless;

    /**
     * A collection of buttons in the menu pages.
     */
    private HashMap<String,Button> menuButtons;

    /**
     * A 2d array of map layout extracted from the configuration file.
     */
	private char[][] mapLayout;

    /**
     * A collection of map image elements loaded during the setip.
     */
	private HashMap<String,PImage> mapElement;

    /**
     * The coordinate of wizard house that will be drawn separately.
     */
    private int wizard_house_x = 0, wizard_house_y = 0;

    /**
     * A list of waves that will be executed one by one.
     */
	private List<Wave> wavesList;

    /**
     * The current wave that the game is using.
     */
	private Wave currentWave;

    /**
     * The next wave that the game will be using.
     */
    private Wave nextWave;

    /**
     * The current wave number displayed in the information bar.
     */
    private int currentWaveNumber;

    /**
     * The total number of waves.
     */
    private int totalWaveNumber;

    /**
     * Mana object.
     */
    private Mana mana; 

    /**
     * A collection of buttons in the gameboard interface.
     */
	private HashMap<String, Button> buttonElement;

    /**
     * A list of monsters that are currently alive in the gameboard.
     */
    private List<Monster> currentMonsterList;

    /**
     * A list of towers that are currently built in the gameboard.
     */
    private List<Tower> currentTowerList;

    /**
     * A collection of tower images in the gameboard interface.
     */
    private HashMap<Integer, PImage> towerElement;

    /**
     * Initial tower range extracted from the configuration file.
     */
    private int initialTowerRange;

    /**
     * Initial tower firing speed extracted from the configuration file.
     */
    private double initialTowerFiringSpeed;

    /**
     * Initial tower damage extracted from the configuration file.
     */
    private int initialTowerDamage;

    /**
     * Initial tower cost extracted from the configuration file.
     */
    private int towerCost;

    /**
     * Image of the bullet fired from the tower.
     */
    private PImage bulletImage;

    /**
     * A boolean that indicates if the game is lost.
     */
    private boolean gameLost = false;

    /**
     * A boolean that indicates if the game is ready for the next round in the endless mode.
     */
    private boolean readyForNextRound = false;


	public GameInterface(
        PApplet app,
        boolean endless,
        HashMap<String,Button> menuButtons,
        char[][] mapLayout,
        HashMap<String,PImage> mapElement,
        List<Wave> wavesList,
        int lastWaveNumber,
        int totalWaveNumberStreak,
        Mana mana,
        HashMap<String, Button> buttonElement,
        HashMap<Integer, PImage> towerElement,
        int initialTowerRange,
        double initialTowerFiringSpeed,
        int initialTowerDamage,
        int towerCost,
        PImage bulletImage
    ) {

		this.app = app;
        this.endless = endless;

        this.menuButtons = menuButtons;

		this.mapLayout = mapLayout;
		this.mapElement = mapElement;

		this.wavesList = wavesList;

        this.currentWaveNumber = 0;
        this.totalWaveNumber = wavesList.size();

        this.mana = mana;

		this.buttonElement = buttonElement;

        this.currentMonsterList = new ArrayList<Monster>();

        this.currentTowerList = new ArrayList<Tower>();
        this.towerElement = towerElement;
        this.initialTowerRange = initialTowerRange;
        this.initialTowerFiringSpeed = initialTowerFiringSpeed;
        this.initialTowerDamage = initialTowerDamage;
        this.towerCost = towerCost;

        this.bulletImage = bulletImage;
	}


    /**
     * Check whether rmouse position is in a specific area or not
     * 
     * @param x The x-position of the target area.
     * @param y The y-position of the target area.
     * @param width The width of the target area.
     * @param height The height of the target area.
     * 
     * @return A boolean that whether the mouse is in the area.
     */
    public boolean checkMousePosition(int x, int y, int width, int height) {
        return (
            app.mouseX >= x &&
            app.mouseX <= x + width &&
            app.mouseY >= y &&
            app.mouseY <= y + height
        );
    }


    /**
     * Draw and handle the starting menu buttons of the game.
     */
    public void drawStartMenu() {

        app.noStroke();
        app.fill(137, 115, 76);
        app.rect(0, 0, 760, 760);

        app.fill(10, 0, 0);
        app.textSize(50);
        app.text("Wizard Tower Defence", 100, 280);

        for (Map.Entry<String, Button> entry : menuButtons.entrySet()) {
            Button button = entry.getValue();
        
            // set whether the mouse is over the button
            button.updateMouseOver(checkMousePosition(button.getX(), button.getY(), 160, 50));
    
            if (button.isOn()) {
                app.fill(255, 227, 132);
            } else if (button.hasMouseOver()) {
                app.fill(128,128,128);
            } else {
                app.noFill();
            }
            app.stroke(10, 0, 0);
            app.strokeWeight(3);
            app.rect(button.getX(), button.getY(), 160, 50);

            app.fill(10, 0, 0);
            app.textSize(20);
            app.text(button.getName(), button.getX() + 30, button.getY() + 30);
        }
    }


    /**
     * Draw the background of the status bar and menu bar in the game.
     */
	public void drawBackground() {
		// the status background
        app.noStroke();
        app.fill(137, 115, 76);
        app.rect(0, 0, 760, 40);
        // the menu background
        app.noStroke();
        app.fill(137, 115, 76);
        app.rect(640, 40, 680, 680);
	}

    /**
     * Draw the gameboard map.
     */
	public void drawMap() {

        // y and x here correspond with the x and y coordinate
        for (int y = 0; y < mapLayout.length; y += 1) {
            for (int x = 0; x < mapLayout[y].length; x += 1) {
                // every tile is 32*32
                // 40 pixels for the menu bar
                int pixel_x = x * 32;
                int pixel_y = y * 32 + 40;

                if (mapLayout[y][x] == ' ') {
                    app.image(mapElement.get("GRASS"), pixel_x, pixel_y);
                }
                else if (mapLayout[y][x] == 'S') {
                    app.image(mapElement.get("SHRUB"), pixel_x, pixel_y);
                }
                else if (mapLayout[y][x] == 'W') {
                    app.image(mapElement.get("GRASS"), pixel_x, pixel_y);
                    // store the position of wizard house
                    wizard_house_x = pixel_x;
                    wizard_house_y = pixel_y;
                }
                else { // map the path
                    char up = ' ', down = ' ', left = ' ', right = ' ';

                    // check map edge
                    if (y-1 >= 0) { // check up edge
                        up = mapLayout[y-1][x];
                    }
                    if (y+1 < 20) { // check down edge
                        down = mapLayout[y+1][x];
                    }
                    if (x-1 >= 0) { // check left edge
                        left = mapLayout[y][x-1];
                    }
                    if (x+1 < 20) { // check right edge
                       right = mapLayout[y][x+1]; 
                    }

                    if (up == 'X' && down == 'X' && left == 'X' && right == 'X') {
                        app.image(mapElement.get("PATH_CROSS"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && down == 'X' && left == 'X') {
                        app.image(mapElement.get("PATH_T_LEFT"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && down == 'X' && right == 'X') {
                        app.image(mapElement.get("PATH_T_RIGHT"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && left == 'X' && right == 'X') {
                        app.image(mapElement.get("PATH_T_UP"), pixel_x, pixel_y);
                    }
                    else if (down == 'X' && left == 'X' && right == 'X') {
                        app.image(mapElement.get("PATH_T_DOWN"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && left == 'X') {
                        app.image(mapElement.get("PATH_TURN_RD"), pixel_x, pixel_y);
                    }
                    else if (down == 'X' && left == 'X') {
                        app.image(mapElement.get("PATH_TURN_RU"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' && right == 'X') {
                        app.image(mapElement.get("PATH_TURN_LD"), pixel_x, pixel_y);
                    }
                    else if (down == 'X' && right == 'X') {
                        app.image(mapElement.get("PATH_TURN_LU"), pixel_x, pixel_y);
                    }
                    else if (up == 'X' || down == 'X') {
                        app.image(mapElement.get("PATH_VERTICAL"), pixel_x, pixel_y);
                    }
                    else if (left == 'X' || right == 'X') {
                        app.image(mapElement.get("PATH_HORIZONTAL"), pixel_x, pixel_y);
                    }
                }
            }
        }
	}
	

    /**
     * Draw and handle the countdown timer of the wave in the status bar.
     */
	public void drawTimer() {

		// update the wave information
		if (currentWaveNumber < totalWaveNumber) {
            currentWave = wavesList.get(currentWaveNumber);
            if (currentWaveNumber + 1 < totalWaveNumber) {
                nextWave = wavesList.get(currentWaveNumber + 1);
            } else {
                nextWave = null;
            }
        }
        if (nextWave != null) {
            if (nextWave.hasStarted()) {
                currentWaveNumber += 1;
            }
        }


        if (!buttonElement.get("P").isOn()) { // if the game is not paused
	        if (currentWave.hasStarted()) {
				if (currentWave.hasFinishedSpawning()) {
	                currentWave.setStarted(false);
	            } else {
	            	if (buttonElement.get("FF").isOn()) {
	            		currentWave.updateRemainingTime(
                            currentWave.getRemainingTime() - (2.0 / frameRate)
                        );
	            	} else {
	            		currentWave.updateRemainingTime(
                            currentWave.getRemainingTime() - (1.0 / frameRate)
                        );
	            	}
	            }
			} else if (nextWave != null) {
				if (nextWave.hasFinishedPausing()) {
					nextWave.setStarted(true);
				} else {
					if (buttonElement.get("FF").isOn()) {
	            		nextWave.updateRemainingPause(
                            nextWave.getRemainingPause() - (2.0 / frameRate)
                        );
	            	} else {
	            		nextWave.updateRemainingPause(
                            nextWave.getRemainingPause() - (1.0 / frameRate)
                        );
	            	}
				}
			}

        }


		if (nextWave != null) {
        	app.textSize(20);
        	app.fill(0, 0, 0);
            if (endless) {
                app.text("Next wave starts: " + (int)(currentWave.getRemainingTime() + nextWave.getRemainingPause()) , 15, 30);
            } else {
                app.text("Wave " + nextWave.getWaveNumber() + " starts: " + (int)(currentWave.getRemainingTime() + nextWave.getRemainingPause()) , 15, 30);
            }
		} else {
            if (endless) {
                readyForNextRound = true;
            }
        } 
    }


    /**
     * Draw and handle the mana information in the status bar.
     */
    public void drawMana() {

        if (mana.getCurrentMana() < 0) {
            gameLost = true;
            buttonElement.get("P").setButtonStatus(true);
        }

        app.textSize(20);
        app.fill(0, 0, 0);
        app.text("MANA:", 345, 30);
        app.fill(255, 255, 255);
        app.rect(420, 10, 300, 20);

        app.fill(100, 149, 237);
        if (!buttonElement.get("P").isOn()) { // if the game is not paused
            if (buttonElement.get("FF").isOn()) {
                mana.increaseCurrentManaByTime((2.0 / frameRate));
            } else {
                mana.increaseCurrentManaByTime((1.0 / frameRate));
            }
        }
        app.rect(420, 10, mana.getManaPercentage(), 20);

        app.textSize(20);
        app.fill(0, 0, 0);
        app.text((int)mana.getCurrentMana() + "/" + mana.getManaCap(), 520, 27);

        if (buttonElement.get("M").isOn()) {
            mana.upgradeMana();
            buttonElement.get("M").updateStatus(); // switch the button off immediately
        }

        // display upgrade cost
        app.textSize(12);
        app.fill(10, 0, 0);
        app.text(mana.getManaPoolSpellCost(), 728, 463);
    }


    /**
     * Draw and handle the buttons in the gameboard.
     */
    public void drawMenu() {

    	for (Map.Entry<String, Button> entry : buttonElement.entrySet()) {
    		Button button = entry.getValue();
    		
    		// set whether the mouse is over the button
    		button.updateMouseOver(checkMousePosition(button.getX(), button.getY(), 50, 50));
    	
	    	if (button.isOn()) {
				app.fill(255, 227, 132);
			} else if (button.hasMouseOver()) {
				app.fill(128,128,128);
			} else {
				app.noFill();
			}
    		app.stroke(10, 0, 0);
			app.strokeWeight(3);
			app.rect(button.getX(), button.getY(), 50, 50);

			app.fill(10, 0, 0);
	        app.textSize(30);
	        app.text(button.getName(), 650, button.getY() + 37);

	        app.fill(10, 0, 0);
	        app.textSize(12);
	        app.text(button.getDesciption(), 698, button.getY() + 20);
    	}
    }


    /**
     * Draw and handle the monsters in the gameboard.
     */
    public void drawMonster() {

        // spawn new monsters
        if (!buttonElement.get("P").isOn()) {
            // update the spawn interval
            if (buttonElement.get("FF").isOn()) {
                currentWave.updateSpawnInterval(2.0 / frameRate);
            } else {
                currentWave.updateSpawnInterval(1.0 / frameRate);
            }
            // spawn a random monster
            if (currentWave.isReadyToSpawn()) {
                if (currentWave.hasRemainingMonster()) {
                    Monster m = currentWave.getRandomMonster();
                    Monster monster = new Monster(
                        m.getType(),
                        m.getImage(),
                        m.getDeathAnimation(),
                        m.getFullHp(),
                        m.getSpeed(),
                        m.getArmour(),
                        m.getManaGainedOnKill(),
                        mapLayout
                    );
                    currentMonsterList.add(monster);
                }
            }
        }

        List<Monster> toRemove = new ArrayList<>();
        
        if (currentMonsterList.size() > 0) {
            for (Monster monster : currentMonsterList) {
                // draw monster image
                app.image(monster.getImage(), monster.getPosition()[0], monster.getPosition()[1]);
                
                // draw hp bar
                app.fill(255, 0, 0);
                app.noStroke();
                app.rect(monster.getPosition()[0] - 5, monster.getPosition()[1] - 7, 29, 3);
                app.fill(0, 255, 0);
                app.noStroke();
                app.rect(monster.getPosition()[0] - 5, monster.getPosition()[1] - 7, monster.getHpPercentage(), 3);

                if (monster.isDying()) {
                    // if the monster is dying, stop moving and draw death animation
                    if (!buttonElement.get("P").isOn()) {
                        monster.updateDyingImage();
                    }
                } else {
                    // move the monster
                    if (!buttonElement.get("P").isOn()) {
                        if (buttonElement.get("FF").isOn()) {
                            monster.move((float)(monster.getSpeed()/32));
                        }
                        monster.move((float)(monster.getSpeed()/32));
                    }
                }
                
                // check whether the monster reaches the wizard house
                // deduct mana and banish monster
                if (monster.hasReachedWizardHouse()) {
                    mana.decreaseCurrentMana(monster.getCurrentHp());
                    monster.banishMonster();
                }

                // if the monster is dead, add that monster to the removal list
                if (monster.isDead()) {
                    toRemove.add(monster);
                    mana.increaseCurrentMana(monster.getManaGainedOnKill());
                }
            }
        }

        // remove dead monster
        for (Monster monsterToRemove : toRemove) {
            Iterator<Monster> iterator = currentMonsterList.iterator();
            while (iterator.hasNext()) {
                Monster m = iterator.next();
                if (m == monsterToRemove) {
                    iterator.remove();
                    break;
                }
            }
        }
    }


    /**
     * Draw and handle the towers and bullets in the gameboard.
     */
    public void drawTowerBullet() {

        if (currentTowerList.size() > 0) {
            for (Tower tower : currentTowerList) {
                
                // draw tower image
                tower.updateImage();
                app.image(tower.getImage(), tower.getPosition()[0], tower.getPosition()[1]);
                
                // draw upgrade images and features
                if (tower.getUpgradeLevel('s') != 0) {
                    app.strokeWeight(tower.getUpgradeLevel('s') + 0.5f);
                    app.stroke(173, 216, 230);
                    app.noFill();
                    app.rect(tower.getPosition()[0] + 5, tower.getPosition()[1] + 5, 20, 20);
                    app.stroke(0, 0, 0);
                    app.strokeWeight(1);
                }
                for (int i = 0; i < tower.getUpgradeLevel('r'); i += 1) {
                    app.noFill();
                    app.stroke(218, 112, 214);
                    app.strokeWeight(2);
                    app.ellipse(tower.getPosition()[0] + 6 + i * 10,tower.getPosition()[1] + 5, 5, 5);
                    app.stroke(0, 0, 0);
                    app.strokeWeight(1);
                }
                for (int i = 0; i < tower.getUpgradeLevel('d'); i += 1) {
                    app.fill(218, 112, 214);
                    app.textSize(10);
                    app.text("X", tower.getPosition()[0] + 3 + i * 10, tower.getPosition()[1] + 30);
                }

                // draw tower range
                if (tower.isOver(app.mouseX, app.mouseY)) {
                    app.noFill();
                    app.stroke(255, 255, 0);
                    app.strokeWeight(2);
                    app.ellipse(tower.getPosition()[0]+32/2, tower.getPosition()[1]+32/2, tower.getRange()*2, tower.getRange()*2);
                    app.stroke(0, 0, 0);
                    app.strokeWeight(1);
                }

                // attack monster
                if (currentMonsterList.size() > 0) {

                    // select a random monster to attack
                    Random random = new Random();
                    int randomIndex = random.nextInt(currentMonsterList.size());
                    Monster randomMonster = currentMonsterList.get(randomIndex);

                    // get the distance between the monster and the tower
                    float distance = app.dist(tower.getPosition()[0], tower.getPosition()[1], randomMonster.getPosition()[0], randomMonster.getPosition()[1]);
                    
                    // update fire cooldown
                    if (!buttonElement.get("P").isOn()) {
                        if (buttonElement.get("FF").isOn()) {
                            tower.updateFireCoolDown(2.0 / frameRate);
                        } else {
                            tower.updateFireCoolDown(1.0 / frameRate);
                        }
                    }

                    // if the monster is in the range and cd is ready, attack the monster
                    if (distance < tower.getRange() && tower.isReadyToFire()) {
                        tower.fire(randomMonster);
                    }
                }

                List<Bullet> toRemove = new ArrayList<>();

                // draw bullet
                if (tower.getBulletList().size() > 0) {
                    for (Bullet bullet : tower.getBulletList()) {

                        // move the bullet
                        if (!buttonElement.get("P").isOn()) {
                            bullet.move(buttonElement.get("FF").isOn());
                        }

                        // check whether the bullet can make damage
                        bullet.makeDamage();

                        // check if the target monster is dead or lost
                        bullet.checkTargetLost();

                        // if the bullet is still valid, draw the bullet
                        // if the bullet loses its target, remove it from the gameboard
                        if (! bullet.isHide()) {
                            app.image(bullet.getImage(), bullet.getPosition()[0], bullet.getPosition()[1]);
                        } else {
                            toRemove.add(bullet);
                        }
                    }
                }

                // remove bullet
                for (Bullet bulletToRemove : toRemove) {
                    Iterator<Bullet> iterator = tower.getBulletList().iterator();
                    while (iterator.hasNext()) {
                        Bullet b = iterator.next();
                        if (b == bulletToRemove) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
    }


    /**
     * Draw and handle the tower upgrade information in the menu bar.
     */
    public void drawTowerUpgradeInfo() {

        if (currentTowerList.size() > 0) {
            for (Tower tower : currentTowerList) {
                if (tower.isOver(app.mouseX, app.mouseY)) {   
                    // draw upgrade details
                    app.textSize(10);
                    app.fill(255, 255, 255);
                    app.rect(650, 540, 90, 20);
                    app.rect(650, 560, 90, 60);
                    app.rect(650, 620, 90, 20);
                    app.fill(0, 0, 0);

                    int rangeFee = 0, speedFee = 0, damageFee = 0;
                    if (buttonElement.get("U1").isOn()) {
                        rangeFee = tower.getUpgradeCost('r');
                    }
                    if (buttonElement.get("U2").isOn()) {
                        speedFee = tower.getUpgradeCost('s');
                    }
                    if (buttonElement.get("U3").isOn()) {
                        damageFee = tower.getUpgradeCost('d');
                    }
                    app.text("Upgrade cost", 655, 555);
                    app.text("range:\t" + rangeFee, 655, 575);
                    app.text("speed:\t" + speedFee, 655, 595);
                    app.text("damage:\t" + damageFee, 655, 615);
                    app.text("Total:\t" + (rangeFee+speedFee+damageFee), 655, 635);
                }
            }
        }
    }


    /**
     * Draw the wizard house.
     */
    public void drawWizardHouse() {
        app.image(mapElement.get("WIZARD_HOUSE"), wizard_house_x, wizard_house_y - 8);
    }


    /**
     * Draw the game lost message bar.
     */
    public void drawGameLost() {
        app.stroke(0, 0, 0);
        app.strokeWeight(3);
        app.fill(137, 115, 76);
        app.rect(150, 230, 340, 200);
        app.fill(0, 0, 0);
        app.textSize(30);
        app.text("You LOST", 245, 310);
        app.textSize(20);
        app.text("press 'r' to restart", 230, 340);
        app.textSize(20);
        app.text("press 'b' to return the menu", 180, 370);
    }


    /**
     * Draws the game win message bar.
     */
    public void drawGameWin() {
        app.stroke(0, 0, 0);
        app.strokeWeight(3);
        app.fill(137, 115, 76);
        app.rect(150, 230, 340, 200);
        app.fill(0, 0, 0);
        app.textSize(30);
        app.text("You WIN", 250, 310);
        app.textSize(20);
        app.text("press any key to return the menu", 160, 340);
    }


    /**
     * Check whether the game is won.
     * 
     * The game will win if:
     * 1. it is the last wave
     * 2. the wave is finish
     * 3. no remaining monsters in the gameboard
     * 4. no monster to be spawned in the current wave
     * 
     * @return A boolean that the game is won or not.
     */
    public boolean isGameWin() {
        return (
            currentWaveNumber + 1 == totalWaveNumber && 
            (int)currentWave.getRemainingTime() == 0 && 
            currentMonsterList.size() == 0 && 
            !currentWave.hasRemainingMonster()
        );
    }


    /**
     * Check whether the game is lost.
     * 
     * @return A boolean that the game is lost or not.
     */
    public boolean isGameLost() {
        return gameLost;
    }


    /**
     * Build new tower in the gameboard.
     */
    public void buildNewTower() {

        int cost = towerCost;

        // update cost based on the upgrade selections
        if (buttonElement.get("U1").isOn()) {
            cost += 20;
        }
        if (buttonElement.get("U2").isOn()) {
            cost += 20;
        }
        if (buttonElement.get("U3").isOn()) {
            cost += 20;
        }

        // build tower if the mana is enough and the mouse is in the gameboard
        if (mana.getCurrentMana() >= cost && (mapLayout[(int)(app.mouseY-40)/32][(int)app.mouseX/32] == ' ')) {
            
            // check whether there has already exist a tower or not
            if (currentTowerList.size() > 0) {
                for (Tower tower : currentTowerList) {
                    if (tower.isOver(app.mouseX, app.mouseY)) {
                        return;
                    }
                }
            }

            // build a new tower
            Tower tower = new Tower(app.mouseX, app.mouseY, initialTowerRange, initialTowerFiringSpeed, initialTowerDamage, towerElement, bulletImage);
            
            // upgrade tower
            if (buttonElement.get("U1").isOn()) {
                tower.upgradeTower('r');
            }
            if (buttonElement.get("U2").isOn()) {
                tower.upgradeTower('s');
            }
            if (buttonElement.get("U3").isOn()) {
                tower.upgradeTower('d');
            }

            // add the tower to the tower list
            currentTowerList.add(tower);

            // deduct the mana
            mana.decreaseCurrentMana(towerCost);
        }
        buttonElement.get("T").setButtonStatus(false);
        buttonElement.get("U1").setButtonStatus(false);
        buttonElement.get("U2").setButtonStatus(false);
        buttonElement.get("U3").setButtonStatus(false);
    }


    /**
     * Handles the tower upgrades.
     */
    public void upgradeTower() {
        
        if (currentTowerList.size() > 0) {
            for (Tower tower : currentTowerList) {

                // check whether a tower is selected
                if (tower.isOver(app.mouseX, app.mouseY)) {
                    
                    int upgradeCost = 0;

                    // update cost based on the upgrade selections
                    if (buttonElement.get("U1").isOn()) {
                        upgradeCost += tower.getUpgradeCost('r');
                    }
                    if (buttonElement.get("U2").isOn()) {
                        upgradeCost += tower.getUpgradeCost('s');
                    }
                    if (buttonElement.get("U3").isOn()) {
                        upgradeCost += tower.getUpgradeCost('d');
                    }

                    // upgrade tower if the mana is enough
                    if (mana.getCurrentMana() >= upgradeCost) {
                        if (buttonElement.get("U1").isOn()) {
                            tower.upgradeTower('r');
                        }
                        if (buttonElement.get("U2").isOn()) {
                            tower.upgradeTower('s');
                        }
                        if (buttonElement.get("U3").isOn()) {
                            tower.upgradeTower('d');
                        }

                        // deduct the mana
                        mana.decreaseCurrentMana(upgradeCost);
                    } else {
                        // the upgrade button will be switched off when mana is not enough
                        buttonElement.get("T").setButtonStatus(false);
                        buttonElement.get("U1").setButtonStatus(false);
                        buttonElement.get("U2").setButtonStatus(false);
                        buttonElement.get("U3").setButtonStatus(false);
                    }
                }
            }
        } 
    }


    /**
     * Check whether the game is ready for the next round in the endless mode.
     * 
     * @return A boolean that indicate the next round status.
     */
    public boolean isReadyForNextRound() {
        return readyForNextRound;
    }


    /**
     * Updates the next round status.
     */
    public void updateReadyForNextRound() {
        readyForNextRound = false;
    }


    /**
     * Get the current wave list for the endless mode to upgrade the wave.
     * 
     * @return Current wave list.
     */
    public List<Wave> getWavesList() {
        return wavesList;
    }


    /**
     * Updates the game in the endless mode by making the next wave harder.
     * 
     * @param updatedWavesList the next five waves
     */
    public void updateEndlessInformation(List<Wave> updatedWavesList) {

        wavesList = updatedWavesList;

        currentWaveNumber = 0;
        totalWaveNumber = wavesList.size();
    }
}


