package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;


/**
 * Tower has the ability to attack monster using its bullet.
 * 
 * @author Junrui Kang
 * @version 1.0.0
 */
public class Tower {

	private int x;
	private int y;

	private int range;
	private int rangeLevel;

	private double speed;
	private int speedLevel;

	private int damage;
	private int damageLevel;
	private int initialDamage;

	private int allLevel;

	private PImage image;
	private HashMap<Integer, PImage> imageList;
	private PImage bulletImage;

	private List<Bullet> bulletList;

	private double fireCoolDown;


	public Tower(float xPos, float yPos, int range, double speed, int damage, HashMap<Integer, PImage> imageList, PImage bulletImage) {

		setPosition(xPos, yPos);

		this.range = range;
		this.rangeLevel = 0;

		this.speed = speed;
		this.speedLevel = 0;

		this.damage = damage;
		this.damageLevel = 0;
		this.initialDamage = damage;

		this.allLevel = 0;

		this.imageList = imageList;
		this.image = imageList.get(0);
		this.bulletImage = bulletImage;

		this.bulletList = new ArrayList<Bullet>();

		this.fireCoolDown = 0;

	}


	/**
	 * Set the position of the tower based on the mouse position.
	 * 
	 * @param xPos the pixel coordinate for x
	 * @param yPos the pixel coordinate for y
	 */
	public void setPosition(float xPos, float yPos) {
		x = (int) xPos/32;
		y = (int) (yPos-40)/32;
	}


    /**
     * Get the image of the tower.
     * 
     * @return tower image
     */
	public PImage getImage() {
		return image;
	}


    /**
     * Update the image of the tower based on the upgrade level.
     */
	public void updateImage() {
		if (rangeLevel >= 7 && speedLevel >= 7 && damageLevel >= 7) {
		    image = imageList.get(7);
		    allLevel = 7;
		} else if (rangeLevel >= 6 && speedLevel >= 6 && damageLevel >= 6) {
		    image = imageList.get(6);
		    allLevel = 6;
		} else if (rangeLevel >= 5 && speedLevel >= 5 && damageLevel >= 5) {
		    image = imageList.get(5);
		    allLevel = 5;
		} else if (rangeLevel >= 4 && speedLevel >= 4 && damageLevel >= 4) {
		    image = imageList.get(4);
		    allLevel = 4;
		} else if (rangeLevel >= 3 && speedLevel >= 3 && damageLevel >= 3) {
		    image = imageList.get(3);
		    allLevel = 3;
		} else if (rangeLevel >= 2 && speedLevel >= 2 && damageLevel >= 2) {
		    image = imageList.get(2);
		    allLevel = 2;
		} else if (rangeLevel >= 1 && speedLevel >= 1 && damageLevel >= 1) {
		    image = imageList.get(1);
		    allLevel = 1;
		}
	}


    /**
     * Get the position of the tower.
     * 
     * @return tower position in {x,y}
     */
	public float[] getPosition() {
        return new float[]{x * 32, y * 32 + 40};
    }


    /**
     * Check whether the given position is the same as this tower.
     * 
	 * @param xPos the pixel coordinate for x
	 * @param yPos the pixel coordinate for y
     * @return whether the given position is the same as this tower
     */
    public boolean isOver(float xPos, float yPos) {
    	return ((x == (int)(xPos/32)) && (y == (int)((yPos-40)/32)));
    }


    /**
     * Get the range of the tower.
     * 
     * @return tower range
     */
    public int getRange() {
    	return range;
    }


    /**
     * Get the upgrade cost of a specific level of the tower.
     * 
     * @param toUpgrade the specific upgrade part (range, speed, damage)
     * @return upgrade cost
     */
    public int getUpgradeCost(char toUpgrade) {

    	int cost = 0;

    	switch (toUpgrade) {
        case 'r':
            cost = (20 + rangeLevel * 10);
            break;
        case 's':
            cost = (20 + speedLevel * 10);
            break;
        case 'd':
            cost = (20 + damageLevel * 10);
            break;
    	}

    	return cost;
    }


    /**
     * Upgrade the tower (range, speed, damage)
     * 
     * @param toUpgrade the specific upgrade part (range, speed, damage)
     */
    public void upgradeTower(char toUpgrade) {
    	switch (toUpgrade) {
        case 'r':
        	rangeLevel += 1;
        	range += 32;
            break;
        case 's':
            speedLevel += 1;
        	speed += 0.5;
            break;
        case 'd':
            damageLevel += 1;
        	damage += (0.5 * initialDamage);
            break;
		}
    }


    /**
     * Get the tower upgrade level (range, speed, damage)
     * 
     * @param toCheck the specific upgrade part (range, speed, damage)
     * @return the corresponding level
     */
    public int getUpgradeLevel(char toCheck) {
    	int level = 0;

    	switch (toCheck) {
        case 'r':
            level = rangeLevel - allLevel;
            break;
        case 's':
            level = speedLevel - allLevel;
            break;
        case 'd':
            level = damageLevel - allLevel;
            break;
    	}

    	return level;
    }


    /**
     * The tower will fire a bullet to the given target.
     * 
     * A bullet object will be instantiated here.
     * 
     * @param target monster to attack
     */
    public void fire(Monster target) {
    	Bullet bullet = new Bullet(this.getPosition()[0], this.getPosition()[1], damage, target, bulletImage);
    	bulletList.add(bullet);
    	fireCoolDown = 1/speed;
    }


    /**
     * Reduce the fire cooldown by a given time.
     * 
     * @param time time to reduce the fire cooldown
     */
    public void updateFireCoolDown(double time) {
    	fireCoolDown -= time;
    }


    /**
     * Check whether the fire cooldown is 0.
     * 
     * @return whether the tower is ready to fire
     */
    public boolean isReadyToFire() {
    	return (fireCoolDown <= 0);
    }


    /**
     * Get the current bullet list of the tower for the game to draw.
     * 
     * @return bullet list
     */
    public List<Bullet> getBulletList() {
    	return bulletList;
    }

}