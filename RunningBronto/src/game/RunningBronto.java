package game;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import actionlogger.ActionLogger;
import core.DisplayFrame;
import core.Game;
import display.HighResView;
import display.LighthouseView;
import gameobjects.GameObjectHandler;
import input.KeyInput;
import kuusisto.tinysound.TinySound;

/**
 * Main class of project.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class RunningBronto extends Game {

	// Structure and thoughts
	//
	// 1. mvc-pattern:
	// - model: every objects on the screen is a gameobject (with properties
	// like
	// x, y width, height)
	//
	// - view: the view is handled by a central views. One got high resolutions
	// for the PC and one just pixels for the lighthouse. We didn't used
	// separated views for every gameObject (Player, Ptera, Tree), because then
	// we would need two views per gameObject (high and low (pixel) res) and
	// that would be to overkill at all.
	//
	// - controller: the gameObjectHandler manages all objects on the map and
	// reacts on user inputs, collisions and updates the player position (moves
	// all obstacles)
	//
	// 2. description: Our Game is a scroll slider with a fixed Player that can
	// jump and
	// duck. Obstacles are a tree and a flying Ptera. The goal is it to evade
	// all obstacles that are coming in.
	//
	// 3. performance: for better performance we separated the game in different
	// threads. two are only for the view (lighthouse socket and pc gui
	// painting)
	// and one for the game logic itself.
	//
	// 4. view versions and scaling: the gui got different debugging modes: grid
	// painting, collision box painting and info painting. two fit with the
	// lighthouse the double positions of the gameObjects in the high res gui
	// are split into same sized blocks. to add additional comfort the high res
	// gui scales with resized windows, but is always showing the same content,
	// so you can't hack the game (seeing what's coming earlier) by resizing the
	// window.
	//
	// 5. features: for more fun we added some nice textures and a library to
	// play sounds. some start parameters help you to directly start the right
	// mode of the game. a menu helps you to debug the game. for example to show
	// which way you died by enabling the collision boxes.

	// entry point of program
	public static void main(String[] args) {

		// to not block edit
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				// frame that holds the game if highresview should be displayed
				DisplayFrame gameFrame;
				boolean started = false;
				// evaluate startup paramters
				found: if (args.length > 0) {
					String arg;
					for (int i = 0; i < args.length; i++) {
						arg = args[i];
						if (arg.equalsIgnoreCase("-d") || arg.equalsIgnoreCase("-double")) {
							// start game with both views enabled
							gameFrame = new DisplayFrame();
							gameFrame.addGame(new RunningBronto(true, true));
							started = true;
							break found;
						} else if (arg.equalsIgnoreCase("-lh") || arg.equalsIgnoreCase("lighthouse")) {
							// TODO non functional because console input would
							// be needed
							// (start only lighthouse view)
							// new MapProject(false, true);
							// started = true;
							break found;
						}
					}

				}
				if (!started) {
					// if no start up params were specified or the ones given
					// could not be evaluated
					// to something useful the standard will be created (only
					// high res)
					gameFrame = new DisplayFrame();
					gameFrame.addGame(new RunningBronto());
				}

			}
		});
	}

	// meta data
	private String title = "Running Bronto";
	private String version = "v0.37a";

	// default settings
	private int pixelPerMeter;
	private int width = 800;
	private int height = 450;

	// views
	private HighResView highresView;
	private boolean hightresViewActive = false;

	// Lighthouse related stuff
	private String urlString = "http://localhost:8000/lh.html";
	private LighthouseView lighthouseView;
	private boolean lighthouseViewActive = false;

	// process that holds a reference to the local lighthouse server
	private Process lhProcess;
	// thread that sends data to the lighthouse
	private Thread lighthouseThread;

	// controller for all game objects/logic
	private GameObjectHandler goHandler;

	// pauses the game. static because we want to be able to have the ability to
	// pause the game everywhere
	// and reduce overhead
	private static boolean paused = true;

	/**
	 * Constructs a new Game with only the high res view active.
	 */
	public RunningBronto() {
		this(true, false);
	}

	/**
	 * Constructs a new game.
	 * 
	 * @param highres
	 * @param lighthouse
	 */
	public RunningBronto(boolean highresView, boolean lighthouseView) {

		// initializing the game
		this.setTitle(title + " " + version);
		this.setPreferredSize(new Dimension(width, height));
		this.setFont(this.getFont().deriveFont(Font.BOLD, 15));
		;

		TinySound.init();
		KeyInput input = new KeyInput();
		this.addKeyListener(input);
		goHandler = new GameObjectHandler(input);
		goHandler.spawnPlayer();

		if (highresView) {
			// construct a new high res view with the chosen width and height;
			pixelPerMeter = height / 5;
			this.highresView = new HighResView(0, 0, width, height, pixelPerMeter);
			this.hightresViewActive = true;
		}
		if (lighthouseView) {
			// create a new lighthouse view
			try {
				createLighthouseViewDemo();
			} catch (IOException | URISyntaxException e) {
				System.err.println("Could not create lighthouse view!");
				e.printStackTrace();
			}
		}

		// start the game (-thread)
		this.startGame();
	}

	/**
	 * Opens a demo view. (Starts server and opens browsers window) This does
	 * currently not work on linux or mac.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void createLighthouseView(String host, int port) {
		// creating the lh view
		this.lighthouseView = new LighthouseView(host, port);
		this.lighthouseViewActive = true;
	}

	/**
	 * Opens a demo view. (Starts server and opens browsers window) This does
	 * currently not work on linux or mac.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void createLighthouseViewDemo() throws IOException, URISyntaxException {

		// start lighthouse server
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			lhProcess = new ProcessBuilder(System.getProperty("user.dir") + "/res/lighthouse/lhServer-win.exe").start();
		} else {
			throw new RuntimeException("OS not supported!");
		}

		// generating url/uri so browser window can be opened
		URL url = new URL(urlString);
		Desktop.getDesktop().browse(url.toURI());

		// creating the lh view
		this.lighthouseView = new LighthouseView("localhost", 8000);
		this.lighthouseViewActive = true;

		// adding routine to stop lh thread and close connection
		// so we can start our problem again without a problem
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO find a better (safer) solution.. use this atm because
				// interrupt causes problems
				RunningBronto.this.lighthouseThread.stop();

				try {
					RunningBronto.this.lighthouseView.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				lhProcess.destroy();
			}
		}));
		// TODO this does not seem to work
		// task: bring window to front after browser window has been opened
		// SwingUtilities.getWindowAncestor(this).toFront();
		// this.requestFocus();
	}

	@Override
	public void run() {

		// rendering takes place in its own thread so we can have as many
		// frames as we want :3
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					repaint();
				}
			}
		}, title + "-Renderer").start();

		// thread that handles rendering of the lighthouse view
		lighthouseThread = new Thread(new Runnable() {
			@Override
			public void run() {

				while (true) {
					if (lighthouseViewActive) {
						lighthouseView.render(goHandler);
					} else {
						// If the lighthouse view is not active we sleep and
						// look again after a second because it could get
						// enabled anytime
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, title + "-Lighthouse-Renderer");
		lighthouseThread.start();

		// main game loop
		int gameLoopFPS = 0; // measuring how often the game logic is executed
								// per second
		long lastTimeGameLoop = 0;

		// measuring how long the time between game loop executions was
		long currentTime = 0;
		long lastTime = 0;
		long deltaTime = 0;
		while (true) {
			currentTime = System.currentTimeMillis();
			deltaTime = currentTime - lastTime;

			// Logic Start
			if (!paused) {
				goHandler.update(deltaTime);
			}
			// Logic End

			// counting executions per second
			if (currentTime - lastTimeGameLoop >= 1000) {
				System.out.println("GLFPS: " + gameLoopFPS);
				lastTimeGameLoop = currentTime;
				gameLoopFPS = 0;
			} else {
				gameLoopFPS++;
			}
			lastTime = currentTime;

			try { // it is sufficient when the game logic is executed <= 1000
					// times per second
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		updateFPS(System.currentTimeMillis());

		// rendering everything in the current view
		if (hightresViewActive) {
			highresView.render(this, g, goHandler);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {

	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {

	}

	@Override
	public void componentResized(ComponentEvent e) {
		ActionLogger.LogAction("COMPONENT RESIZED", Color.RED);
		pixelPerMeter = this.getHeight() / 5;

		// TODO goHandler needs to know which view shows the most meters
		// in xdrirection so objects dont get spawned in view

		int max = 0;
		if (hightresViewActive) {
			highresView.resize(this.getWidth(), this.getHeight());
			if (highresView.getVisibleMeters() > max) {
				max = highresView.getVisibleMeters();
			}
		}
		if (lighthouseViewActive) {
			lighthouseView.resize(this.getWidth(), this.getHeight());
			if (lighthouseView.getVisibleMeters() > max) {
				max = lighthouseView.getVisibleMeters();
			}
		}
		goHandler.setMinSpawnX(max);
	}

	@Override
	public void componentShown(ComponentEvent arg0) {

	}

	/**
	 * Allow to pause the game logic
	 * 
	 * @param paused
	 */
	public static void setPaused(boolean paused) {
		RunningBronto.paused = paused;
	}

	/**
	 * Return if the game is currently paused
	 * 
	 * @return
	 */
	public static boolean isPaused() {
		return RunningBronto.paused;
	}

	public void toggleGrid() {
		if (hightresViewActive) {
			highresView.drawGrid(!highresView.isGridEnabled());
		}
	}

	public void toggleInfo() {
		if (hightresViewActive) {
			highresView.drawInfo(!highresView.isInfoEnabled());
		}

	}

	public void toggleBounding() {
		if (hightresViewActive) {
			highresView.drawBounding(!highresView.isBoundingEnabled());
		}
	}

}
