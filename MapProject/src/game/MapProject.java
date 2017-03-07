package game;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import actionlogger.Action;
import actionlogger.ActionLogger;
import core.DisplayFrame;
import core.Game;
import display.Camera;
import display.HighResView;
import display.LighthouseView;
import gameobjects.Player;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;
import lighthouse.LighthouseController;

/**
 * Main class of project.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class MapProject extends Game {

	// TODO look at lighthouse only.

	// entry point of program
	public static void main(String[] args) {

		// because reasons .. :D
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				DisplayFrame gameFrame;
				boolean started = false;
				// evaluate startup paramters
				found: if (args.length > 0) {
					String arg;
					for (int i = 0; i < args.length; i++) {
						arg = args[i];
						if (arg.equalsIgnoreCase("-d") || arg.equalsIgnoreCase("-double")) {
							gameFrame = new DisplayFrame();
							gameFrame.addGame(new MapProject(true, true));
							started = true;
							break found;
						} else if (arg.equalsIgnoreCase("-lh") || arg.equalsIgnoreCase("lighthouse")) {
							// TODO non functional because console input would be needed
							//new MapProject(false, true);
							started = true;
							break found;
						}
					}

				}
				if (!started) {
					gameFrame = new DisplayFrame();
					gameFrame.addGame(new MapProject(true, true));
				}

			}
		});
	}

	// // Sound testing
	// TinySound.init();
	// Music m = TinySound.loadMusic(new
	// File("/Users/Christian/Desktop/jump.wav"));
	//
	// /*
	// * Fancy Graphics if pixelfactor > than ?
	// *
	//
	// */

	// meta data
	private String title = "Map Project";
	private String version = "v0.17a";

	// deafault settings
	private int pixelPerMeter = 200;
	private int width = 1920;
	private int height = 1080;

	// views
	private HighResView highresView;
	private boolean hightresViewActive = false; // TODO move these boolean into
												// camera
	
	// Lighthouse related stuff
	private String urlString = "http://localhost:8000/lh.html";											
	private LighthouseView lighthouseView;
	private boolean lighthouseViewActive = false;
	private Thread lighthouseThread;

	private Map map;
	private static boolean paused = false;

	public static final double GRAVITY = -9.81;

	private Process lhProcess;
	
	
	/**
	 * Constructs a new Game with only the high res view active.
	 */
	public MapProject() {
		this(true, false);
	}

	/**
	 * Constructs a new game.
	 * 
	 * @param highres
	 * @param lighthouse
	 */
	public MapProject(boolean highresView, boolean lighthouseView) {
		this.setTitle(title + " " + version);
		this.setPreferredSize(new Dimension(width, height));

		if (highresView) {
			this.highresView = new HighResView(0, 0, width, height, pixelPerMeter);
			this.hightresViewActive = true;
		}
		if (lighthouseView) {
			try {
				createLighthouseView();
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}

		// some initialization for game objects
		this.setFont(this.getFont().deriveFont(Font.BOLD, 15));;

		map = new Map(width, pixelPerMeter);
		map.spawnPlayer();

		this.startGame();
	}
	
	// TODO keep in mind that remote location does not need process
	private void createLighthouseView() throws IOException, URISyntaxException {
		
		// start lighthouse server
		lhProcess = new ProcessBuilder("C:\\Users\\Christian\\Desktop\\LighthouseProject\\LighthouseProject.exe").start();
		
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
				MapProject.this.lighthouseThread.stop();
			
				try {
					MapProject.this.lighthouseView.disconnect();
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
						lighthouseView.render(map.getGameObjectHandler().getGameObjects());
					} else {
						// If the lighthouse view is not active we sleep and look again
						// after a second because it could get enabled anytime
						try {
							Thread.sleep(1000);
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
				map.update(deltaTime);
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
					// time per second
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
			highresView.render(this, g, map.getGameObjectHandler().getGameObjects());
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Player p = map.getGameObjectHandler().getPlayer();
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			ActionLogger.LogAction("PLAYER JUMPED");
			System.err.println("JUMPED");
			p.setYSpeed(7.5);
		}

		// pausing the game
		if (e.getKeyCode() == KeyEvent.VK_P) {
			if (paused) {
				ActionLogger.LogAction("Game unpaused!", Color.ORANGE);
			} else {
				ActionLogger.LogAction("Game paused!", Color.RED);
			}
			paused = !paused;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

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
		map.resize(this.getWidth(), pixelPerMeter);
		highresView.resize(this.getWidth(), this.getHeight());
	}

	@Override
	public void componentShown(ComponentEvent arg0) {

	}

	public static void setPaused(boolean paused) {
		MapProject.paused = paused;
	}

	public static boolean isPaused() {
		return MapProject.paused;
	}

}
