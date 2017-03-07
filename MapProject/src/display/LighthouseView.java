package display;

import java.awt.Graphics;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import gameobjects.GameObject;
import lighthouse.LighthouseController;
import utility.Vector2D_Double;
import utility.Vector2D_Integer;

public class LighthouseView extends Camera {

	LighthouseController lhController;
	
	public LighthouseView() {
		super(0, 0, 14, 28, 2);
		
		lhController = new LighthouseController();
		try {
			lhController.connect();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(Graphics g, ArrayList<GameObject> gameObjects) {
		
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
			posOnScreen.setYComponent(posOnScreen.getYComponent() + (this.getHeight() - heightOnScreen));

			//g.fillRect(posOnScreen.getXComponent(), posOnScreen.getYComponent(), widthOnScreen, heightOnScreen);
		}
		
	}
}
