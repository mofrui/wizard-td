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


	public Tower(float xPos, float yPos, int range, double speed, int damage, HashMap<Integer, PImage> imageList) {

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

}