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

	// entry point of program
	public static void main(String[] args) {

		// to not block edt
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
							// TODO non functional because console input would be needed
							// (start only lighthouse view)
							// new MapProject(false, true);
							// started = true;
							break found;
						}
					}

				}
				if (!started) {
					// if no start up params were specified or the ones given could not be evaluated
					// to something useful the standard will be created (only high res)
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
	
	// pauses the game. static because we want to be able to have the ability to pause the game everywhere
	// and reduce overhead
	private static boolean paused = false;

	
	
	
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
		this.setFont(this.getFont().deriveFont(Font.BOLD, 15));;
		
		TinySound.init();
		KeyInput input = new KeyInput();
		this.addKeyListener(input);
		goHandler = new GameObjectHandler(width, pixelPerMeter, input);
		goHandler.spawnPlayer();
		
		if (highresView) {
			// construct a new high res view with the chosen width and height;
			pixelPerMeter = height/5;
			this.highresView = new HighResView(0, 0, width, height, pixelPerMeter);
			this.hightresViewActive = true;
		}
		if (lighthouseView) {
			// create a new lighthouse view
			try {
				createLighthouseView();
			} catch (IOException | URISyntaxException e) {
				System.err.println("Could not create lighthouse view!");
				e.printStackTrace();
			}
		}

		// start the game (-thread)
		this.startGame();
	}
	
	// TODO keep in mind that remote location does not need process
	public void createLighthouseView() throws IOException, URISyntaxException {
		
		// TODO make working for remote connection!
		
		// start lighthouse server
		lhProcess = new ProcessBuilder(System.getProperty("user.dir") + "/res/lighthouse/lhServer-win.exe").start();
		
		// generating url/uri so browser window can be opened
		URL url = new URL(urlString);
		Desktop.getDesktop().browse(url.toURI());

		// creating the lh view
		this.lighthouseView = new LighthouseView();
		this.lighthouseViewActive = true;
		
		// adding routine to stop lh thread and close connection
		// so we can start our problem again without a problem
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO find a better (safer) solution.. interrupt causes problems
				RunningBronto.this.lighthouseThread.stop();
			
				try {
					RunningBronto.this.lighthouseView.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				lhProcess.destroy();
			}
		}));
		
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
						lighthouseView.render(goHandler.getGameObjects());
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
			highresView.render(this, g, goHandler.getGameObjects());
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
		pixelPerMeter = this.getHeight()/5;
		
		// TODO goHandler needs to know which view shows the most meters
		// in xdrirection so objects dont get spawned in view
		
		goHandler.resize(this.getWidth(), pixelPerMeter);
		if (hightresViewActive) {
			highresView.resize(this.getWidth(), this.getHeight());
		}
		if (lighthouseViewActive) {
			lighthouseView.resize(this.getWidth(), this.getHeight());
		}
	}

	@Override
	public void componentShown(ComponentEvent arg0) {

	}

	/**
	 * Allow to pause the game logic
	 * @param paused
	 */
	public static void setPaused(boolean paused) {
		RunningBronto.paused = paused;
	}

	/**
	 * Return if the game is currently paused
	 * @return
	 */
	public static boolean isPaused() {
		return RunningBronto.paused;
	}

}
