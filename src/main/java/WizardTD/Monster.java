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


public class Monster {
	
	private String type;
	private PImage image;
	private List<PImage> deathAnimation;

    private int fullHp;
    private int currentHp;

    private double speed;
    private double armour;
    private int manaGainedOnKill;

    public Monster(String type, PImage image, List<PImage> deathAnimation, int hp, double speed, double armour, int manaGainedOnKill) {

    	this.type = type;
    	this.image = image;
    	this.deathAnimation = deathAnimation;

    	this.fullHp = hp;
    	this.currentHp = hp;

    	this.speed = speed;
    	this.armour = armour;
    	this.manaGainedOnKill = manaGainedOnKill;

    }

	public String getType() {
        return type;
    }

	public int getInitialHp() {
		return initialHp;
    }

	public int getCurrentHp() {
		return currentHp;
    }

	public double getSpeed() {
		return speed;
    }

    public double getArmour() {
        return armour;
    }

    public int getManaGainedOnKill() {
        return manaGainedOnKill;
    }

}