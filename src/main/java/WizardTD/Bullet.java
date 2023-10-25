package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;


public class Bullet {

	private float x;
	private float y;
	private int damage;
	private Monster target;
	private PImage image;

	private boolean hide;


	public Bullet(float x, float y, int damage, Monster target, PImage image) {

		this.x = x;
		this.y = y;
		this.damage = damage;
		this.target = target;
		this.image = image;

		this.hide = false;
	}


	public float[] getPosition() {
		return new float[]{x, y};
	}


	public void move(boolean speedUp) {
		
		// get target position
		float targetX = target.getPosition()[0];
        float targetY = target.getPosition()[1];

        double angle = Math.atan2(targetY - y, targetX - x);
        if (speedUp){
            x += Math.cos(angle) * 10;
            y += Math.sin(angle) * 10;
        } else {
            x += Math.cos(angle) * 5;
            y += Math.sin(angle) * 5;
        }
	}


	public void makeDamage() {

		float targetX = target.getPosition()[0];
        float targetY = target.getPosition()[1];
		
		if (Math.abs(x - targetX) < 1 && Math.abs(y - targetY) < 1) {
			target.takeDamage(damage);
			hide = true;
		}
	}


	public void checkTargetLost() {
		if (target.getCurrentHp() == 0) {
			hide = true;
		}
	}


	public PImage getImage() {
		return image;
	}


	public boolean isHide() {
		return hide;
	}


}