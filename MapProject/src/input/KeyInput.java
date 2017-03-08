package input;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import actionlogger.ActionLogger;
import core.SoundHandler;
import gameobjects.GameObjectHandler;
import gameobjects.Player;

public class KeyInput extends Input implements KeyListener{
	
	public KeyInput() {
		super();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			ActionLogger.LogAction("PLAYER JUMPING");
			setJumping(true);
			SoundHandler.playJump();
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			setDucking(true);
		}

//		// pausing the game
//		if (e.getKeyCode() == KeyEvent.VK_P) {
//			if (paused) {
//				ActionLogger.LogAction("Game unpaused!", Color.ORANGE);
//			} else {
//				ActionLogger.LogAction("Game paused!", Color.RED);
//			}
//			paused = !paused;
//		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			setJumping(false);
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			setDucking(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}
	
}
