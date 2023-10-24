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

	private PImage image;
	private HashMap<Integer, PImage> imageList;
	private PImage bulletImage;

	private List<Bullet> bulletList;

	private double fireCoolDown;


	public Tower(float xPos, float yPos, int range, double speed, int damage, HashMap<Integer, PImage> imageList, PImage bulletImage) {

		setPosition(xPos, yPos);

		this.range = range;
		this.rangeLevel = 1;

		this.speed = speed;
		this.speedLevel = 1;

		this.damage = damage;
		this.damageLevel = 1;
		this.initialDamage = damage;

		this.imageList = imageList;
		this.image = imageList.get(1);
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

	public float[] getPosition() {
        return new float[]{x * 32, y * 32 + 40};
    }


    public boolean isOver(float xPos, float yPos) {
    	return ((x == (int)(xPos/32)) && (y == (int)((yPos-40)/32)));
    }


    public int getRange() {
    	return range;
    }


    public int getUpgradeCost(char typeFlag) {

    	int cost = 0;

    	switch (typeFlag) {
        case 'r':
            cost = (rangeLevel < 3 ? 10 + rangeLevel * 10 : 0);
            break;
        case 's':
            cost = (speedLevel < 3 ? 10 + speedLevel * 10 : 0);
            break;
        case 'd':
            cost = (damageLevel < 3 ? 10 + damageLevel * 10 : 0);
            break;
    }

    	return cost;

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