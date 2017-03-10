package display;

import utility.Vector2D_Double;
import utility.Vector2D_Integer;

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
public abstract class Camera {

	// position in game world
	private double xPos;
	private double yPos;

	// width and height in pixel
	private int width;
	private int height;

	// scaling factor, a meter in game equals pixelPerMeter pixels
	// on screen
	private int pixelPerMeter;
	private int visibleMeters;

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
	
	/**
	 * Calculates where an object would be on the screen given its in game coordinate(s)
	 * @param x
	 * @return
	 */
	protected Vector2D_Integer worldToScreen(Vector2D_Double v) {
		int xComp = (int) ((v.getXComponent() - this.getXPos()) * pixelPerMeter);
		int yComp = (int) ((v.getYComponent() - this.getYPos()) * pixelPerMeter);
		return new Vector2D_Integer(xComp, yComp);
	}
	
	/**
	 * like world to screen but only for x coordinate.
	 * useful in some situations.
	 * @param x
	 * @return
	 */
	protected int xToScreen(Double x) {
		int xComp = (int) ((x - this.getXPos()) * pixelPerMeter);
		return xComp;
	}
	
	/**
	 * like world to screen but only for x coordinate.
	 * useful in some situations.
	 * @param y
	 * @return
	 */
	protected int yToScreen(Double y) {
		int yComp = (int) ((y - this.getYPos()) * pixelPerMeter);
		return yComp;
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

	public void setPixelPerMeter(int pixelPerMeter) {
		this.pixelPerMeter = pixelPerMeter;
	}
	
	public int getPixelPerMeter() {
		return pixelPerMeter;
	}

	/**
	 * See getVisibleMeters().
	 * @param x
	 */
	public void setVisibleMeters(int x) {
		this.visibleMeters = x;
	}
	
	/**
	 * Returns how many meters (ingame) the camera can see.
	 * Useful if different views are active that show different areas of the world
	 * so obstacles are alway spawned out of sight.
	 * @return
	 */
	public int getVisibleMeters() {
		return this.visibleMeters;
	}

}
