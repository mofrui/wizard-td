package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;


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


	public void setPosition(float xPos, float yPos) {
		x = (int) xPos/32;
		y = (int) (yPos-40)/32;
	}


	public PImage getImage() {
		return image;
	}


	public void updateImage() {
		if (rangeLevel >= 2 && speedLevel >= 2 && damageLevel >= 2) {
            image = imageList.get(2);
            allLevel = 2;
        } else if (rangeLevel >= 1 && speedLevel >= 1 && damageLevel >= 1) {
			image = imageList.get(1);
			allLevel = 1;
        }
	}


	public float[] getPosition() {
        return new float[]{x * 32, y * 32 + 40};
    }


    public boolean isOver(float xPos, float yPos) {
    	return ((x == (int)(xPos/32)) && (y == (int)((yPos-40)/32)));
    }


    public int getRange() {
    	return range;
    }


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


    public void fire(Monster target) {
    	Bullet bullet = new Bullet(this.getPosition()[0], this.getPosition()[1], damage, target, bulletImage);
    	bulletList.add(bullet);
    	fireCoolDown = 1/speed;
    }


    public void updateFireCoolDown(double time) {
    	fireCoolDown -= time;
    }


    public boolean isReadyToFire() {
    	return (fireCoolDown <= 0);
    }


    public List<Bullet> getBulletList() {
    	return bulletList;
    }

}