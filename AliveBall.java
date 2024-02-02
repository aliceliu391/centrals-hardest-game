/*
* AliveBall.java
* January 17, 2024
* Eleora Jacob, Alice Liu
* Class that creates alive balls, in our case, bouncing ghosts, for the game
*/
package culminating;
import java.awt.Rectangle;
public class AliveBall extends Rectangle{	
	// Attributes
	int vx, vy;
	Rectangle closeToPlayer = new Rectangle(x - 50, y - 40, 140, 140); //rectangle that checks if an AliveBall is close to a player
	
	/**
	 * Constructor
	 * @param ballX		The x-coordinate for the object
	 * @param ballY		The y-coordinate for the object
	 * @param ballSpeed		The desired speed of the ball
	 */
	AliveBall(int ballX, int ballY, int ballSpeed) {
		x = ballX;
		y = ballY;
		vx = vy = ballSpeed;
		width = height = 60;
	}
	
	/**
	 * Checks to see if ball is close to player
	 * @param player	The player object so that it can be checked for intersection
	 * @return		Returns whether the player is close to the object or not using Boolean
	 */
	Boolean closeToPlayer(Player player) {
		closeToPlayer.x = x-50;
		closeToPlayer.y = y-50;
		if (closeToPlayer.intersects(player)) {
			return true;
		}
		else return false;
	}
	
}
