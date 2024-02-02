/**
 * Npc.java
 * Defines NPCs that can be interacted with in MainGame.java
 * Alice Liu
 * January 21, 2024
 */
package culminating;
import java.awt.Rectangle;
public class Npc extends Rectangle {
	
	Npc(int x, int y) {
		width = 70;
		height = 50;
		this.x = x;
		this.y = y;
	}
}
