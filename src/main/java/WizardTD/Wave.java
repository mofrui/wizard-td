package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;


/**
 * The game will be process in different waves. Each waves is different in time and monsters.
 * 
 * @author Junrui Kang
 * @version 1.0.0
 */
public class Wave {

	private int waveNumber;
	private double duration;
	private double preWavePause;

	private HashMap<Monster, Integer> originalMonsterDict;
	private HashMap<Monster, Integer> monsterDict;
	private int totalMonsterQuantity = 0;

	private boolean started;
	private double spawnInterval;

	// these attributes are used for countdown timers
	private double remainingTime;
	private double remainingPause;
	

	public Wave(int waveNumber, double duration, double preWavePause, HashMap<Monster, Integer> monsterDict) {
		
		this.waveNumber = waveNumber;
		this.duration = duration;
		this.preWavePause = preWavePause;

		this.monsterDict = monsterDict;
		this.originalMonsterDict = monsterDict;
		for (int value : monsterDict.values()) {
            this.totalMonsterQuantity += value;
        } 

		if (waveNumber == 1) {
			started = true; // the wave 1 will immediately start
		} else {
			started = false;
		}
		spawnInterval = 0;

		remainingTime = duration;
		remainingPause = preWavePause;
	}

	
    /**
     * Get the current wave number.
     * 
     * @return current wave number
     */
	public int getWaveNumber() {
		return waveNumber;
	}


    /**
     * Get the duration of the wave.
     * 
     * @return duration of the wave
     */
	public double getDuration() {
		return duration;
	}


    /**
     * Get the pre wave pause of the wave.
     * 
     * @return pre wave pause of the wave
     */
	public double getPreWavePause() {
		return preWavePause;
	}


    /**
     * Get the remaining time of the wave.
     * 
     * @return remianing time of the wave
     */
	public double getRemainingTime() {
		return remainingTime;
	}


    /**
     * Get the remaining pause of the wave.
     * 
     * @return remaining pause of the wave
     */
	public double getRemainingPause() {
		return remainingPause;
	}


    /**
     * Get the monster dictionary of the wave, and used in updating new monster in endless mode.
     * 
     * @return monster dictionary of the wave
     */
	public HashMap<Monster, Integer> getMonsterDict() {
		return originalMonsterDict;
	}


    /**
     * Set the starting status.
     * 
     * @param started starting status
     */
	public void setStarted(boolean started) {
		this.started = started;
	}


    /**
     * Reduce the remaining time by a given time.
     * 
     * @param time time to reduce the remaining time
     */
	public void updateRemainingTime(double time) {
		this.remainingTime = time;
	}


    /**
     * Reduce the remaining pause by a given time.
     * 
     * @param pause time to reduce the remaining pause
     */
	public void updateRemainingPause(double pause) {
		this.remainingPause = pause;
	}


    /**
     * Check whether the wave has started (to spawn monster).
     * 
     * @return whether the wave has started
     */
	public boolean hasStarted() {
		return started;
	}


    /**
     * Check whether the wave has finished to spawn monster).
     * 
     * @return whether the wave has finished
     */
	public boolean hasFinishedSpawning() {
		return (remainingTime <= 0);
	}


    /**
     * Check whether the wave has finished pausing time
     * 
     * @return whether the wave has finished pausing time
     */
	public boolean hasFinishedPausing() {
		return (remainingPause <= 0);
	}


    /**
     * Reduce the spawn interval by a given time.
     * 
     * @param time time to reduce the spawn interval
     */
	public void updateSpawnInterval(double time) {
		spawnInterval -= time;
	}


    /**
     * Check whether the wave is ready to spawn a new monster
     * 
     * @return whether the wave is ready to spawn a new monster
     */
	public boolean isReadyToSpawn() {
		if (spawnInterval <= 0) {
			spawnInterval = duration / totalMonsterQuantity;
			return true;
		}
		return false;
	}


    /**
     * Get a random monster from the monster list to spawn, and decrease the monster list number.
     * 
     * @return a random monster
     */
	public Monster getRandomMonster() {
    	Random random = new Random();
    	while (true) {
    		int randomIndex = random.nextInt(monsterDict.size());
	    	Monster[] keys = monsterDict.keySet().toArray(new Monster[0]);
	    	Monster randomKey = keys[randomIndex];
	    	if (monsterDict.containsKey(randomKey) && monsterDict.get(randomKey) > 0) {
	    		int updatedValue = monsterDict.get(randomKey) - 1;
	            monsterDict.put(randomKey, updatedValue);
	            return randomKey;
	    	}
    	}
    }


   /**
     * Check whether this wave still has monster that has not been spawned.
     * 
     * @return whether this wave still has monster that has not been spawned
     */
    public boolean hasRemainingMonster() {
    	int remainingQuantity = 0;
		for (int value : monsterDict.values()) {
            remainingQuantity += value;
        } 
        return (remainingQuantity > 0);
    }

}