package display;

import java.awt.Color;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import gameobjects.GameObject;
import gameobjects.GameObjectHandler;
import gameobjects.Player;
import gameobjects.Ptera;
import gameobjects.Tree;
import lighthouse.CoordinatesOutOfBoundsException;
import lighthouse.LighthouseController;
import utility.Vector2D_Double;
import utility.Vector2D_Integer;

public class LighthouseView extends Camera {

	LighthouseController lhController;
	private boolean gameOverdrawn = false;
	
	public LighthouseView(String host, int port) {
		super(0, 0, 28, 14, 2);
		
		setVisibleMeters(((28/2) + 1));
		
		lhController = new LighthouseController(host, port);
		try {
			lhController.connect();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(GameObjectHandler goHandler) {
		
		ArrayList<GameObject> gameObjects = goHandler.getGameObjects();
		
		//limiter
		try {
			Thread.sleep(1000/30);
		} catch (InterruptedException e) {
			// do nothing
		}
		
		GameObject go; // copy of list it needed so we dont cause threading issues
		ArrayList<GameObject> imList;
		Iterator<GameObject> i;
		
		int widthOnScreen;
		int heightOnScreen;
		Vector2D_Integer posOnScreen;
		
		imList = new ArrayList<>(gameObjects);
		i = imList.iterator();
		while (i.hasNext()) {
			
			go = i.next();
			
			posOnScreen = worldToScreen(new Vector2D_Double(go.getXPos(), go.getYPos()));
			
			widthOnScreen = (int) (go.getWidth() * this.getPixelPerMeter());
			heightOnScreen = (int) (go.getHeight() * this.getPixelPerMeter());

			posOnScreen.setYComponent(posOnScreen.getYComponent() * -1);
			posOnScreen.setYComponent(posOnScreen.getYComponent() + (14 - heightOnScreen));
			
			if (go instanceof Player) {
				lhController.fillRectangle(posOnScreen.getXComponent(), posOnScreen.getYComponent(), widthOnScreen, heightOnScreen, Color.GREEN);
				
			} else if (go instanceof Tree) {
				lhController.fillRectangle(posOnScreen.getXComponent(), posOnScreen.getYComponent(), widthOnScreen, heightOnScreen, Color.BLUE);
				
			} else if(go instanceof Ptera) {
				lhController.fillRectangle(posOnScreen.getXComponent(), posOnScreen.getYComponent(), widthOnScreen, heightOnScreen, Color.RED);
			} else {
				lhController.fillRectangle(posOnScreen.getXComponent(), posOnScreen.getYComponent(), widthOnScreen, heightOnScreen, Color.RED);
			}
		}
		
		if (lhController.isConnected()) {
			if (!goHandler.isGameOver()) {
				lhController.pushFullImage();
			} else if(goHandler.isGameOver() && !gameOverdrawn) {
					Color color;
					int r = 0;
					int b = 0;
					int g = 0;
					Random rand = new Random();
					for (int j = 0; j < 28; j++) {
						for (int j2 = 0; j2 < 14; j2++) {
							r = rand.nextInt(255);
							g = rand.nextInt(255);
							b = rand.nextInt(255);
							color = new Color(r,g,b);
							try {
								lhController.setLighthousePixel(j, j2, color);
							} catch (CoordinatesOutOfBoundsException e) {
								e.printStackTrace();
							}
						}
					}
					lhController.pushFullImage();
					gameOverdrawn = true;
			}
		}
	}
	
	public void disconnect() throws IOException {
		lhController.disconnect();
	}
}
