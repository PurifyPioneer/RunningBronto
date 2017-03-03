package game;

import gameobjects.GameObjectHandler;

/**
 * Mostly useless at the moment.. maybe used later
 * for tile based systems..
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class Map {

	private GameObjectHandler goHandler;
	
	public Map(int width, int pixelPerMeter) {
		goHandler = new GameObjectHandler(width, pixelPerMeter);	
	}

	public GameObjectHandler getGameObjectHandler() {
		return this.goHandler;
	}
	
	public void update(long deltaTime) {
		goHandler.update(deltaTime);
	}
	
	public void resize(int width, int pixelPerMeter) {
		goHandler.resize(width, pixelPerMeter);
	}

	public void spawnPlayer() {
		goHandler.spawnPlayer();		
	}

	public void spawnObstacle() {
		goHandler.spawnObstacle();
	}
	
}
