package WizardTD;

import processing.core.PImage;
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


    public double getCurrentMana() {
    	return currentMana;
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
    	if ((currentMana + time * manaGainedRate) <= manaCap) {
    		currentMana += time * manaGainedRate;
    	} else {
            currentMana = manaCap;
        }
    }


    public void increaseCurrentMana(int amount) {
        if ((currentMana + amount) <= manaCap) {
            currentMana += amount;
        } else {
            currentMana = manaCap;
        }
    }


    public void decreaseCurrentMana(int amount) {
        if (currentMana - amount >= 0) {
            currentMana -= amount;
        } else {
            currentMana = -0.1;
        }
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