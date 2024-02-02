/*
 * MainGame.java
 * Alice Liu, Teresa Mach, Eleora Jacob
 * An adventure game!
 * January 20, 2024
 */

//imports
package culminating;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import hsa2.GraphicsConsole;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MainGame implements ActionListener{
	public static void main (String[] args) {
		new MainGame();
	}
	
	//global variables
	int level = 1; //level of game
	double points = 0; //total number of points	
	int[] freePoints = {1, 2, 3, 4, 5, 6}; //list of numbers (to give random points)
	
	// Statistics to keep track of
	String[] statTitles = { "bad grade", "bouncing ghost", "final ghost", "level 1", "level 2", "level 3"}; // The parallel array to store the purpose of the stat
	int[] stats = {0, 0, 0, 0, 0, 0}; // The parallel array to store the corresponding number of the stat
	int gameDeaths = 0; // Keeps track of all deaths in the game
	int totalDeaths = 0; //deaths in a level
	Random rand = new Random(); //random object
	String name; //the user's name
	String avatar; //the user's chosen avatar
	int scrW = 1000; //screen width
	int scrH = 700;	//screen height
	
	//variables to keep track of time
	final static int timerSpeed = 100; // speed of the timer intervals
	double timeElapsed; // tracks the time elapsed
	Timer timer = new Timer(timerSpeed, this); // the actual timer object

	//colours
	static Color yellow = new Color(230, 237, 83);
	static Color purple = new Color(95, 59, 150);
	static Color clear = new Color(0, 0, 0, 0);
	static Color white = new Color(255, 255, 255, 150);
	
	//fonts
	static Font font = new Font("Calibri", Font.PLAIN, 50);
	static Font font2 = new Font("Calibri", Font.PLAIN, 20);
	static Font font3 = new Font("Calibri", Font.PLAIN, 30);
	static Font font4 = new Font("Calibri", Font.PLAIN, 15);
	
	//images
	BufferedImage grade, ghostOpenDownLeft, ghostClosedDownLeft, ghostOpenDown, ghostClosedDown, ghostOpenRight, bigGhost; //obstacle images
	BufferedImage background, gameInstructions, yesButton, noButton, doorImg, player1Side, player2Side, player1Front,
			player2Front, player1Back, player2Back, centralImg2, smileImg, sadEmoji, coolSmile, angryImg,
			ghostImg, catNpc, level1MapImg, level2MapImg, level3MapImg, pixelGhost, completionBackground,
			continueButton, quitButton, bigGhostMystery; // other images
	
	// Declare and/or initialize screens in the game
	GraphicsConsole gcIntro = null;
	GraphicsConsole gc = null;
	GraphicsConsole gcIntro2 = null;
	GraphicsConsole gc2 = null;
	GraphicsConsole gcIntro3 = null;
	GraphicsConsole gc3 = null;
	GraphicsConsole gcComplete = new GraphicsConsole(scrW, scrH, "Level" + level + "Completed!");
	GraphicsConsole gcEndGame = null;
	
	// NPC - Boost giver
	Boolean npcTriggered = false; //if the npc has already been touched
	
	//obstacles used for level 1, 2, and 3 (in order)
	ArrayList<Obstacle> level1 = new ArrayList<Obstacle>();
	ArrayList<AliveBall> ghosts = new ArrayList<AliveBall>();
	ArrayList<Obstacle> level3 = new ArrayList<Obstacle>();
	AliveBall b; //a AliveBall used in level 3
	Npc npc1, npc2, npc3; //NPCs, appear in all levels
	
	//the map for level 1, 2, and 3, in order (made of rectangles)
	ArrayList<Rectangle> level1Map = new ArrayList<Rectangle>();
	Rectangle m1 = new Rectangle(0, 625, 1000, 75);
	Rectangle m2 = new Rectangle(0, 0, 1000, 125);
	Rectangle m3 = new Rectangle(0, 0, 20, 700);
	Rectangle m4 = new Rectangle(180, 355, 110, 420);
	Rectangle m5 = new Rectangle(940, 425, 60, 300);
	Rectangle m6 = new Rectangle(940, 0, 60, 300);
	Rectangle m7 = new Rectangle(375, 0, 115, 500);
	ArrayList<Rectangle> level2Map = new ArrayList<Rectangle>();
	Rectangle level2m1 = new Rectangle(0, 640, 1000, 60);
	Rectangle level2m2 = new Rectangle(0, 0, 1000, 100);
	Rectangle level2m3 = new Rectangle(0, 0, 20, 700);
	Rectangle level2m4 = new Rectangle(950, 0, 50, 318);
	Rectangle level2m5 = new Rectangle(950, 398, 50, 290);
	Rectangle level2m6 = new Rectangle(105, 0, 125, 550);
	ArrayList<Rectangle> level3Map = new ArrayList<Rectangle>();
	Rectangle level3m1 = new Rectangle(0, 620, 1000, 800);
	Rectangle level3m2 = new Rectangle(0, 0, 1000, 120);
	Rectangle level3m3 = new Rectangle(0, 0, 30, 700);
	Rectangle level3m4 = new Rectangle(900, 0, 100, 318);
	Rectangle level3m5 = new Rectangle(900, 398, 100, 305);
	Rectangle level3m6 = new Rectangle(170, 190, 80, 120);
	Rectangle level3m7 = new Rectangle(170, 395, 80, 120);
	Rectangle level3m8 = new Rectangle(365, 0, 130, 345);
	Rectangle level3m9 = new Rectangle(350, 420, 140, 80);
	Rectangle level3m10 = new Rectangle(590, 230, 120, 330);
	
	//two buttons for the user to choose from in level completion screens
	Rectangle r1 = new Rectangle(scrW/2-150, 575, 90, 80);
	Rectangle r2 = new Rectangle(550, 575, 90, 80);
	Rectangle finish = new Rectangle(920, 315, 90, 80); //the door to reach for level 1 and 3
	Rectangle finish2 = new Rectangle(945, 315, 90, 80); //the door to reach for level 2
	Player p1; //the player's avatar
	
	//the direction that the player faces
	Boolean left = false;
	Boolean right = true;
	Boolean up = false;
	Boolean down = false;
	Obstacle o1, o2, o3, o4; //obstacles in level 1
	Obstacle o5, o6, o7; //obstacles in level 3
	
	MainGame() {
		// Make sure the complete screen is not visible
		gcComplete.setVisible(false);
		//setup introduction screen
		gcIntro = new GraphicsConsole(scrW, scrH, "Welcome!");
		introSetup();
		introGraphics();
		checkOk(gcIntro);
		gcIntro.dispose();
		//moving on to level 1
		gc = new GraphicsConsole(scrW, scrH, "Level 1"); //first level
		setup();
		gc.setFont(font2);

		//animation loop for level 1
		while(true) {
			if (gc.getKeyCode() == 'Q') System.exit(0); //if user wants to quit, exit program
			movePlayer(gc, level1Map);
			moveObstacles();
			drawLevel1Graphics();

			// checking for collision between player and obstacles
			for (Obstacle o: level1) {
				//if collision is detected, player is sent back to the start
				if (checkCollision(o, p1)) {
					p1.x = 100;
					p1.y = scrH/2;
					totalDeaths++;
					gameDeaths++;
					stats[0] += 1; // Add to deaths by bad grade
					stats[3] += 1; // Add to deaths in level 1
					npcTriggered = false; //reset NPC
					p1.setSpeed(3);	//reset speed
					p1.changeSize(55); //reset size
					gc.sleep(5);
				}
			}
			//if player touches npc, lower speed and size
			if (checkCollision(npc1, p1)) {
				p1.setSpeed(2);
				p1.changeSize(44);
				npcTriggered = true;
			}
			//if player reaches the finish, go to the next level
			if (checkCollision(finish, p1)) {
				timer.stop();
				break;
			}
			gc.sleep(10);
		}
		gc.dispose();
		// Show completed level screen
		gcComplete.setVisible(true);
		levelCompletion(gcComplete);
		checkOk(gcComplete);
		gcComplete.setVisible(false);
		//second introduction screen
		gcIntro2 = new GraphicsConsole(scrW, scrH, "Ready for level 2?"); //second introduction screen
		level2IntroSetup();
		checkOk(gcIntro2);
		gcIntro2.dispose();
		//second level setup
		gc2 = new GraphicsConsole(scrW, scrH, "Level 2");
		level2Setup();
		//level 2 animation loop
		while(true) {
			if (gc2.getKeyCode() == 'Q') System.exit(0); //if user wants to quit, exit program
			synchronized(gc2) {
				gc2.clear();
				movePlayer(gc2, level2Map);
				moveGhosts();
				//if player touches npc, add speed
				if (checkCollision(npc2, p1)) {
					p1.setSpeed(4);
					npcTriggered = true;
				}
				//if there is a collision with ghost, return player to start
				for (AliveBall ghost: ghosts) {
					if (checkCollision(ghost, p1)) {
						p1.x = 53;
						p1.y = (scrH / 2) - (p1.height / 2);
						totalDeaths++;
						gameDeaths++;
						stats[1] += 1; // Add to deaths by bouncing ghosts
						stats[4] += 1; // Add to deaths in level 2
						npcTriggered = false; //reset NPC
						p1.setSpeed(3);	//reset speed
						gc2.sleep(5);
					}
				}
				//if player reaches the finish, then stop timer and go to the next level
				if (checkCollision(finish2, p1)) {
					timer.stop();
					break;
				}

				drawLevel2Graphics();
			}
			gc2.sleep(10);
		}
		gc2.dispose();
		
		// Show completed level
		gcComplete.clear();
		gcComplete.setVisible(true);
		levelCompletion(gcComplete);
		checkOk(gcComplete);
		gcComplete.setVisible(false);
		
		//level 3 introduction screen
		gcIntro3 = new GraphicsConsole(scrW, scrH, "Ready for level 3?"); //third introduction screen
		level3IntroSetup();
		checkOk(gcIntro3);
		gcIntro3.dispose();
		
		//third level setup
		gc3 = new GraphicsConsole(scrW, scrH, "Level 3"); //level 3
		level3Setup();
		
		//animation loop for level 3
		while(true) {
			if (gc3.getKeyCode() == 'Q') System.exit(0); //if user wants to quit, exit program
			synchronized(gc3) {
				movePlayer(gc3, level3Map);
				moveObstacles3();
				moveGhosts(b);
				drawLevel3Graphics();
				
				// checking for collision between player and vertical moving obstacles
				for (Obstacle o: level3) {
					//if collision is detected, player is sent back to the start
					if (checkCollision(o, p1)) {
						p1.x = 53;
						p1.y = (scrH / 2) - (p1.height / 2);
						totalDeaths++;
						gameDeaths++;
						stats[0] += 1; // Add to deaths by bad grade
						stats[5] += 1; // Add to deaths in level 3
						//reset ghost position
						o5.x = 700;
						o5.y = 100;
						npcTriggered = false; //reset NPC
						p1.setSpeed(3);	//reset speed
						gc3.sleep(5);
					}
				}
				
				//if player touches npc, put speed higher
				if (checkCollision(npc3, p1)) {
					p1.setSpeed(4);
					npcTriggered = true;
				}
				
				//if collision is detected with big ghost, player and ghost sent back to initial positions
				if (checkCollision(o5, p1)) {
					totalDeaths++;
					gameDeaths++;
					stats[2] += 1; // Add to deaths by big ghost
					stats[5] += 1; // Add to deaths in level 3
					p1.x = 53;
					p1.y = (scrH / 2) - (p1.height / 2);
					o5.x = 700;
					o5.y = 100;
					npcTriggered = false; //reset NPC
					p1.setSpeed(3);	//reset speed
					gc3.sleep(5);
				}
				
				//if collision is detected with bouncing ball, player and ghost sent back to start
				if (checkCollision(b, p1)) {
					totalDeaths++;
					gameDeaths++;
					stats[1] += 1; // Add to deaths by bouncing ghosts
					stats[5] += 1; // Add to deaths in level 3
					p1.x = 53;
					p1.y = (scrH / 2) - (p1.height / 2);
					o5.x = 700;
					o5.y = 100;
					gc3.sleep(5);
				}
				
				//if player reaches the finish, then stop timer and go to the next level
				if (checkCollision(finish, p1)) {
					timer.stop();
					break;
				}
			}
			gc3.sleep(10);
		}
		gc3.dispose();
		
		// Show completed level
		gc.sleep(20);
		gcComplete.clear();
		gcComplete.setVisible(true);
		levelCompletion(gcComplete);
		checkOk(gcComplete);
		gcComplete.setVisible(false);
		
		//SAY BYE TO USER
		gcEndGame = new GraphicsConsole(scrW, scrH, "You've Finished!");
		gcEndGameSetup();
		gcEndGame.dispose();
	}
	/***************************/
	/////// INTRODUCTION ////////
	/***************************/
	/**
	 * setup for the intro screen
	 */
	
	void introSetup() {
		//standard setup
		gcIntro.setAntiAlias(true);
		gcIntro.setLocationRelativeTo(null);
		gcIntro.setBackgroundColor(purple); //purple color
		gcIntro.clear();
		gcIntro.enableMouse();
		gcIntro.enableMouseMotion();
		//loading in images
		try {
			background = ImageIO.read(new File ("background.jpg")); //background
			gameInstructions = ImageIO.read(new File ("gameInstructions.png")); //game instructions
			yesButton = ImageIO.read(new File ("yesButton.png")); //graphics for buttons
			noButton = ImageIO.read(new File ("noButton.png"));
			continueButton = ImageIO.read(new File ("continueButton.png"));
			quitButton = ImageIO.read(new File ("quitButton.png"));
			level1MapImg = ImageIO.read(new File ("level1MapImg.png")); //graphic for level 1 map
			pixelGhost = ImageIO.read(new File ("pixelGhost.png")); //decorative ghost
			doorImg = ImageIO.read(new File ("doorImg.png")); //door
			player1Side = ImageIO.read(new File ("player1Side.png")); //player avatar A
			player1Front = ImageIO.read(new File ("player1Front.png"));
			player1Back = ImageIO.read(new File ("player1Back.png"));
			player2Side = ImageIO.read(new File ("player2Side.png")); //player avatar B
			player2Front = ImageIO.read(new File ("player2Front.png"));
			player2Back = ImageIO.read(new File ("player2Back.png"));
			completionBackground = ImageIO.read(new File ("completionBackground.png")); //background for completion console
			smileImg = ImageIO.read(new File ("smileImg.png")); //emojis
			coolSmile = ImageIO.read(new File ("coolSmile.jpeg"));
			sadEmoji = ImageIO.read(new File ("sadEmoji.png"));
			angryImg = ImageIO.read(new File ("angryImg.png"));
			//obstacles
			grade = ImageIO.read(new File ("gradeImg.png"));
			bigGhost = ImageIO.read(new File ("bigGhost.png"));
			centralImg2 = ImageIO.read(new File ("centralImg2.jpeg"));
			//npcs
			catNpc = ImageIO.read(new File ("catNpc.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * displays the game instructions
	 */
	void introGraphics() {
		while(true) {
			name = gcIntro.showInputDialog("What is your name? ('Q' to quit)", "Hello, newest Central student!");
			if(name == null){ //if cancel is pressed, continue asking for a name
				gcIntro.sleep(30);
				continue;
			} else if (name.equals("")) { //empty name
				name = "'Failed Namer'";
				break;
			} else if (name.toUpperCase().trim().equals("Q")) { //if user wants to leave game, then quit program
				gcIntro.dispose();
				System.exit(0);
			}
			else break; //valid name is given
		}
		//lets user choose their avatar
		while(true) {
			avatar = gcIntro.showInputDialog("For Ms. Cempball, type 'A'. For Mr. Bellcamp, type 'B'. ('Q' to quit)", name + ", choose your avatar!");
			
			if(avatar == null){ //if cancel is pressed, continue asking for a name
				gcIntro.sleep(30);
				continue;
			} else if (avatar.toUpperCase().trim().equals("A")) { //avatar A chosen
				break;
			} else if (avatar.toUpperCase().trim().equals("B")) { //avatar B chosen
				break;
			} else if (avatar.toUpperCase().trim().equals("Q")) { //if user wants to leave game, then quit program
				gcIntro.dispose();
				System.exit(0);
			} else { //no valid option is give
				gcIntro.sleep(30);
				continue;
			}
		}
		gcIntro.sleep(50);
		//display game instructions
		gcIntro.drawImage(background, 0, 0, scrW, scrH);
		gcIntro.drawImage(pixelGhost, 700, 290, 200, 240);
		gcIntro.setColor(white);
		gcIntro.fillRect(50, 50, scrW-100, scrH-100);
		gcIntro.drawImage(gameInstructions, 0, 0, scrW, scrH);
		gcIntro.setColor(clear);
		gcIntro.drawRect(r1.x, r1.y-20, r1.width, r1.height); //draws the box for user confirmation
		gcIntro.drawRect(r2.x, r2.y-20, r2.width, r2.height); //draws the box for user to say no
		//decoration images
		gcIntro.drawImage(yesButton, r1.x, r1.y-20, r1.width, r1.height);
		gcIntro.drawImage(noButton, r2.x, r2.y-20, r2.width, r2.height);
		gcIntro.setTitle("Welcome, " + name + "!");
	}
	
	/**
	 * checks what button the user has clicked and gives the appropriate response
	 * @param gc the graphics console being used
	 */
	void checkOk(GraphicsConsole gc) {
		while (true) {
			if(gc.getMouseClick() > 0) {
				if (r1.contains(gc.getMousePosition())) return;
				else if (r2.contains(gc.getMousePosition())) {
					if (level == 1) gc.showDialog("You failed all your tests and dropped out of Central :( Better luck next time!", "Bye!"); //if the user clicks "no" on the first introduction screen
					else if (level == 2) gc.showDialog("Aw, it sucks that you're a quitter", "Have fun at home");
					gc.dispose();
				}
			}
			gc.sleep(10);
		}
	}
	
	/***************************/
	////////// LEVEL 1 //////////
	/***************************/
	
	/**
	 * sets up the level
	 */
	void setup() {
		timeElapsed = 0; //sets time elapsed to 0
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null);
		//initializing the player
		p1 = new Player();
		p1.x = 100;
		p1.y = 565;
		//the three obstacles in this level, adding them to an ArrayList
		o1 = new Obstacle(600, 115);
		o2 = new Obstacle(700, 550);
		o3 = new Obstacle(800, 350);
		o4 = new Obstacle(220, 115);
		level1.add(o1);
		level1.add(o2);
		level1.add(o3);
		level1.add(o4);
		//initializing attributes of the obstacles
		for (Obstacle l: level1) {
			l.vy = 5; //speed
			l.width = 70;
			l.height = 70;
		}
		//adding rectangles to make the map
		level1Map.add(m1);
		level1Map.add(m2);
		level1Map.add(m3);
		level1Map.add(m4);
		level1Map.add(m5);
		level1Map.add(m6);
		level1Map.add(m7);
		npc1 = new Npc(500, 125); //npc
		timer.start(); //starts timer
	}
	
	/**
	 * draws graphics for level 1
	 */
	void drawLevel1Graphics() {
		gc.setFont(font4);
		synchronized(gc) {
			gc.drawImage(level1MapImg, 0, 0, scrW, scrH);
			gc.drawImage(catNpc, npc1.x, npc1.y, npc1.width, npc1.height); //draw NPC
			drawInstructions1();
			drawTime(gc);
			drawPlayer(gc);
			drawOther();
			if (npcTriggered) {
				gc.drawString("Meow! >:D", npc1.x, npc1.y+80);
				gc.drawString("-1 speed", npc1.x, npc1.y+100);
				gc.drawString("You shrank!", npc1.x, npc1.y+120);
			}
		}
	}
	
	/**
	 * draws in-game instructions for level 1
	 */
	void drawInstructions1() {
		gc.setColor(white);
		gc.fillRect(50, 60, 680, 30);
		gc.setColor(Color.BLACK);
		gc.drawString("Avoid the obstacles.", 75, 210);
		gc.drawString("Get to the door.", 75, 240);
		gc.drawString("Use arrow keys to move.", 75, 270);
		gc.drawString("You're writing your physics test! Can you get a good grade and make it to the end of class?!", 60, 80);
	}

	/**
	 * draws time
	 * @param gc the graphics console being used
	 */
	void drawTime(GraphicsConsole gc) {
		gc.drawString("Time: " + Math.round((timeElapsed*1000.0))/1000.0, 30, 20);
	}
	
	/**
	 * allows player to move avatar with arrows
	 * @param gc the graphics console being used
	 * @param r ArrayList that holds all the map rectangles
	 */
	void movePlayer(GraphicsConsole gc, ArrayList<Rectangle> r) {
		if (gc.isKeyDown(37)) { //left
			if (!checkLeftSide(r)) p1.x -= p1.v;
			left = true;
			up = false;
			right = false;
			down = false;
		}
		if (gc.isKeyDown(38)) { //up
			if (!checkTopSide(r)) p1.y -= p1.v;
			up = true;
			left = false;
			right = false;
			down = false;
		}
		if (gc.isKeyDown(39)) { //right
			if (!checkRightSide(r)) p1.x += p1.v;
			right = true;
			left = false;
			up = false;
			down = false;
		}
		if (gc.isKeyDown(40)){ //down
			if (!checkBottomSide(r)) p1.y += p1.v;
			down = true;
			left = false;
			up = false;
			right = false;
		}
	}
	
	/**
	 * checks if the right side of the player will touch a wall
	 * @param r ArrayList that holds all the map rectangles
	 * @return returns true if there will be a collision with the wall, false if no
	 */
	boolean checkRightSide(ArrayList<Rectangle> r) {
		for (int i = 0; i < p1.height; i++) {
			if (checkForMapCollision(p1.x+p1.v+p1.width, p1.y+i, r)) return true;
		}
		return false;
	}
	
	/**
	 * checks if the left side of the player will touch a wall
	 * @param r ArrayList that holds all the map rectangles
	 * @return returns true if there will be a collision with the wall, false if no
	 */
	boolean checkLeftSide(ArrayList<Rectangle> r) {
		for (int i = 0; i < p1.height; i++) {
			if (checkForMapCollision(p1.x-p1.v, p1.y+i, r)) return true;
		}
		return false;
	}
	
	/**
	 * checks if the bottom side of the player will touch a wall
	 * @param r ArrayList that holds all the map rectangles
	 * @return returns true if there will be a collision with the wall, false if no
	 */
	boolean checkBottomSide(ArrayList<Rectangle> r) {
		for (int i = 0; i < p1.width; i++) {
			if (checkForMapCollision(p1.x+i, p1.y+p1.v+p1.height, r)) return true;
		}
		return false;
	}
	
	/**
	 * checks if the top side of the player will touch a wall
	 * @param r ArrayList that holds all the map rectangles
	 * @return returns true if there will be a collision with the wall, false if no
	 */
	boolean checkTopSide(ArrayList<Rectangle> r) {
		for (int i = 0; i < p1.width; i++) {
			if (checkForMapCollision(p1.x+i, p1.y-p1.v, r)) return true;
		}
		return false;
	}
	
	/**
	 * draws the player
	 * @param gc the graphics console being used
	 */
	void drawPlayer(GraphicsConsole gc) {
		gc.setColor(yellow);
		if (right == true) {
			if (avatar.toUpperCase().trim().equals("A")) gc.drawImage(player1Side, p1.x, p1.y, p1.width, p1.height); //draw avatar A right
			else if (avatar.toUpperCase().trim().equals("B")) gc.drawImage(player2Side, p1.x, p1.y, p1.width, p1.height); ////draw avatar B right
		} else if (left == true){
			if (avatar.toUpperCase().trim().equals("A")) gc.drawImage(player1Side, p1.x+p1.width, p1.y, -p1.width, p1.height); //draw avatar A left
			else if (avatar.toUpperCase().trim().equals("B")) gc.drawImage(player2Side, p1.x+p1.width, p1.y, -p1.width, p1.height); ////draw avatar B left
		} else if (up == true){
			if (avatar.toUpperCase().trim().equals("A")) gc.drawImage(player1Back, p1.x, p1.y, p1.width, p1.height); //draw avatar A up
			else if (avatar.toUpperCase().trim().equals("B")) gc.drawImage(player2Back, p1.x, p1.y, p1.width, p1.height); ////draw avatar B up
		} else if (down == true){
			if (avatar.toUpperCase().trim().equals("A")) gc.drawImage(player1Front, p1.x, p1.y, p1.width, p1.height); //draw avatar A down
			else if (avatar.toUpperCase().trim().equals("B")) gc.drawImage(player2Front, p1.x, p1.y, p1.width, p1.height); ////draw avatar B down
		}
	}
	
	/**
	 * moves the obstacles
	 */
	void moveObstacles() {
		//moving first three obstacles up and down
		for (int i = 0; i < 3; i++) {
			level1.get(i).moveVertically(105, 560);
		}
		//the 4th obstacle has a different min and max
		o4.moveVertically(50, 275);
	}
	
	/**
	 * draws obstacles and finish line
	 */
	void drawOther() {
		//draws obstacles as circles
		for (Obstacle l: level1) {
			gc.drawImage(grade, l.x, l.y, l.width, l.height);
		}
	}
	
	/**
	 * checks for a collision between player and rectangle
	 * @param r1 first rectangle
	 * @param r2 player's avatar
	 * @return returns true if there is a collision, false if there is not
	 */
	boolean checkCollision(Rectangle r1, Player r2) {
		if (r2.intersects(r1)) return true;
		else return false;
	}
	
	/**
	 * checks for a collision between two rectangles
	 * @param r1 first rectangle
	 * @param r2 second rectangle
	 * @return returns true if there is a collision, false if there is not
	 */
	boolean checkCollision(Rectangle r1, Rectangle r2) {
		if (r2.intersects(r1)) return true;
		else return false;
	}
	
	/**
	 * makes sure that the player cannot go through the walls of the level map
	 * @param x x value to be checked for collision
	 * @param y y value to be checked for collision
	 * @param list the ArrayList that contains all the rectangles that compose the map
	 * @return true if there is a collision, false if there is no collision
	 */
	boolean checkForMapCollision(int x, int y, ArrayList<Rectangle> list) {
		for (Rectangle r: list) {
			if (r.contains(x, y)) return true;
		}
		return false;
	}
	
	/**
	 * appears after a level is completed
	 * @param gc the graphics console being used
	 */
	void levelCompletion(GraphicsConsole gc) {
		gc.setTitle("Level " + level + " Completed!");
		int newPoints = 0; //points received in one level
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null);
		gc.clear();
		gc.enableMouse();
		gc.enableMouseMotion();
		
		//calculate points to give (based on completion time and level)
		if(level==1) {
			if (timeElapsed <= 15) newPoints += 100;
			else if (timeElapsed <= 25) newPoints += 75;
			else if (timeElapsed <= 30) newPoints += 50;
			else if (timeElapsed <= 35) newPoints += 25;
			else if (timeElapsed <= 40) newPoints += 5;
		}
		if(level==2) {
			if (timeElapsed <= 25) newPoints += 100;
			else if (timeElapsed <= 40) newPoints += 75;
			else if (timeElapsed <= 50) newPoints += 50;
			else if (timeElapsed <= 60) newPoints += 25;
			else if (timeElapsed <= 70) newPoints += 5;
		}
		if(level==3) {
			if (timeElapsed <= 55) newPoints += 100;
			else if (timeElapsed <= 70) newPoints += 75;
			else if (timeElapsed <= 75) newPoints += 50;
			else if (timeElapsed <= 80) newPoints += 25;
			else if (timeElapsed <= 90) newPoints += 5;
		}
		
		//adds newly received points to total points
		points += newPoints;
		
		//display message
		gc.drawImage(completionBackground, 0, 0, scrW, scrH);
		gc.setColor(white);
		gc.fillRect(50, 40, 900, 650);
		gc.setColor(Color.BLACK);
		gc.setFont(font);
		gc.drawString("You completed Level " + level + " !", 190, 100);
		gc.setFont(font2);
		gc.drawString("You took a total of " + Math.round((timeElapsed*1000.0))/1000.0 + " seconds, receiving " + newPoints + "/100 marks. ", 220, 150);
		if (totalDeaths == 0) gc.drawString("Incredible! You died 0 times. No marks will be deducted from your score!", 150, 200);
		else gc.drawString("You also died a total of " + totalDeaths + " times. 1 mark will be deducted for each death.", 140, 200);
		
		// Deduct points from deaths
		points -= totalDeaths;
		
		//draws image based on the points
		if (newPoints == 100) gc.drawImage(coolSmile, 75, 350, 150, 150);
		else if (newPoints == 75) gc.drawImage(smileImg, 75, 350, 150, 150);
		else if (newPoints == 50) gc.drawImage(sadEmoji, 75, 350, 150, 150);
		else gc.drawImage(angryImg, 75, 350, 130, 150); //0 points, failure!
		
		//get a random number, add it to total points
		int i = freePoints[rand.nextInt(freePoints.length-1) + 1];
		points += i;
		
		// Reveal point information
		gc.sleep(2000);
		gc.drawString("WOW!!! The Central ghost corrected your test and gave you " + i + " bonus marks!", 150, 250);
		gc.drawImage(centralImg2, 800, 350, 130, 150); //draws the central ghost
		gc.sleep(2000);
		gc.drawString("You now have a total of " + ((int) points * 100)/100 + " points. ", 325, 300);
		gc.drawString("What do you want to do next?", 350, 350);
		
		// Buttons
		gc.setColor(clear);
		gc.fillRect(r1.x, r1.y, r1.width, r1.height); //draws the box for user confirmation
		gc.fillRect(r2.x, r2.y, r2.width, r2.height); //draws the box for user to say no
		gc.drawImage(continueButton, r1.x, r1.y, r1.width, r1.height);
		gc.drawImage(quitButton, r2.x, r2.y, r2.width, r2.height);
	}
	
	/***************************/
	////////// LEVEL 2 //////////
	/***************************/
	
	/**
	 * Sets up the introduction screen for level 2
	 */
	void level2IntroSetup() {
		// Update the level
		level = 2;
		p1.changeSize(55); //reset size

		// Enable the mouse for the class
		gcIntro2.enableMouse();
		gcIntro2.enableMouseMotion();
		
		//Set the background
		gcIntro2.setBackgroundColor(purple);
		gcIntro2.setLocationRelativeTo(null);
		gcIntro2.clear();
		
		// The buttons
		gcIntro2.fillRect(r1.x, r1.y - 20, r1.width, r1.height); // Button
		gcIntro2.setFont(font2);
		gcIntro2.setColor(Color.white);
		gcIntro2.drawString("Start", r1.x + 24, r1.y + 25);	
		gcIntro2.setColor(Color.black);
		gcIntro2.fillRect(r2.x, r2.y - 20, r2.width, r2.height); // Button
		gcIntro2.setColor(Color.white);
		gcIntro2.drawString("Quit", r2.x + 26, r2.y + 25);
		
		// Introduce the user to the game
		gcIntro2.setColor(Color.white);
		gcIntro2.drawString("Welcome to the hallway, you have escaped the physics class.", (scrW/4) - 40, 50);
		gcIntro2.drawString("This does not come without consequence.", (scrW/4) + 50, 70);
		gcIntro2.drawString("The ghosts are out to get you!", (scrW/4) + 110, 90);
		gcIntro2.drawString("Move around so you do not get eaten by the ghosts.", 278-20, 110);
		gcIntro2.drawString("Reach the door to get refuge from the math classroom!", (scrW/4-20) + 14, 300);
		gcIntro2.drawRect(200, 20, 600, 500);

		// Draw the ghosts for decoration
		try {
			ghostOpenDown = ImageIO.read(new File ("ghostOpenDown.png")); // Open mouthed ghost facing down
			ghostClosedDown = ImageIO.read(new File ("ghostClosedDown.png")); // Close mouthed ghost facing down
			ghostOpenRight = ImageIO.read(new File ("ghostOpenRight.png")); // Open mouthed ghost to the right
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		gcIntro2.drawImage(ghostClosedDown, 298, 160); // first
		gcIntro2.drawImage(ghostOpenDown, 458, 160); // second
		gcIntro2.drawImage(ghostClosedDown, 638, 160); // third
		gcIntro2.drawImage(ghostOpenRight, 298, 380); // Below first ghost
		
		// Draw player and door
		p1.x = 460;
		p1.y = 380;
		p1.v = 3;
		drawPlayer(gcIntro2);
		gcIntro2.drawImage(doorImg, 600, 360, finish.width, finish.height);
	}
	
	/**
	 * Sets up the screen for level 2
	 */
	void level2Setup() {
		level = 2;
		// Ready graphics console
		gc2.setBackgroundColor(Color.BLACK);
		gc2.setLocationRelativeTo(null);
		gc2.clear();
		gc2.setVisible(true);
		npcTriggered = false;
		// Add images
		try {
			ghostOpenDownLeft = ImageIO.read(new File ("ghostOpenDownLeft.png")); // Open mouthed ghost
			ghostClosedDownLeft = ImageIO.read(new File ("ghostClosedDownLeft.png")); // Close mouthed ghost
			level2MapImg = ImageIO.read(new File ("level2MapImg.png")); //graphic for level 2 map
		} catch(IOException e) {
			e.printStackTrace();
		}
		// Reset variables
		timeElapsed = 0.0;
		p1.x = 53;
		p1.y = (scrH / 2) - (p1.height / 2);
		totalDeaths = 0;
		// Add obstacles
		ghosts.add(new AliveBall(400, 300, 8));
		ghosts.add(new AliveBall(600, 500, 5));
		ghosts.add(new AliveBall(700, 550, 3));
		// Setup the map
		level2Map.add(level2m1);
		level2Map.add(level2m2);
		level2Map.add(level2m3);
		level2Map.add(level2m4);
		level2Map.add(level2m5);
		level2Map.add(level2m6);
		//setup npc coordinates, add to ArrayList
		npc2 = new Npc(35, 130);
		// Start the time
		timer.start();
	}
	
	/**
	 * Draws all graphics for level 2, utilizes helper methods
	 */
	void drawLevel2Graphics() {
		gc2.drawImage(level2MapImg, 0, 0, scrW, scrH);
		drawInstructions2(); //draws instructions
		drawPlayer(gc2); //draws player
		gc2.drawImage(catNpc, npc2.x, npc2.y, npc2.width, npc2.height);
		drawGhosts(ghosts);
		gc2.setColor(Color.RED);
		drawTime(gc2);
		gc2.setColor(Color.BLACK);
		gc2.setFont(font4);
		if (npcTriggered) {
			gc2.drawString("Meow!", npc2.x-5, npc2.y-35);
			gc2.drawString("+1 speed!", npc2.x-6, npc2.y-15);
		}
	}
	
	/**
	 * draws ghosts
	 */
	void drawGhosts(ArrayList<AliveBall> g) {
		for (AliveBall ghost: g) {
			// Draw the image based on whether the player is close and the direction the ghost is traveling
			if (ghost.closeToPlayer(p1)) {
				if (ghost.vx > 0 && ghost.vy > 0) gc2.drawImage(ghostOpenDownLeft, ghost.x + ghost.height, ghost.y, -ghost.width, ghost.height); // Down right
				else if (ghost.vx < 0 && ghost.vy > 0) gc2.drawImage(ghostOpenDownLeft, ghost.x, ghost.y, ghost.width, ghost.height); // Down left
				else if (ghost.vx > 0 && ghost.vy < 0) gc2.drawImage(ghostOpenDownLeft, ghost.x + ghost.width, ghost.y + ghost.height, -ghost.width, -ghost.height); // Up right
				else gc2.drawImage(ghostOpenDownLeft, ghost.x, ghost.y + ghost.height, ghost.width, -ghost.height); // up left
			} else {
				if (ghost.vx > 0 && ghost.vy > 0) gc2.drawImage(ghostClosedDownLeft, ghost.x + ghost.height, ghost.y, -ghost.width, ghost.height); // Down right
				else if (ghost.vx < 0 && ghost.vy > 0) gc2.drawImage(ghostClosedDownLeft, ghost.x, ghost.y, ghost.width, ghost.height); // Down left
				else if (ghost.vx > 0 && ghost.vy < 0) gc2.drawImage(ghostClosedDownLeft, ghost.x + ghost.width, ghost.y + ghost.height, -ghost.width, -ghost.height); // Up right
				else gc2.drawImage(ghostClosedDownLeft, ghost.x, ghost.y + ghost.height, ghost.width, -ghost.height); // up left
			}
		} // End of for loop
	}
	
	/**
	 * draws a ghost
	 */
	void drawGhosts(AliveBall ghost) {
		// Draw the image based on whether the player is close and the direction the ghost is traveling
		if (ghost.closeToPlayer(p1)) {
			if (ghost.vx > 0 && ghost.vy > 0) gc3.drawImage(ghostOpenDownLeft, ghost.x + ghost.height, ghost.y, -ghost.width, ghost.height); // Down right
			else if (ghost.vx < 0 && ghost.vy > 0) gc3.drawImage(ghostOpenDownLeft, ghost.x, ghost.y, ghost.width, ghost.height); // Down left
			else if (ghost.vx > 0 && ghost.vy < 0) gc3.drawImage(ghostOpenDownLeft, ghost.x + ghost.width, ghost.y + ghost.height, -ghost.width, -ghost.height); // Up right
			else gc3.drawImage(ghostOpenDownLeft, ghost.x, ghost.y + ghost.height, ghost.width, -ghost.height); // up left
		} else {
			if (ghost.vx > 0 && ghost.vy > 0) gc3.drawImage(ghostClosedDownLeft, ghost.x + ghost.height, ghost.y, -ghost.width, ghost.height); // Down right
			else if (ghost.vx < 0 && ghost.vy > 0) gc3.drawImage(ghostClosedDownLeft, ghost.x, ghost.y, ghost.width, ghost.height); // Down left
			else if (ghost.vx > 0 && ghost.vy < 0) gc3.drawImage(ghostClosedDownLeft, ghost.x + ghost.width, ghost.y + ghost.height, -ghost.width, -ghost.height); // Up right
			else gc3.drawImage(ghostClosedDownLeft, ghost.x, ghost.y + ghost.height, ghost.width, -ghost.height); // up left
		}
	}
	
	/**
	 * draws in-game instructions for level 2
	 */
	void drawInstructions2() {
		gc2.setFont(font2);
		gc2.setColor(white);
		gc2.fillRect(60, 60, 920, 30);
		gc2.fillRect(60, 190, 220, 30);
		gc2.setColor(Color.BLACK);
		gc2.drawString("Avoid the obstacles.", 75, 210);
		gc2.drawString("Take refuge in the math classroom, quick! --->", 350, 350);
		gc2.drawString("Maybe you can find help there?", 400, 325);
		gc2.drawString("You successfully escaped physics class... but the hungry ghosts of Central are coming for you!", 70, 80);
	}
	
	/**
	 * Moves the ghosts
	 */
	void moveGhosts() {
		for (AliveBall g: ghosts) {
			// Check for collisions
			if (g.x <= 225 || g.x >= 910) {
				g.vx = -g.vx;
			}
			if (g.y <= 100 || g.y >= 580) {
				g.vy = -g.vy;
			}
			// Move the ghost
			g.x += g.vx;
			g.y += g.vy;
		}
	}
	
	/**
	 * Moves one ghost
	 */
	void moveGhosts(AliveBall g) {
		// Check for collisions
		if (g.x <= 690 || g.x >= 850) {
			g.vx = -g.vx;
		}
		if (g.y <= 150 || g.y >= 550) {
			g.vy = -g.vy;
		}
		// Move the ghost
		g.x += g.vx;
		g.y += g.vy;
	}
	
	/***************************/
	////////// LEVEL 3 //////////
	/***************************/
	
	/**
	 * Sets up the introduction screen for level 3
	 */
	void level3IntroSetup() {
		p1.setSpeed(3); //reset player speed
		npc3 = new Npc(170, 335); //new npc

		// Load images
		// Add images
		try {
			bigGhostMystery = ImageIO.read(new File ("bigGhostMystery.png")); // Open mouthed ghost
			level3MapImg = ImageIO.read(new File ("level3MapImg.png")); //graphic for level 3 map
		} catch(IOException e) {
			e.printStackTrace();
		}

		// Enable the mouse for the class
		gcIntro3.enableMouse();
		gcIntro3.enableMouseMotion();
		//Set the background
		gcIntro3.setBackgroundColor(purple);
		gcIntro3.setLocationRelativeTo(null);
		gcIntro3.clear();
		// The buttons
		gcIntro3.fillRect(r1.x, r1.y - 20, r1.width, r1.height); // Button
		gcIntro3.setFont(font2);
		gcIntro3.setColor(Color.white);
		gcIntro3.drawString("Start", r1.x + 24, r1.y + 25);	
		gcIntro3.setColor(Color.black);
		gcIntro3.fillRect(r2.x, r2.y - 20, r2.width, r2.height); // Button
		gcIntro3.setColor(Color.white);
		gcIntro3.drawString("Quit", r2.x + 26, r2.y + 25);
		// Introduce the user to the game
		gcIntro3.setColor(Color.white);
		gcIntro3.drawString("You made it to the math classroom... The haunted one", 220, 50);
		gcIntro3.drawString("This time, it's not just the ghosts and bad grades.", 230, 75);
		gcIntro3.drawString("THE central ghost is coming for you... Avoid his ghostly clutches.", 162, 100);
		gcIntro3.drawString("Beware, he can pass through walls.", 300, 125);
		gcIntro3.drawString("This is the hardest level yet. Many do not make it out alive.", 190, 150);
		gcIntro3.drawImage(bigGhostMystery, 473, 180);
		gcIntro3.drawImage(bigGhost, 400, 180);
		gcIntro3.drawImage(player1Front, 450, 320, 50, 70);
		gcIntro3.drawString("Goodluck, reach the door and make your final escape from Central!", 155, 305);
		gcIntro3.drawRect(150, 20, 650, 500);
	}
	
	/**
	 * setup for level 3
	 */
	void level3Setup() {
		// Ready graphics console
		gc3.setBackgroundColor(Color.BLACK);
		gc3.setLocationRelativeTo(null);
		gc3.clear();
		npcTriggered = false;
		
		//reset variables
		level = 3;
		timeElapsed = 0;
		left = false; //default position that player faces
		p1.x = 53;
		p1.y = (scrH / 2) - (p1.height / 2);
		totalDeaths = 0;
		
		//instantiating obstacles
		o5 = new Obstacle(750, 100); //the obstacle that follows the player around
		o6 = new Obstacle(260, 120); //obstacle that moves vertically
		o7 = new Obstacle(490, 500); //obstacle that moves vertically
		
		//obstacle that moves vertically
		level3.add(o6);
		level3.add(o7);
		
		//obstacle that bounces off walls
		b = new AliveBall(700, 400, 3);
		
		//give width and height
		for (Obstacle o: level3) {
			o.width = 70;
			o.height = 70;
			o.vy = 5;
		}
		
		//setting attributes for the obstacle that follows player
		o5.width = o5.height = 80;
		o5.vx = o5.vy = 1;
		
		//setup map
		level3Map.add(level3m1);
		level3Map.add(level3m2);
		level3Map.add(level3m3);
		level3Map.add(level3m4);
		level3Map.add(level3m5);
		level3Map.add(level3m6);
		level3Map.add(level3m7);
		level3Map.add(level3m8);
		level3Map.add(level3m9);
		level3Map.add(level3m10);
		
		timer.start(); //start timer
	}
	
	/**
	 * move the obstacles for level 3
	 */
	void moveObstacles3() {
		o5.followPlayer(p1.x, p1.y);
		moveGhosts(b);
		for (Obstacle o: level3) {
			o.moveVertically(120, 550);
		}
	}
	
	/**
	 * draws graphics for level 3
	 */
	void drawLevel3Graphics() {
		gc3.clear();

		gc3.drawImage(level3MapImg, 0, 0, scrW, scrH);

		//npc
		gc3.drawImage(catNpc, npc3.x, npc3.y, npc3.width, npc3.height);
		gc3.setFont(font4);
		gc3.setColor(Color.BLACK);
		if (npcTriggered) {
			gc3.drawString("Meowww :)", npc3.x+npc3.width+5, npc3.y+20);
			gc3.drawString("+1 speed", npc3.x+npc3.width+5, npc3.y+40);
		}

		drawInstructions3();
		drawPlayer(gc3); //draws player
		if (o5.closeToPlayer(p1)) gc3.drawImage(bigGhostMystery, o5.x, o5.y, o5.width, o5.height); //draw the black ghost
		else gc3.drawImage(bigGhost, o5.x, o5.y, o5.width, o5.height); //draw the white ghost
		drawGhosts(b); //draw the bouncing ghosts
		for (Obstacle o: level3) {
			gc3.drawImage(grade, o.x, o.y, o.width, o.height);
		}
		drawTime(gc3); //draws time
	}
	
	/**
	 * draws in-game instructions for level 3
	 */
	void drawInstructions3() {
		gc3.setColor(Color.WHITE);
		gc3.setFont(font2);
		gc3.drawString("You're almost there!", 710, 400);
		gc3.drawString("It won't be easy....", 710, 430);
	}
	
	/**
	 * Sets up and draws everything for the end of the game screen
	 */
	void gcEndGameSetup() {
		// Setup for the screen
		gcEndGame.setBackgroundColor(purple);
		gcEndGame.setLocationRelativeTo(null);
		gcEndGame.clear();
		gcEndGame.setFont(font2);
		gcEndGame.setColor(yellow);
		
		// Local variables
		String text = ""; // Holds the text to print in statements
		
		// Text
		gcEndGame.drawString("Looks like you've truly escaped Central.", 100, 100);
		gcEndGame.drawString("You did well, it didn't come without challenges.", 100, 130);
		gcEndGame.drawString("Let's take a look at how it went: ", 100, 160);
		gcEndGame.sleep(2000);
		
		// If they didn't die at all
		if (gameDeaths == 0) gcEndGame.drawString("You didn't die at all! ", 100, 190);
		
		// If they did die, display the statistics
		else {
			// Display the level they died most in
			if (stats[3] == stats[5]) {
				if (stats[3] > stats[4]) text = "level 1 and 3";
				else if (stats[3] == stats[4]) text = "each level"; // all
				else text = "level 2";
			}
			else if (stats[3] == stats[4]) {
				if (stats[3] > stats[5]) text = "level 1 and 2";
				else text = "level 3";
			}
			else if (stats[4] == stats[5]) {
				if (stats[4] > stats[3]) text = "level 2 and 3";
				else text = "level 1";
			}
			else {
				if (stats[4] > stats[3]) {
					if (stats[4] > stats[5]) text = "level 2";
					else text = "level 3";
				}
				else {
					if (stats[3] > stats[5]) text = "level 1";
					else text = "level 3";
				}
			}
			
			gcEndGame.drawString("You died the most in " + text, 100, 190);
			gcEndGame.sleep(2000);
			
			// Display what obstacle killed them most
			if (stats[0] == stats[2]) {
				if (stats[0] > stats[1]) text = "bad grades and the big ghost";
				else if (stats[0] == stats[1]) text = "bad grades, bouncing ghosts, and the big ghost"; // all
				else text = "bouncing ghosts";
			}
			else if (stats[0] == stats[1]) {
				if (stats[0] > stats[2]) text = "bad grades and bouncing ghosts";
				else text = "the big ghost";
			}
			else if (stats[1] == stats[2]) {
				if (stats[1] > stats[0]) text = "bouncing ghosts and the big ghost";
				else text = "bad grades";
			}
			else {
				if (stats[1] > stats[0]) {
					if (stats[1] > stats[2]) text = "bouncing ghosts";
					else text = "the big ghost";
				}
				else {
					if (stats[0] > stats[2]) text = "bad grades";
					else text = "the big ghost";
				}
			}
			
			gcEndGame.drawString("What killed you the most was " + text + "!", 100, 220);
			// Draw the corresponding image of the thing that killed them
			if (text.equals("bad grades")) {
				gcEndGame.drawImage(grade, 100, 240, 70, 70);
			} else if (text.equals("bouncing ghosts")) {
				gcEndGame.drawImage(ghostOpenRight, 100, 240);
			} else if (text.equals("the big ghost")) {
				gcEndGame.drawImage(bigGhost, 100, 240);
			} else if (text.equals("bad grades and the big ghost")) {
				gcEndGame.drawImage(grade, 100, 240, 70, 70);
				gcEndGame.drawImage(bigGhost, 200, 240);
			} else if (text.equals("bad grades and bouncing ghosts")) {
				gcEndGame.drawImage(grade, 100, 240, 70, 70);
				gcEndGame.drawImage(ghostOpenRight, 200, 240);
			} else if (text.equals("bouncing ghosts and the big ghost")) {
				gcEndGame.drawImage(ghostOpenRight, 100, 240);
				gcEndGame.drawImage(bigGhost, 200, 240);
			} else {
				gcEndGame.drawImage(grade, 100, 240, 70, 70);
				gcEndGame.drawImage(ghostOpenRight, 200, 240);
				gcEndGame.drawImage(bigGhost, 300, 240);
			}
			gcEndGame.sleep(2000);
		} // end of else
		
		// Let the user know how many points they got
		gcEndGame.drawString("You had " + ((int) points * 100)/100 + "/300 points, which is a " + (int)((points / 300) * 100) + "%.", 100, 340);
		gcEndGame.sleep(2000);
		
		// Divide the points to find the percentage
		points /= 300;
		
		// Determine the corresponding grade
		if (points >= 0.95) text = "A+"; // As
		else if (points >= 0.87) text = "A";
		else if (points >= 0.80) text = "A-";
		else if (points >= 0.77) text = "B+"; // Bs
		else if (points >= 0.73) text = "B";
		else if (points >= 0.70) text = "B-";
		else if (points >= 0.67) text = "C+"; // Cs
		else if (points >= 0.63) text = "C";
		else if (points >= 0.60) text = "C-";
		else if (points >= 0.57) text = "D+"; // Ds
		else if (points >= 0.53) text = "D";
		else if (points >= 0.50) text = "D-";
		else text = "A FAIL! Please study next time!";
		
		// Display their final grade
		gcEndGame.drawString("Your final grade is... " + text, 100, 370);
		
		// Print images of the character for decoration
		for (int images = 0; images < 11; images++) {
			if (images % 4 == 0) {
				if (avatar.toUpperCase().equals("A")) gcEndGame.drawImage(player1Side, 70 + (images * 90), 500, -p1.width, p1.height); //draw avatar A left
				else gcEndGame.drawImage(player2Side, 70 + (images * 90), 500, -p1.width, p1.height); ////draw avatar B left
			} else if (images % 4 == 1) {
				if (avatar.toUpperCase().equals("A")) gcEndGame.drawImage(player1Back, 25 + (images * 90), 485, p1.width, p1.height); //draw avatar A up
				else gcEndGame.drawImage(player2Back, 25 + (images * 90), 485, p1.width, p1.height); ////draw avatar B up
			} else if (images % 4 == 2) {
				if (avatar.toUpperCase().equals("A")) gcEndGame.drawImage(player1Side, 25 + (images * 90), 500, p1.width, p1.height); //draw avatar A right
				else gcEndGame.drawImage(player2Side, 25 + (images * 90), 500, p1.width, p1.height); ////draw avatar B right
			} else {
				if (avatar.toUpperCase().equals("A")) gcEndGame.drawImage(player1Front, 25 + (images * 90), 500, p1.width, p1.height); //draw avatar A down
				else gcEndGame.drawImage(player2Front, 25 + (images * 90), 500, p1.width, p1.height); ////draw avatar B down
			}
		}
		
		// Say bye to the user
		gcEndGame.sleep(5000);
		gcEndGame.showDialog("Thanks for playing! The ghosts of Central will miss you.", "Goodbye, " + name);
		System.exit(0);
	}
	
	/**
	 * counts time
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		timeElapsed += 0.1;
	}
}