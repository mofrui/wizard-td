package WizardTD;

import processing.core.PImage;
import java.io.*;
import java.util.*;


/**
 * Monster will be spawned in the game, and move along to path to the Wizard House.
 * 
 * @author Junrui Kang
 * @version 1.0.0
 */
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


    /**
     * Get the type of the monster.
     * 
     * @return monster type
     */
	public String getType() {
        return type;
    }


    /**
     * Get the full hp of the monster.
     * 
     * @return full hp
     */
	public int getFullHp() {
		return fullHp;
    }


    /**
     * Get the current hp of the monster.
     * 
     * @return current hp
     */
	public int getCurrentHp() {
		return currentHp;
    }


    /**
     * Get the speed of the monster.
     * 
     * @return monster speed
     */
	public double getSpeed() {
		return speed;
    }


    /**
     * Get the armour of the monster.
     * 
     * @return monster armour
     */
    public double getArmour() {
        return armour;
    }


    /**
     * Get the amount of mana when the monster is killed.
     * 
     * @return mana gained on kill
     */
    public int getManaGainedOnKill() {
        return manaGainedOnKill;
    }


    /**
     * Get the position of the monster.
     * 
     * @return monster position in {x,y}
     */
    public float[] getPosition() {
        return new float[]{x * 32 + 5, y * 32 + 45};
    }


    /**
     * Get the image of the monster.
     * 
     * @return monster image
     */
    public PImage getImage() {
        return image;
    }


    /**
     * Get the death animatopn of the monster.
     * 
     * @return monster death animation in a list
     */
    public List<PImage> getDeathAnimation() {
        return deathAnimation;
    }


    /**
     * Get the percentage of hp out of full hp of the monster.
     * 
     * @return percentage of hp
     */
    public float getHpPercentage() {
        return ((int)currentHp * 29 / fullHp);
    }


    /**
     * Monster's hp will be decreased when it takes damage.
     * 
     * @param damage damage taken
     */
    public void takeDamage(int damage) {
        currentHp -= armour * damage;
        if (currentHp <= 0) {
            currentHp = 0;
            dying = true;
        }
    }


    /**
     * Monster's image will be updated to its death animation when it is dying.
     */
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


    /**
     * Get whether the monster is dying.
     * 
     * @return monster dying status
     */
    public boolean isDying() {
        return dying;
    }


    /**
     * Get whether the monster is dead. It will be removed when it is dead.
     * 
     * @return monster dead status
     */
    public boolean isDead() {
        return dead;
    }


    /**
     * The spawn position of the monster will be determined based on the map layout.
     * 
     * It will extract all 'X' at the edge of the map, and select a random point to spawn.
    */
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


    /**
     * Monster's path finding algorithm using BFS search.
     * 
     * All the point 'X' that can be reached by the monster will be found first, and stored in a queue.
     * 
     * These points have the attribute of the previous point pointFrom.
     * 
     * The path finding starts from the end point (wizard's house).
     * 
     * During the finding process, all points will be linked together.
     * 
     * These point will be updated with an attribute of pointNext.
     * 
     * The path stops when it reaches the start position, where it does not have a pointFrom.
     * 
     * Then the starting point of the monster will be set as currentPosition.
     */
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


    /**
     * The monster moves with a given distance value.
     * 
     * @param distance distance to move each frame
     */
    public void move(float distance) {
        double limit = distance / 20;

        // if there is a next point, the monster will move towards it
        if (currentPosition.pointNext != null) {

            // if the monster reaches the next point, set the current point as it
            if (firstMove || lastMove == 'n' || (Math.abs(x - currentPosition.pointNext.x) <= limit && Math.abs(y - currentPosition.pointNext.y) <= limit)) {
                currentPosition = currentPosition.pointNext;
                firstMove = false;
                lastMove = ' ';

            // the monster will move to the next point based on its relative position to the next
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

            // if the monster is close enough to the next point, it will directly move to the next point
            } else {
                if ((int) Math.abs(x - currentPosition.pointNext.x) == 0) {
                    x = currentPosition.pointNext.x;
                } else if ((int) Math.abs(y - currentPosition.pointNext.y) == 0) {
                    y = currentPosition.pointNext.y;
                }
            }
        }
    }


    /**
     * Check whether the monster has reached the wizard house.
     * 
     * @return whether the monster has reached the wizard house
     */
    public boolean hasReachedWizardHouse() {
        return (currentPosition.pointNext == null);
    }


    /**
     * The monster will be banished when it reaches the wizard house and respawn at the starting position.
     */
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


    /**
     * The next point of the current point will be set.
     * 
     * @param point next point
     */
    public void setNextPoint(Point point) {
        pointNext = point;
    }
}


