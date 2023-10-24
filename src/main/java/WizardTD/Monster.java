package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;


public class Monster {
	
	private String type;
	private PImage image;
	private List<PImage> deathAnimation;

    private int fullHp;
    private int currentHp;

    private double speed;
    private double armour;
    private int manaGainedOnKill;

    private char[][] mapLayout;

    private Point startPosition;
    private Point currentPosition;
    private float x;
    private float y;
    private int xStart;
    private int yStart;

    private boolean dying;
    private int dyingCounter;
    private boolean dead;

    private char lastMove = ' ';
    private boolean firstMove = true;


    public Monster(String type, PImage image, List<PImage> deathAnimation, int hp, double speed, double armour, int manaGainedOnKill, char[][] mapLayout) {

    	this.type = type;
    	this.image = image;
    	this.deathAnimation = deathAnimation;

    	this.fullHp = hp;
    	this.currentHp = hp;

    	this.speed = speed;
    	this.armour = armour;
    	this.manaGainedOnKill = manaGainedOnKill;

        this.mapLayout = mapLayout;
        
        setSpawnPosition();
        findPath();

        this.dying = false;
        this.dyingCounter = 0;
        this.dead = false;

    }


	public String getType() {
        return type;
    }


	public int getFullHp() {
		return fullHp;
    }


	public int getCurrentHp() {
		return currentHp;
    }


	public double getSpeed() {
		return speed;
    }


    public double getArmour() {
        return armour;
    }


    public int getManaGainedOnKill() {
        return manaGainedOnKill;
    }


    public float[] getPosition() {
        return new float[]{x * 32 + 5, y * 32 + 45};
    }


    public PImage getImage() {
        return image;
    }


    public List<PImage> getDeathAnimation() {
        return deathAnimation;
    }


    public float getHpPercentage() {
        return ((int)currentHp * 29 / fullHp);
    }


    public void takeDamage(int damage) {
        currentHp -= armour * damage;
        if (currentHp <= 0) {
            currentHp = 0;
            dying = true;
        }
    }


    public void updateDyingImage() {
        if (dying){
            switch(dyingCounter) {
            case 3:
                image = deathAnimation.get(0);
                break;
            case 7:
                image = deathAnimation.get(1);
                break;
            case 11:
                image = deathAnimation.get(2);
                break;
            case 15:
                image = deathAnimation.get(3);
                break;
            case 29:
                image = deathAnimation.get(4);
                break;
            }
            dyingCounter += 1;  
        }
        if (dyingCounter == 20) {
            dying = false;
            dead = true;
        }
    }


    public boolean isDying() {
        return dying;
    }


    public boolean isDead() {
        return dead;
    }


    public void setSpawnPosition() {

        List<int[]> spawnPositions = new ArrayList<>();

        // iterate through the top and bottom rows
        for (int i = 0; i < mapLayout[0].length; i+=1) {
            if (mapLayout[0][i] == 'X') {
                spawnPositions.add(new int[]{i, 0});
            }
            if (mapLayout[mapLayout.length - 1][i] == 'X') {
                spawnPositions.add(new int[]{i, (mapLayout.length - 1)});
            }
        }

        // iterate through the left and right columns
        for (int j = 0; j < mapLayout.length; j+=1) {
            if (mapLayout[j][0] == 'X') {
                spawnPositions.add(new int[]{0, j});
            }
            if (mapLayout[j][mapLayout[0].length - 1] == 'X') {
                spawnPositions.add(new int[]{(mapLayout[0].length - 1), j});
            }
        }

        // use modulo to randomly spawn a monster in random starting position
        Random random = new Random();
        int randomIndex = random.nextInt(spawnPositions.size());
        x = spawnPositions.get(randomIndex)[0];
        xStart = (int)x;
        y = spawnPositions.get(randomIndex)[1];
        yStart = (int)y;
    }


