package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;



/**
 * Bullet is fired from the tower to attack a target monster.
 * 
 * @author Junrui Kang
 * @version 1.0.0
 */
public class Bullet {

	private float x;
	private float y;
	private int damage;
	private Monster target;
	private PImage image;

	private boolean hide;


	public Bullet(float x, float y, int damage, Monster target, PImage image) {

		this.x = x + 16;
		this.y = y + 16;
		this.damage = damage;
		this.target = target;
		this.image = image;

		this.hide = false;
	}


	/**
	 * Get the current position of the bullet in pixel.
	 * 
	 * @return Position of the bullet in a 2d array.
	 */
	public float[] getPosition() {
		return new float[]{x, y};
	}


	/**
	 * Move the bullet based on the relative angle with its target.
	 * 
	 * @param speedUp A boolean that indicates the speed of the game
	 */
	public void move(boolean speedUp) {
		
		// get target position
		float targetX = target.getPosition()[0] + 10;
        float targetY = target.getPosition()[1] + 10;

        double angle = Math.atan2(targetY - y, targetX - x);
        if (speedUp){
            x += Math.cos(angle) * 10;
            y += Math.sin(angle) * 10;
        } else {
            x += Math.cos(angle) * 5;
            y += Math.sin(angle) * 5;
        }
	}


	/**
	 * Check whether the bullet reaches the target monster, and make damage.
	 * 
	 * The bullet will be hided and removed after making the damage.
	 */
	public void makeDamage() {

		float targetX = target.getPosition()[0] + 10;
        float targetY = target.getPosition()[1] + 10;
		
		if (Math.abs(x - targetX) < 1 && Math.abs(y - targetY) < 1) {
			target.takeDamage(damage);
			hide = true;
		}
	}


	/**
	 * Check whether the target is dead or lost.
	 * 
	 * When a monster is dead, its hp will be 0 or it will no longer exist (null).
	 */
	public void checkTargetLost() {
		if (target.getCurrentHp() == 0 || target == null) {
			hide = true;
		}
	}


	/**
	 * Get the image of the bullet.
	 * 
	 * @return Image of the bullet
	 */
	public PImage getImage() {
		return image;
	}


	/**
	 * Get whether the bullet is hided or not.
	 * 
	 * @return A boolean of hide status.
	 */
	public boolean isHide() {
		return hide;
	}


}