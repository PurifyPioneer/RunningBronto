package display;

import java.awt.Color;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import gameobjects.GameObject;
import gameobjects.Player;
import lighthouse.LighthouseController;
import utility.Vector2D_Double;
import utility.Vector2D_Integer;

public class LighthouseView extends Camera {

	LighthouseController lhController;
	
	public LighthouseView() {
		super(0, 0, 28, 14, 2);
		
		lhController = new LighthouseController("localhost", 8000);
		try {
			lhController.connect();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(ArrayList<GameObject> gameObjects) {
		
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
				
			} else {
				lhController.fillRectangle(posOnScreen.getXComponent(), posOnScreen.getYComponent(), widthOnScreen, heightOnScreen, Color.RED);
				
			}
		}
		
		if (lhController.isConnected()) {
			lhController.pushFullImage();
			System.out.println("SENDING");
		}
	}
	
	public void disconnect() throws IOException {
		lhController.disconnect();
	}
}
