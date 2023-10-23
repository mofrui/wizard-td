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
    private int wizard_house_x, wizard_house_y;

	private List<Wave> wavesList;
	private Wave currentWave;
    private Wave nextWave;
    private int currentWaveNumber;
    private int totalWaveNumber;

    private Mana mana; 

	private HashMap<String, Button> buttonElement;


	public GameInterface(PApplet app, char[][] mapLayout, HashMap<String,PImage> mapElement, List<Wave> wavesList, Mana mana, HashMap<String, Button> buttonElement) {
		this.app = app;

		this.mapLayout = mapLayout;
		this.mapElement = mapElement;
        wizard_house_x = 0;
        wizard_house_y = 0;

		this.wavesList = wavesList;
		currentWaveNumber = 0;
		totalWaveNumber = wavesList.size();

        this.mana = mana;

		this.buttonElement = buttonElement;
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
        app.text(mana.getCurrentMana() + "/" + mana.getManaCap(), 520, 27);

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

        for (Monster monster : currentWave.getMonsterList()) {
            app.image(monster.getImage(), monster.getPosition()[0], monster.getPosition()[1]);
            if (!buttonElement.get("P").isOn()) {
                if (buttonElement.get("FF").isOn()) {
                    monster.move((float)(2 * monster.getSpeed()/frameRate));
                } else {
                    monster.move((float)(1 * monster.getSpeed()/frameRate));
                }
            }   
        }

    }

}