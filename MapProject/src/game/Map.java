package game;

import gameobjects.GameObjectHandler;

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
