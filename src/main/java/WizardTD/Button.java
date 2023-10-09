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


public class Button {

	private String name;
	private String description;

	private boolean over;
	private boolean clicked;

	private int x;
	private int y;

	public Button(String name, String description, int x, int y) {

		this.name = name;
		this.description = description;
		this.x = x;
		this.y = y;

		over = false;
		clicked = false;
	}

	public String getName() {
		return name;
	}

	public String getDesciption() {
		return description;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

    public void setOver(boolean over) {
    	this.over = over;
    }

    public void setClicked() {
    	clicked = !clicked;
    }


    public boolean hasOver() {
    	return over;
    }

    public boolean hasClicked() {
    	return clicked;
    }

}