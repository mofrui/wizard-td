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


public class GameInterface {

	private final int frameRate = 60;

	private PApplet app;

	private char[][] mapLayout;
	private HashMap<String,PImage> mapElement;

	private List<Wave> wavesList;
	private Wave currentWave;
    private Wave nextWave;
    private int currentWaveNumber;
    private int totalWaveNumber;

    private Mana mana; 

	private HashMap<String, Button> buttonElement;

    private List<Monster> currentMonsterList;

    private List<Tower> currentTowerList;
    private HashMap<Integer, PImage> towerElement;
    private int initialTowerRange;
    private double initialTowerFiringSpeed;
    private int initialTowerDamage;
    private int towerCost;

    private PImage bulletImage;

    private boolean gameLost;


	public GameInterface(
        PApplet app,
        char[][] mapLayout,
        HashMap<String,PImage> mapElement,
        List<Wave> wavesList,
        Mana mana,
        HashMap<String, Button> buttonElement,
        HashMap<Integer, PImage> towerElement,
        int initialTowerRange,
        double initialTowerFiringSpeed,
        int initialTowerDamage,
        int towerCost,
        PImage bulletImage) {

		this.app = app;

		this.mapLayout = mapLayout;
		this.mapElement = mapElement;

		this.wavesList = wavesList;
		currentWaveNumber = 0;
		totalWaveNumber = wavesList.size();

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

        this.gameLost = false;

	}


    // this method help to check whether the mouse is on a specific area or not
    public boolean checkMousePosition(int x, int y, int width, int height) {
        return app.mouseX >= x && app.mouseX <= x + width && app.mouseY >= y && app.mouseY <= y + height;
    }


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


	public void drawMap() {

        int wizard_house_x = 0, wizard_house_y = 0;
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
        // load the wizard house at the end
        app.image(mapElement.get("WIZARD_HOUSE"), wizard_house_x, wizard_house_y - 8);
	}
	

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
	            		currentWave.updateRemainingTime(currentWave.getRemainingTime() - (2.0 / frameRate));
	            	} else {
	            		currentWave.updateRemainingTime(currentWave.getRemainingTime() - (1.0 / frameRate));
	            	}
	            }
			} else if (nextWave != null) {
				if (nextWave.hasFinishedPausing()) {
					nextWave.setStarted(true);
				} else {
					if (buttonElement.get("FF").isOn()) {
	            		nextWave.updateRemainingPause(nextWave.getRemainingPause() - (2.0 / frameRate));
	            	} else {
	            		nextWave.updateRemainingPause(nextWave.getRemainingPause() - (1.0 / frameRate));
	            	}
				}
			}

        }


		if (nextWave != null) {
        	app.textSize(20);
        	app.fill(0, 0, 0);
        	app.text("Wave " + nextWave.getWaveNumber() + " starts: " + (int)(currentWave.getRemainingTime() + nextWave.getRemainingPause()) , 15, 30);
		}    
    }


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


    public void drawMonster() {

        if (!buttonElement.get("P").isOn()) {
            // if the time passed is a multiple of the spawn time interval, then spawn a new monster
            if ( ((currentWave.getDuration() - currentWave.getRemainingTime()) % currentWave.getSpawnInterval(buttonElement.get("FF").isOn())) < 0.04) {
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
                
                if (!buttonElement.get("P").isOn()) {
                    if (buttonElement.get("FF").isOn()) {
                        monster.move((float)(monster.getSpeed()/20));
                    }
                    monster.move((float)(monster.getSpeed()/20));
                }
                if (monster.hasReachedWizardHouse()) {
                    mana.decreaseCurrentMana(monster.getCurrentHp());
                    toRemove.add(monster);
                }
                if (monster.isDead()) {
                    toRemove.add(monster);
                    mana.increaseCurrentMana(monster.getManaGainedOnKill());
                }
            }
        }

        // remove monster
        for (Monster monsterToRemove : toRemove) {
            Iterator<Monster> iterator = currentMonsterList.iterator();
            while (iterator.hasNext()) {
                Monster m = iterator.next();
                if (m == monsterToRemove) {
                    iterator.remove(); // Safely remove the monster
                    break;
                }
            }
        }

    }


    public void drawTower() {

        if (currentTowerList.size() > 0) {
            for (Tower tower : currentTowerList) {
                app.image(tower.getImage(), tower.getPosition()[0], tower.getPosition()[1]);
                
                // draw tower details
                if (tower.isOver(app.mouseX, app.mouseY)) {
                    // draw range
                    app.noFill();
                    app.stroke(255, 255, 0);
                    app.strokeWeight(2);
                    app.ellipse(tower.getPosition()[0]+32/2, tower.getPosition()[1]+32/2, tower.getRange()*2, tower.getRange()*2);
                    app.stroke(0, 0, 0);
                    app.strokeWeight(1);
                    
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

                // draw bullet
                if (currentMonsterList.size() > 0) {
                    for (Monster monster : currentMonsterList) {
                        float distance = app.dist(tower.getPosition()[0], tower.getPosition()[1], monster.getPosition()[0], monster.getPosition()[1]);
                        if (buttonElement.get("FF").isOn()) {
                            tower.updateFireCoolDown(2.0 / frameRate);
                        } else {
                            tower.updateFireCoolDown(1.0 / frameRate);
                        }
                        if (distance < tower.getRange() && tower.isReadyToFire()) {
                            tower.fire(monster);
                        }
                    }   
                }

                List<Bullet> toRemove = new ArrayList<>();

                if (tower.getBulletList().size() > 0) {
                    for (Bullet bullet : tower.getBulletList()) {
                        bullet.move(buttonElement.get("FF").isOn());
                        bullet.makeDamage();
                        if (! bullet.isHide()) {
                            app.image(bullet.getImage(), bullet.getPosition()[0], bullet.getPosition()[1]);
                        } else {
                            toRemove.add(bullet);
                        }
                    }
                }

                // remove monster
                for (Bullet bulletToRemove : toRemove) {
                    Iterator<Bullet> iterator = tower.getBulletList().iterator();
                    while (iterator.hasNext()) {
                        Bullet b = iterator.next();
                        if (b == bulletToRemove) {
                            iterator.remove(); // Safely remove the monster
                            break;
                }
            }
        }

            }
        }
    }


    public void drawGameLost() {
        app.fill(0, 255, 0);
        app.textSize(30);
        app.text("You LOST", 320, 320);
        app.textSize(20);
        app.text("press 'r' to restart", 320, 360);
    }


    public boolean isGameLost() {
        return gameLost;
    }


    public void buildNewTower() {

        if (mana.getCurrentMana() - towerCost >= 0 && (mapLayout[(int)(app.mouseY-40)/32][(int)app.mouseX/32] == ' ')) {
            if (currentTowerList.size() > 0) {
                for (Tower tower : currentTowerList) {
                    if (tower.isOver(app.mouseX, app.mouseY)) {
                        return;
                    }
                }
            }
            Tower tower = new Tower(app.mouseX, app.mouseY, initialTowerRange, initialTowerFiringSpeed, initialTowerDamage, towerElement, bulletImage);
            currentTowerList.add(tower);
            mana.decreaseCurrentMana(towerCost);
            
        }
        buttonElement.get("T").setButtonStatus(false);

    }

}