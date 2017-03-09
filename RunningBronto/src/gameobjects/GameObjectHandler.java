package gameobjects;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import core.ResourceHandler;
import input.Input;
import utility.Physics;

/**
 * Handles all objects that are currently somewhere in the game.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class GameObjectHandler {

	private Input input;
	private boolean currentJump;
	
	private Player player;
	private ArrayList<GameObject> gameObjects;
	private double xPos;

	// time between obstacle spawn
	private long spawnTime; // TODO think about randomizing
	private long lastSpawn;
	private long currentTime;

	/**
	 * Creates a new instances of GameObjectHandler.
	 * 
	 * @param width
	 *            width of the camera
	 * @param pixelPerMeter
	 *            how many picel are available to display 1 meter
	 */
	public GameObjectHandler(int width, int pixelPerMeter, Input input) {
		this(width, pixelPerMeter, 2.5, input);
	}

	/**
	 * Creates a new instances of GameObjectHandler.
	 * 
	 * @param width
	 *            width of the camera
	 * @param pixelPerMeter
	 *            how many picel are available to display 1 meter
	 * @param spawnTime
	 *            time between obstacle spawn
	 */
	public GameObjectHandler(int width, int pixelPerMeter, double spawnTime, Input input) {
		gameObjects = new ArrayList<GameObject>();
		// this.xPos = width/pixelPerMeter + 1; TODO
		this.xPos = 16;
		this.spawnTime = (long) (spawnTime * 1000);
		this.input = input;
	}

	/**
	 * Updates the state of the GameObjectHandler and all the objects controlled
	 * by it, like the player.
	 * 
	 * @param deltaTime
	 */
	public void update(long deltaTime) {
		double dTime = (double) deltaTime / 1000; // divide to get time in
													// seconds (meter per
													// seconds)

		// TODO main logic like collision detection may happen here

		// move all obstacles
		Iterator<GameObject> it;
		it = gameObjects.iterator();
		GameObject go;
		while (it.hasNext()) {
			go = it.next();
			if (go instanceof Obstacle) {
				go.setXPos(go.getXPos() + go.getXSpeed() * dTime);
			}
			go.updateBoundingBox();
		}
		
		if (input.isJumping() && !currentJump) {
			currentJump = true;
			getPlayer().setYSpeed(7.5);
			ResourceHandler.getSound("jump.wav").play(0.5);
		}
		if (input.isDucking()) {
			getPlayer().setHeight(1);
		} else if (!input.isDucking() && getPlayer().getHeight() == 1) {
			getPlayer().setHeight(1.7);
		}

		boolean collide = false;
		double newXPos = player.getXPos() + player.getXSpeed() * dTime;
		double newYPos = player.getYPos() + player.getYSpeed() * dTime;
		Rectangle2D.Double newBounding = new Rectangle2D.Double(newXPos, newYPos, getPlayer().getWidth(),
				getPlayer().getHeight());

		// check collision
//		for (int i = 0; i < gameObjects.size(); i++) {
//			if (!(gameObjects.get(i) instanceof Player)) {
//				if (gameObjects.get(i).getBoundingBox().intersects(newBounding)) {
//					System.out.println("Collision");
//					collide = true;
//					MapProject.setPaused(true);
//				}
//			}
//		}

		if (!collide) {
			// update player
			getPlayer().setXPos(newXPos);
			getPlayer().setYPos(newYPos);
			getPlayer().setYSpeed(player.getYSpeed() + (Physics.GRAVITY * dTime));
		}

		if (player.getYPos() < 0) {
			currentJump = false;
			player.setYSpeed(0);
			player.setYPos(0);
		}
		// update player end

		// spawn new obstacle
		currentTime = System.currentTimeMillis();
		if (currentTime - lastSpawn >= spawnTime) {
			spawnObstacle();
			lastSpawn = currentTime;
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public ArrayList<GameObject> getGameObjects() {
		return this.gameObjects;
	}

	public void spawnPlayer() {
		if (player == null) {
			player = new Player(2, 0, 0, 0, 0.5, 1.7);
			gameObjects.add(player);
		}
	}

	public void spawnObstacle() {
		Random r = new Random();
		int x = r.nextInt(2);
		switch (x) {
		case 0:
			gameObjects.add(new Tree(xPos, 0, -2, 0, 1, 1));
			break;
		case 1:
			gameObjects.add(new Ptera(xPos + 2, 1, -2, 0, 1, 1));
			this.xPos += 2;
			break;
		default:
			break;
		}
	}

	/**
	 * Needs to be called when the window and thus the camera are resized.
	 * Otherwise Obstacles would be spawned at the same position which is not
	 * what we want.
	 * 
	 * @param width
	 * @param pixelPerMeter
	 */
	public void resize(int width, int pixelPerMeter) {
		xPos = width / pixelPerMeter + 1;
	}
}
