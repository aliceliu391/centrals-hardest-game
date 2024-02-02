/*
* Player.java
* Eleora Jacob
* January 12, 2024
* Creates attributes for the player in the Surviving at Central: The Game!
*/
package culminating;
import java.awt.Rectangle;
public class Player extends Rectangle {
	
	// Attributes
	int v; //speed that player moves
	
	/**
	 * Constructor for player object
	 */
	Player() {
		v = 3;
		width = 40;
		height = 55;
	}
	
	/**
	 * Changes the speed of the character
	 * @param speed		The new speed for the character
	 */
	void setSpeed(int speed) {
		v = speed;
	}
	
	/**
	 * Changes the player to a smaller or larger size
	 * @param	The increase or decrease in height in pixels
	 */
	void changeSize(int newH) {
		height = newH;
		width = 8 * (newH/11);
	}
}
