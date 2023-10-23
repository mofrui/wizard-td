package WizardTD;

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

	public String getName() {
		return name;
	}

	public String getDesciption() {
		return description;
	}

	public char getHotKey() {
		return hotKey;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

    public void updateMouseOver(boolean mouseOver) {
    	this.mouseOver = mouseOver;
    }

    public void updateStatus() {
    	status = !status;
    }


    public void setButtonStatus(boolean newStatus) {
    	status = newStatus;
    }


    public boolean hasMouseOver() {
    	return mouseOver;
    }

    public boolean isOn() {
    	return status;
    }

}