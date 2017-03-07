package display;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import gameobjects.GameObject;
import utility.Vector2D_Double;
import utility.Vector2D_Integer;

public class HighResView extends Camera {

	public HighResView(double xPos, double yPos, int width, int height, int pixelPerMeter) {
		super(xPos, yPos, width, height, pixelPerMeter);
	}

	// if the grid should be drawn
	private boolean drawGrid = true;

	@Override
	public void render(Graphics g, ArrayList<GameObject> gameObjects) {
		
		double xPosInGame;
		int xPosPixel;
		double yPosInGame;
		int yPosPixel;
		int lineCount;
		
		GameObject go; // copy of list it needed so we dont cause threading issues
		ArrayList<GameObject> imList;
		Iterator<GameObject> i;
		
		int widthOnScreen;
		int heightOnScreen;
		Vector2D_Integer posOnScreen;
		
		// render map grid
		if (drawGrid) {
			
			// position of line in game world
			xPosInGame = ((int) this.getXPos());
			yPosInGame = ((int) this.getYPos());
			
			posOnScreen = worldToScreen(new Vector2D_Double(xPosInGame, yPosInGame));
			
			// how many lines need to be drawn
			lineCount = this.getWidth() / this.getPixelPerMeter();
			
			// where on the screen the line will be drawn
			// x component only because y is fixed (vertical)
			do { // lines vertically
				xPosPixel = xToScreen(xPosInGame);
				g.drawLine(xPosPixel, this.getHeight(), xPosPixel, 0);
				g.drawString("" + xPosInGame, xPosPixel + 5, 50);
				xPosInGame += 1;
			} while (xPosInGame - this.getXPos() <= lineCount); // off set by this.xPos so we actually draw the lines the camera sees


			// same principle as above
			lineCount = this.getHeight() / this.getPixelPerMeter();
			do { // lines vertically
				yPosPixel = yToScreen(yPosInGame);
				yPosPixel *= -1; // needed or lines will be drawn out of sight
				yPosPixel += this.getHeight(); // offset to make up for coordinates starting in top left corner
				g.drawLine(0, yPosPixel, this.getWidth(), yPosPixel);
				g.drawString("" + yPosInGame, 5, yPosPixel - 5);
				yPosInGame += 1;
			} while (yPosInGame - this.getYPos() <= lineCount);
		}

		imList = new ArrayList<>(gameObjects);
		i = imList.iterator();
		while (i.hasNext()) {
			
			go = i.next();

			posOnScreen = worldToScreen(new Vector2D_Double(go.getXPos(), go.getYPos()));
			
			widthOnScreen = (int) (go.getWidth() * this.getPixelPerMeter());
			heightOnScreen = (int) (go.getHeight() * this.getPixelPerMeter());

			posOnScreen.setYComponent(posOnScreen.getYComponent() * -1);
			posOnScreen.setYComponent(posOnScreen.getYComponent() + (this.getHeight() - heightOnScreen));

			g.fillRect(posOnScreen.getXComponent(), posOnScreen.getYComponent(), widthOnScreen, heightOnScreen);
		}
	}

	/**
	 * Enables/Disables drawing of the coordinate grid
	 * @param drawGrid
	 */
	public void drawGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
	}
	
}
