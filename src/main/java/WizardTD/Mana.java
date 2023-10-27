package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;


/**
 * Wizard's mana is displayed on the top of the screen, and it is the 'currency' in the game.
 * 
 * @author Junrui Kang
 * @version 1.0.0
 */
public class Mana {

	private double currentMana;
    private int manaCap;

    private double manaGainedRate;
    private double initialManaGainedRate;

    private int manaPoolSpellCost;
    private int manaPoolSpellCostIncreasePerUse;
    private double manaPoolSpellCapMultiplier;
    private double manaPoolSpellManaGainedMultiplier;


    public Mana(int initialMana, int initialManaCap, double initialManaGainedPerSecond, int manaPoolSpellInitialCost, int manaPoolSpellCostIncreasePerUse, double manaPoolSpellCapMultiplier, double manaPoolSpellManaGainedMultiplier) {

    	this.currentMana = initialMana;
    	this.manaCap = initialManaCap;

        this.manaGainedRate = initialManaGainedPerSecond;
        this.initialManaGainedRate = initialManaGainedPerSecond;

    	this.manaPoolSpellCost = manaPoolSpellInitialCost;
    	this.manaPoolSpellCostIncreasePerUse = manaPoolSpellCostIncreasePerUse;
    	this.manaPoolSpellCapMultiplier = manaPoolSpellCapMultiplier;
    	this.manaPoolSpellManaGainedMultiplier = manaPoolSpellManaGainedMultiplier;
    }


    /**
     * Get current remaining mana.
     * 
     * @return Current remaining mana
     */
    public double getCurrentMana() {
    	return currentMana;
    }


    /**
     * Get the mana cap.
     * 
     * @return Mana cap
     */
    public int getManaCap() {
    	return manaCap;
    }


    /**
     * Get the cost to upgrade mana.
     * 
     * @return Cost
     */
    public int getManaPoolSpellCost() {
    	return manaPoolSpellCost;
    }


    /**
     * Get the percentage of current mana (out of the mana cap).
     * 
     * @return Current mana percentage
     */
    public float getManaPercentage() {
    	return ((int)currentMana * 300 / manaCap);
    }


    /**
     * Increase the current mana by time in the game.
     * 
     * @param time time passed to calculate the mana amount
     */
    public void increaseCurrentManaByTime(double time) {
    	if ((currentMana + time * manaGainedRate) <= manaCap) {
    		currentMana += time * manaGainedRate;
    	} else {
            currentMana = manaCap;
        }
    }


    /**
     * Increase the current mana by a given amount.
     * 
     * @param amount amount of mana
     */
    public void increaseCurrentMana(int amount) {
        if ((currentMana + amount * manaGainedRate) <= manaCap) {
            currentMana += amount * manaGainedRate;
        } else {
            currentMana = manaCap;
        }
    }


    /**
     * Decrease the current mana by a given amount.
     * 
     * @param amount amount of mana
     */
    public void decreaseCurrentMana(int amount) {
        if (currentMana - amount >= 0) {
            currentMana -= amount;
        } else {
            currentMana = -0.1;
        }
    }


    /**
     * Upgrade the mana pool, and decrease the current mana.
    */
    public void upgradeMana() {
    	if (currentMana - manaPoolSpellCost >= 0) {
            manaCap *= manaPoolSpellCapMultiplier;
            manaGainedRate = initialManaGainedRate * manaPoolSpellManaGainedMultiplier;
            currentMana -= manaPoolSpellCost;
	    	manaPoolSpellCost += manaPoolSpellCostIncreasePerUse;
    	}
    }


	
}