    public void findPath() {

        Queue<Point> queue = new LinkedList<>();

        // the starting point will not contain and parent point
        Point start = new Point(xStart, yStart, null);
        Point end = null;

        queue.add(start);

        // this 2D array matches with mapLayout array to record all visited points
        boolean[][] visited = new boolean[20][20];
        visited[yStart][xStart] = true;

        while (!queue.isEmpty()) {
            Point currentPoint = queue.poll();
            int x = currentPoint.x;
            int y = currentPoint.y;

            // reached to the wizard house
            if (mapLayout[y][x] == 'W') {
                end = currentPoint;
                break;
            }

            // movements to explore the neibouring points by moving 1 unit at a time
            int[] dx = {0, 1, 0, -1};
            int[] dy = {1, 0, -1, 0};

            for (int i = 0; i < 4; i += 1) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                // check the validity of the point
                // 1. in the index range
                // 2. not visited
                // 3. is a path (X)
                if (newX < 20 && newX >= 0 && newY < 20 && newY >= 0) {
                    if (!visited[newY][newX] && (mapLayout[newY][newX] == 'X' || mapLayout[newY][newX] == 'W')) {
                        // update the visit status
                        visited[newY][newX] = true;
                        Point neighbor = new Point(newX, newY, currentPoint);
                        queue.add(neighbor);
                    } 
                }

            }
        }

        // if the finish point is found, join all points to form a path
        if (end != null) {
            // starting from the end point
            Point current = end;
            // the loop will end if this current point is the starting point (pointFrom = null)
            while (current != null) {
                if (current.pointFrom != null) {
                    current.pointFrom.setNextPoint(current);
                } else {
                    startPosition = current;
                    currentPosition = current;
                }
                // get the pointfrom
                current = current.pointFrom;
            }
        }

    }


    public void move(float distance) {
        double limit = distance / 10;
        // System.out.println(lastMove);
        if (currentPosition.pointNext != null) {
            if (firstMove || lastMove == 'n' || (Math.abs(x - currentPosition.pointNext.x) <= limit && Math.abs(y - currentPosition.pointNext.y) <= limit)) {
                currentPosition = currentPosition.pointNext;
                firstMove = false;
                lastMove = ' ';
            } else if (Math.abs(x - currentPosition.pointNext.x) != 0 && Math.abs(y - currentPosition.pointNext.y) <= limit) {
                if (x > currentPosition.pointNext.x) {
                    x -= distance;
                    if (lastMove == 'r') {
                        lastMove = 'n';
                    } else {
                        lastMove = 'l';
                    }
                } else {
                    x += distance;
                    if (lastMove == 'l') {
                        lastMove = 'n';
                    } else {
                        lastMove = 'r';
                    }
                }
            } else if (Math.abs(x - currentPosition.pointNext.x) <= limit && Math.abs(y - currentPosition.pointNext.y) != 0) {
                if (y > currentPosition.pointNext.y) {
                    y -= distance;
                    if (lastMove == 'd') {
                        lastMove = 'n';
                    } else {
                        lastMove = 'u';
                    }
                } else {
                    y += distance;
                    if (lastMove == 'u') {
                        lastMove = 'n';
                    } else {
                        lastMove = 'd';
                    }
                }
            } else {
                // System.out.println("what to do?");
                // System.out.println(x);
                // System.out.println(currentPosition.pointNext.x);
                // System.out.println(y);
                // System.out.println(currentPosition.pointNext.y);
                // System.out.println(limit);
                if ((int) Math.abs(x - currentPosition.pointNext.x) == 0) {
                    x = currentPosition.pointNext.x;
                    // System.out.println("change x");
                    // System.out.println(x);
                } else if ((int) Math.abs(y - currentPosition.pointNext.y) == 0) {
                    y = currentPosition.pointNext.y;
                    // System.out.println("change y");
                    // System.out.println(y);
                }
            }
        }
    }


    public boolean hasReachedWizardHouse() {
        return (currentPosition.pointNext == null);
    }


    public void banishMonster() {
        x = xStart;
        y = yStart;
        currentPosition = startPosition;
    }

}



/**
 * A point contains infomation of the current point coordinate and its connected parent point
 */ 
class Point {

    int x , y;
    Point pointFrom;
    Point pointNext;

    public Point(int x, int y, Point pointFrom) {
        this.x = x;
        this.y = y;
        this.pointFrom = pointFrom;
        this.pointNext = null;
    }


    public void setNextPoint(Point point) {
        pointNext = point;
    }

}


