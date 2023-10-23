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


public class Wave {

	private int waveNumber;
	private double duration;
	private double preWavePause;

	private List<Monster> monsterList;
	private int monsterQuantity;

	private boolean started;

	// these attributes are used for countdown timers
	private double remainingTime;
	private double remainingPause;
	
	public Wave(int waveNumber, double duration, double preWavePause, List<Monster> monsterList, int monsterQuantity) {
		
		this.waveNumber = waveNumber;
		this.duration = duration;
		this.preWavePause = preWavePause;

		this.monsterList = monsterList;
		this.monsterQuantity = monsterQuantity;

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

	public int getMonsterQuantity() {
        return monsterQuantity;
    }

    public List<Monster> getMonsterList() {
    	return monsterList;
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
}