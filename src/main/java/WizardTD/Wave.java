package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;


public class Wave {

	private int waveNumber;
	private double duration;
	private double preWavePause;

	private HashMap<Monster, Integer> monsterDict;
	private int totalMonsterQuantity;

	private boolean started;

	// these attributes are used for countdown timers
	private double remainingTime;
	private double remainingPause;
	

	public Wave(int waveNumber, double duration, double preWavePause, HashMap<Monster, Integer> monsterDict) {
		
		this.waveNumber = waveNumber;
		this.duration = duration;
		this.preWavePause = preWavePause;

		this.monsterDict = monsterDict;
		for (int value : monsterDict.values()) {
            this.totalMonsterQuantity += value;
        } 

		if (waveNumber == 1) {
			started = true; // the wave 1 will immediately start
		} else {
			started = false;
		}
		remainingTime = duration;
		remainingPause = preWavePause;
	}

	
	public int getWaveNumber() {
		return waveNumber;
	}


	public double getDuration() {
		return duration;
	}


	public double getPreWavePause() {
		return preWavePause;
	}


	public double getRemainingTime() {
		return remainingTime;
	}


	public double getRemainingPause() {
		return remainingPause;
	}


	public void setStarted(boolean started) {
		this.started = started;
	}


	public void updateRemainingTime(double time) {
		this.remainingTime = time;
	}


	public void updateRemainingPause(double pause) {
		this.remainingPause = pause;
	}


	public boolean hasStarted() {
		return started;
	}


	public boolean hasFinishedSpawning() {
		return (remainingTime <= 0);
	}


	public boolean hasFinishedPausing() {
		return (remainingPause <= 0);
	}


	public double getSpawnInterval(boolean fast) {
		if (fast) {
			return ((duration/2) / totalMonsterQuantity);
		} else {
			return (duration / totalMonsterQuantity);
		}
		
	}


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


    public boolean hasRemainingMonster() {
    	int remainingQuantity = 0;
		for (int value : monsterDict.values()) {
            remainingQuantity += value;
        } 
        return (remainingQuantity > 0);
    }

}