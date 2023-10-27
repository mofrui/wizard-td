package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;


/**
 * Button can be clicked and will affect the gameplay based on their uses.
 * 
 * @author Junrui Kang
 * @version 1.0.0
 */
public class Button {

	private String name;
	private String description;
	private char hotKey;

	private boolean mouseOver;
	private boolean status;

	private int x;
	private int y;


	public Button(String name, String description, char hotKey, int x, int y) {

		this.name = name;
		this.description = description;
		this.hotKey = hotKey;
		this.x = x;
		this.y = y;

		mouseOver = false;
		status = false;
	}


	/**
	 * Get the name of the button.
	 * 
	 * @return Name of the button.
	 */
	public String getName() {
		return name;
	}


	/**
	 * Get the description of the button.
	 * 
	 * @return Description of the button.
	 */
	public String getDesciption() {
		return description;
	}


	/**
	 * Get the hotkey of the button.
	 * 
	 * @return Hotkey of the button.
	 */
	public char getHotKey() {
		return hotKey;
	}


	/**
	 * Get the x-position of the bullet.
	 * 
	 * @return x-position of the bullet.
	 */
	public int getX() {
		return x;
	}


	/**
	 * Get the y-position of the bullet.
	 * 
	 * @return y-position of the bullet
	 */
	public int getY() {
		return y;
	}


	/**
	 * Update the status of whether the mouse is over the button.
	 * 
	 * @param mouseOver A boolean that whether the mouse is over the button.
	 */
    public void updateMouseOver(boolean mouseOver) {
    	this.mouseOver = mouseOver;
    }


	/**
	 * Update the status of whether the button is on or off by inverting the status.
	*/
    public void updateStatus() {
    	status = !status;
    }


	/**
	 * Update the status of whether the button is on or off by providing with the status.
	 * 
	 * @param newStatus A boolean that the button is on or off.
	*/
    public void setButtonStatus(boolean newStatus) {
    	status = newStatus;
    }


	/**
	 * Get the status of whether the mouse is over the button.
	 * 
	 * @return Status of whether the mouse is over the button.
	 */
    public boolean hasMouseOver() {
    	return mouseOver;
    }


	/**
	 * Get the status of whether the button is on or off.
	 * 
	 * @return Status of the button is on or off.
	 */
    public boolean isOn() {
    	return status;
    }

}