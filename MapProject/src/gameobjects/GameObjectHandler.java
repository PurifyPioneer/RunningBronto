package gameobjects;

import java.util.ArrayList;
import java.util.Iterator;

import game.MapProject;

/**
 * Handles all objects that are currently somewhere in the game.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class GameObjectHandler {

	private Player player;
	private ArrayList<GameObject> gameObjects;
	private double xPos;
	
	// time between obstacle spawn
	private long spawnTime; // TODO think about randomizing
	private long lastSpawn;
	private long currentTime;
	
	/**
	 * Creates a new instances of GameObjectHandler.
	 * @param width width of the camera
	 * @param pixelPerMeter how many picel are available to display 1 meter
	 */
	public GameObjectHandler(int width, int pixelPerMeter) {
		this(width, pixelPerMeter, 2.5);
	}
	
	/**
	 * Creates a new instances of GameObjectHandler.
	 * @param width width of the camera
	 * @param pixelPerMeter how many picel are available to display 1 meter
	 * @param spawnTime time between obstacle spawn
	 */
	public GameObjectHandler(int width, int pixelPerMeter, double spawnTime) {
		gameObjects = new ArrayList<GameObject>();
		this.xPos = width/pixelPerMeter + 1;
		this.spawnTime = (long) (spawnTime*1000);
	}
	
	/**
	 * Updates the state of the GameObjectHandler
	 * and all the objects controlled by it,
	 * like the player.
	 * @param deltaTime 
	 */
	public void update(long deltaTime) {
		double dTime = (double) deltaTime/1000; // divide to get time in seconds (meter per seconds)
		
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
		
		// update player
		getPlayer().setXPos(player.getXPos() + player.getXSpeed() * dTime);	
		getPlayer().setYPos(player.getYPos() + player.getYSpeed() * dTime);
		getPlayer().setYSpeed(player.getYSpeed() + (MapProject.GRAVITY * dTime));
		
		if (player.getYPos() < 0) {
			player.setYSpeed(0);
			player.setYPos(0);
		}
		// update player end
		
		// check collision
		for (int i = 0; i < gameObjects.size(); i++) {
			if (!(gameObjects.get(i) instanceof Player)) {
				if (gameObjects.get(i).getBoundingBox().intersects(player.getBoundingBox())) {
					System.out.println("Collision");
					MapProject.setPaused(true);
				}
			}
		}
		
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
		gameObjects.add(new Obstacle(xPos, 0, -2, 0, 1, 1));
	}

	/**
	 * Needs to be called when the window and thus the camera are
	 * resized.
	 * Otherwise Obstacles would be spawned at the same position which is not what we
	 * want.
	 * @param width
	 * @param pixelPerMeter
	 */
	public void resize(int width, int pixelPerMeter) {
		xPos = width/pixelPerMeter +1;
	}
}
