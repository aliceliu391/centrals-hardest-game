/*
 * Obstacle.java
 * Alice Liu
 * January 13, 2024
 * The obstacle class. They are circles that go in different directions- up and down, diagonally, or follow the player;
 */

package culminating;
import java.awt.Rectangle;


public class Obstacle extends Rectangle{	
	//attributes
	int vy, vx; //the speed that the obstacle goes, up and down or left and right
	Rectangle closeToPlayer = new Rectangle(x - 50, y - 40, 195, 195); //rectangle that checks if an obstacle is close to a player
	
	Obstacle(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * moves the obstacle vertically
	 * @param min minimum y value
	 * @param max max y value
	 */
	void moveVertically(int min, int max) {
		y += vy;
		if (y < min || y > max) {
			vy = -vy;
		}
	}
	
	/**
	 * obstacle follows the player
	 * @param px the x coordinate of the player
	 * @param py the y coordinate of the player
	 */
	void followPlayer(int px, int py) {
		if (px > x && x < 1000) { //if the player is to the right, move to the right
			x += vx;
		} else if (px < x && x > 0) { //if the player is to the left, move to the left
			x -= vx;
		}
		if (py > y && y < 650) { //if the player is below, move lower
			y += vy;
		} else if (py < y && y > 0) { //if the player is above, move higher
			y -= vy;
		}
	}
	
	/**
	 * Checks to see if ball is close to player
	 * @param player	The player object so that it can be checked for intersection
	 * @return		Returns whether the player is close to the object or not using Boolean
	 */
	Boolean closeToPlayer(Player player) {
		closeToPlayer.x = x-60;
		closeToPlayer.y = y-60;
		if (closeToPlayer.intersects(player)) {
			return true;
		}
		else return false;
	}

}
