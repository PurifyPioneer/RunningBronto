package game;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import gameobjects.GameObject;

/**
 * Represents a Camera that has its own position in a two
 * dimensional game world and renders everything to the screen
 * that is in the current view of the camera depending on its
 * width, height and scaling.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class Camera {

	// position in game world
	private double xPos;
	private double yPos;

	// width and height in pixel
	private int width;
	private int height;

	// scaling factor, a meter in game equals pixelPerMeter pixels
	// on screen
	private int pixelPerMeter;

	// if the grid should be drawn
	private boolean drawGrid = true;

	/**
	 * Creates a new camera with all the values it needs to know to function.
	 * @param xPos x position in game world
	 * @param yPos y position in game world
	 * @param width width of the frame
	 * @param height height of the frame
	 * @param pixelPerMeter scaling factor
	 */
	public Camera(double xPos, double yPos, int width, int height, int pixelPerMeter) {
		this.xPos = xPos;
		this.yPos = yPos;

		this.width = width;
		this.height = height;

		this.pixelPerMeter = pixelPerMeter;
	}

	/**
	 * Notifies the camera when the size of the current view has changed
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/* HINT: not active atm
	public void moveRight() {
		this.xPos += .1;
	}

	public void moveleft() {
		this.xPos -= .1;
	}

	public void moveUp() {
		this.yPos += .1;
	}

	public void moveDown() {
		this.yPos -= .1;
	}
	*/
	
	/**
	 * Enables/Disables drawing of the coordinate grid
	 * @param drawGrid
	 */
	public void drawGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
	}

	/**
	 * Renders all objects to the screen, that are within the current view.
	 */
	public void render(Graphics g, ArrayList<GameObject> gameObjects) {
		
		// render map grid
		if (drawGrid) {
			
			// position of line in game world
			double xPosInGame = ((int) this.xPos);
			
			// how many lines need to be drawn
			double lineCount = this.getWidth() / pixelPerMeter;
			
			// where on the screen the line will be drawn
			// x component only because why is fixed (vertical)
			int xPosPixel;
			do { // lines vertically
				xPosPixel = toScreenCoordinateX(xPosInGame);
				g.drawLine(xPosPixel, this.height, xPosPixel, 0);
				g.drawString("" + xPosInGame, xPosPixel + 5, 50);
				xPosInGame += 1;
			} while (xPosInGame - this.xPos <= lineCount); // off set by this.xPos so we actually draw the lines the camera sees


			// same principle as above
			double yPosInGame = ((int) this.yPos);
			lineCount = this.getHeight() / pixelPerMeter;
			int yPosPixel;
			do { // lines vertically
				yPosPixel = (int) (((yPosInGame) - this.getYPos()) * pixelPerMeter);
				yPosPixel *= -1; // needed or lines will be drawn out of sight
				yPosPixel += this.getHeight(); // offset to make up for coordinates starting in top left corner
				g.drawLine(0, yPosPixel, this.width, yPosPixel);
				g.drawString("" + yPosInGame, 5, yPosPixel - 5);
				yPosInGame += 1;
			} while (yPosInGame -this.yPos <= lineCount);
		}


		GameObject go; // copy of list it needed so we dont cause threading issues
		ArrayList<GameObject> imList = new ArrayList<>(gameObjects);
		Iterator<GameObject> i = imList.iterator();
		while (i.hasNext()) {
			
			go = i.next();

			int xPosOnScreen = toScreenCoordinateX(go.getXPos()); // TODO better encapsulation of
			int yPosOnScreen = toScreenCoordinateX(go.getYPos()); // transformation method
			
			int widthOnScreen = (int) (go.getWidth() * pixelPerMeter);
			int heightOnScreen = (int) (go.getHeight() * pixelPerMeter);

			yPosOnScreen *= -1;
			yPosOnScreen += (this.height - heightOnScreen);

			g.fillRect(xPosOnScreen, yPosOnScreen, widthOnScreen, heightOnScreen);
			g.drawString("X: " + go.getXPos(), xPosOnScreen + widthOnScreen + 5, yPosOnScreen);
			g.drawString("Y: " + go.getYPos(), xPosOnScreen + widthOnScreen + 5, yPosOnScreen + g.getFontMetrics().getHeight());

		}
	}
	
	/**
	 * Calculates where an object would be on the screen given its in game coordinate(s)
	 * @param x
	 * @return
	 */
	private int toScreenCoordinateX(double x) {
		return (int) ((x - this.getXPos()) * pixelPerMeter);
	}

	public double getXPos() {
		return xPos;
	}

	public double getYPos() {
		return yPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getPixelPerMeter() {
		return pixelPerMeter;
	}

}
