package display;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import actionlogger.Action;
import actionlogger.ActionLogger;
import core.Game;
import core.ResourceHandler;
import game.MapProject;
import gameobjects.GameObject;
import gameobjects.Player;
import utility.Vector2D_Double;
import utility.Vector2D_Integer;

public class HighResView extends Camera {

	public HighResView(double xPos, double yPos, int width, int height, int pixelPerMeter) {
		super(xPos, yPos, width, height, pixelPerMeter);
	}

	// if the grid should be drawn
	private boolean drawGrid = true;

	public void render(Game parent, Graphics g, ArrayList<GameObject> gameObjects) {
		
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

			if (go instanceof Player) {
				g.drawImage(ResourceHandler.getTransparentImage("bronto.png"), posOnScreen.getXComponent(), posOnScreen.getYComponent(), widthOnScreen, heightOnScreen, null);
			} else {
				g.drawImage(ResourceHandler.getTransparentImage("tree.png"), posOnScreen.getXComponent(), posOnScreen.getYComponent(), widthOnScreen, heightOnScreen, null);
			}
		}
		
		//TODO refactor
		// draw fps counter draw basic information
		g.drawString("FPS: " + parent.getFPS() + " width: " + this.getWidth() + " height: " + this.getHeight(), 10, 15);
		
		// draw paused info
		if (MapProject.isPaused()) {
			g.setColor(Color.RED);
			g.drawString("Game is paused!", 300, 15);
			g.setColor(Color.BLACK);
		}
		
		// draw camera information
		g.drawString("CamX: " + getXPos() + " CamY: " + getYPos(), 10, 15 + g.getFontMetrics().getHeight());
		
		// draw content of action logger
		LinkedList<Action> actions; actions = ActionLogger.getActions();
		if (actions.size() > 0) {
			g.setColor(ActionLogger.getBackgroundColor());
			g.fillRect(this.getWidth() - 205, 0, 205, actions.size() * g.getFontMetrics().getHeight() + 5);
			g.setColor(Color.BLACK);
			for (int i1 = 0; i1 < actions.size(); i1++) {
				g.setColor(actions.get(i1).getColor());
				g.drawString(actions.get(i1).getAction(), this.getWidth() - 200, i1 * g.getFontMetrics().getHeight() + g.getFontMetrics().getHeight());
			}
			ActionLogger.update();
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
