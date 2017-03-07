package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.LinkedList;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import actionlogger.Action;
import actionlogger.ActionLogger;
import core.DisplayFrame;
import core.Game;
import display.Camera;
import display.HighResView;
import gameobjects.Player;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

/**
 * Main class of project.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class MapProject extends Game {

	//TODO look at lighthouse only.
	
	// entry point of program
	public static void main(String[] args) {

		// because reasons .. :D
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				//@SuppressWarnings("unused")
				DisplayFrame gameFrame = new DisplayFrame();
				gameFrame.addGame(new MapProject());
			}

		});
	}
	
	// meta data
	private String title = "Map Projhect";
	private String version = "v0.1a";
	
	// deafult settings 
	private int pixelPerMeter = 100;
	private int width = 800;
	private int height = 450;
	
	private HighResView camera;
	private Camera lightHouseCamera;
	
	private Map map;
	private static boolean paused = false;
	
	public static final double GRAVITY = -9.81;
	
	
	public MapProject() {
		
		// Sound testing 
		TinySound.init();
		Music m = TinySound.loadMusic(new File("/Users/Christian/Desktop/jump.wav"));
		
//		LighthouseController lc = new LighthouseController();
//		try {
//			lc.connect();
//			lc.setLighthousePixel(27, 13, Color.RED);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		/*
		 * Fancy Graphics if pixelfactor > than ?
		 * 
		 * 
		 */
		
		this.setTitle(title + " " + version);
		this.setPreferredSize(new Dimension(width, height));
		camera = new HighResView(0, 0, width, height, pixelPerMeter);
		//lightHouseCamera = new Camera(0, 0, 14, 28, 2);
		map = new Map(width, pixelPerMeter);
		map.spawnPlayer();
		this.startGame();	
	}
	

	@Override
	public void run() {
		
		this.setFont(this.getFont().deriveFont(Font.BOLD, 15));;
		
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
		
		// main game loop	
		int gameLoopFPS = 0; // measuring how often the game logic is executed per second
		long lastTimeGameLoop = 0;
		
		// measuring how long the time between game loop executions was
		long currentTime = 0;
		long lastTime = 0;
		long deltaTime = 0;
		while (true) {
			currentTime = System.currentTimeMillis();
			deltaTime = currentTime - lastTime;
			
			//Logic Start
			if (!paused) {
				map.update(deltaTime);
			}
			//Logic End
			
			// counting executions per second
			if (currentTime - lastTimeGameLoop >= 1000) {
				System.out.println("GLFPS: " + gameLoopFPS);
				lastTimeGameLoop = currentTime;
				gameLoopFPS = 0;
			} else {
				gameLoopFPS++;
			}
			lastTime = currentTime;
			
			try { // it is sufficient when the game logic is executed <= 1000 time per second
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	// local variable .. avoid memory allocation.. TODO needs testing
	LinkedList<Action> actions;
	Color bgColor = new Color(125, 245, 160); 
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		updateFPS(System.currentTimeMillis());
		
		// rendering everything in the current view
		camera.render(g, map.getGameObjectHandler().getGameObjects());
		
		// draw fps counter draw basic information
		g.drawString("FPS: " + getFPS() + " width: " + this.getWidth() + " height: " + this.getHeight(), 10, 15);
		
		// draw paused info
		if (paused) {
			g.setColor(Color.RED);
			//g.drawString("Game is paused!", 300, 15);
			g.setColor(Color.BLACK);
		}
		
		// draw camera information
		g.drawString("CamX: " + camera.getXPos() + " CamY: " + camera.getYPos(), 10, 15 + g.getFontMetrics().getHeight());
		
		// draw content of action logger
		actions = ActionLogger.getActions();
		if (actions.size() > 0) {
			g.setColor(bgColor);
			g.fillRect(this.getWidth() - 205, 0, 205, actions.size() * g.getFontMetrics().getHeight() + 5);
			g.setColor(Color.BLACK);
			for (int i = 0; i < actions.size(); i++) {
				g.setColor(actions.get(i).getColor());
				g.drawString(actions.get(i).getAction(), this.getWidth() - 200, i * g.getFontMetrics().getHeight() + g.getFontMetrics().getHeight());
			}
			ActionLogger.update();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Player p = map.getGameObjectHandler().getPlayer();
		if (e.getKeyCode() == KeyEvent.VK_A) {
			ActionLogger.LogAction("PLAYER MOVED LEFT");
			p.setXSpeed(-2);
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			ActionLogger.LogAction("PLAYER MOVED RIGHT");
			p.setXSpeed(2);
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			ActionLogger.LogAction("PLAYER JUMPED");
			p.setYSpeed(7.5);
		}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			ActionLogger.LogAction("PLAYER POSITION RESET");
			p.setPosition(2, 0);
		}

// Camera movement disabled because jump'n run
//		if (e.getKeyCode() == KeyEvent.VK_W) {
//			ActionLogger.LogAction("PLAYER MOVED UP");
//			p.moveUp();
//		}
//		if (e.getKeyCode() == KeyEvent.VK_S) {
//			ActionLogger.LogAction("PLAYER MOVED DOWN");
//			p.moveDown();
//		}
//		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//			ActionLogger.LogAction("CAMERA MOVED RIGHT");
//			camera.moveRight();
//	}
//		if	 (e.getKeyCode() == KeyEvent.VK_LEFT) {
//			ActionLogger.LogAction("CAMERA MOVED LEFT");
//			camera.moveleft();
//		}
//		if (e.getKeyCode() == KeyEvent.VK_UP) {
//			ActionLogger.LogAction("CAMERA MOVED UP");
//			camera.moveUp();
//		}
//		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//			ActionLogger.LogAction("CAMERA MOVED DOWN");
//			camera.moveDown();
//		}
//		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//			System.exit(0);
//		}

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
		Player p = map.getGameObjectHandler().getPlayer();
		if (e.getKeyCode() == KeyEvent.VK_A) {
			p.setXSpeed(0);
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			p.setXSpeed(0);
		}
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
		camera.resize(this.getWidth(), this.getHeight());
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		
	}
	
	public static void setPaused(boolean paused) {
		MapProject.paused = paused;
 	}

}
