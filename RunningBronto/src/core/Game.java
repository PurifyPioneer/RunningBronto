package core;

import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

/**
 * Represents a basic Game that makes sure that functionalities
 * like input are available.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("serial")
public abstract class Game extends JPanel implements Runnable, MouseListener, MouseWheelListener, ComponentListener {

	private Thread thread;
	private String title;

	private long lastTime;
	private int fps;
	private int fpsCounter;
	
	/**
	 * Creates a new Game and takes care
	 * that listeners are available.
	 */
	protected Game() {
		title = "";
		this.setFocusable(true);
		this.addMouseWheelListener(this);
		this.addMouseListener(this);
		this.addComponentListener(this);
	}
	
	/**
	 * Returns the title of the game
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	

	/**
	 * Changes the games title
	 * @param title
	 */
	protected void setTitle(String title) {
		this.title = title;
	}

	public Thread getThread() {
		return thread;
	}


	protected void setThread(Thread thread) {
		this.thread = thread;
	}
	
	/**
	 * Will calculate how much fps the game is running at.
	 * (How often the frame is repainted).
	 * TODO Find away to calculate fps in general purposes
	 * @param currentTime
	 */
	public void updateFPS(long currentTime) {
		
		if (currentTime - lastTime>= 1000) {
			fps = fpsCounter;
			fpsCounter = 0;
			lastTime = currentTime;
		}
		fpsCounter++;
	}
	
	/**
	 * Will return the fps the game is running at.
	 * You will need to call updateFps method to get
	 * up to date values.
	 * @return fps
	 */
	public long getFPS() {
		return fps;
	}
	
	/**
	 * Creates a new thread and executes the code in the run method.
	 */
	protected void startGame() {
		setThread(new Thread(this, getTitle()));
		getThread().start();
	}
}
