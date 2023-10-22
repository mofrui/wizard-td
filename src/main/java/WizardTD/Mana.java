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


public class Mana {

	private double currentMana;
    private int manaCap;
    private double manaGainedRate;

    private int manaPoolSpellCost;
    private int manaPoolSpellCostIncreasePerUse;
    private double manaPoolSpellCapMultiplier;
    private double manaPoolSpellManaGainedMultiplier;

    public Mana(int initialMana, int initialManaCap, double initialManaGainedPerSecond, int manaPoolSpellInitialCost, int manaPoolSpellCostIncreasePerUse, double manaPoolSpellCapMultiplier, double manaPoolSpellManaGainedMultiplier) {

    	this.currentMana = initialMana;
    	this.manaCap = initialManaCap;
    	this.manaGainedRate = initialManaGainedPerSecond;

    	this.manaPoolSpellCost = manaPoolSpellInitialCost;
    	this.manaPoolSpellCostIncreasePerUse = manaPoolSpellCostIncreasePerUse;
    	this.manaPoolSpellCapMultiplier = manaPoolSpellCapMultiplier;
    	this.manaPoolSpellManaGainedMultiplier = manaPoolSpellManaGainedMultiplier;
    }

    public int getCurrentMana() {
    	return (int)currentMana;
    }

    public int getManaCap() {
    	return manaCap;
    }

    public int getManaPoolSpellCost() {
    	return manaPoolSpellCost;
    }

    public float getManaPercentage() {
    	return ((int)currentMana * 300 / manaCap);
    }

    public void increaseCurrentManaByTime(double time) {
    	if (currentMana < manaCap) {
    		currentMana += time * manaGainedRate;
    	}
    }

    public void decreaseCurrentMana() {
    }

    public void upgradeMana() {
    	if (currentMana - manaPoolSpellCost >= 0) {
    		manaCap *= manaPoolSpellCapMultiplier;
	    	manaGainedRate *= manaPoolSpellManaGainedMultiplier;
	    	currentMana -= manaPoolSpellCost;
	    	manaPoolSpellCost += manaPoolSpellCostIncreasePerUse;
    	}
    }


	
}