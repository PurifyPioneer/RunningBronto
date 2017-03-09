package input;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import actionlogger.ActionLogger;
import game.RunningBronto;

/**
 * Class the specifically handles the keyboard input.
 * It notifies the input class on what the user wants to do.
 * From there the object handler gets its information.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class KeyInput extends Input implements KeyListener{

	@Override
	public void keyPressed(KeyEvent e) {
		// player jump
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			ActionLogger.LogAction("PLAYER JUMPING");
			setJumping(true);
		}
		// player duck
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			ActionLogger.LogAction("PLAYER DUCKING");
			setDucking(true);
		}
		// pausing the game
		if (e.getKeyCode() == KeyEvent.VK_P) {
			if (RunningBronto.isPaused()) {
				ActionLogger.LogAction("Game unpaused!", Color.ORANGE);
			} else {
				ActionLogger.LogAction("Game paused!", Color.RED);
			}
			// toggle paused state
			RunningBronto.setPaused(!RunningBronto.isPaused());;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// player not jumping any longer
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			setJumping(false);
		}
		// player not ducking any longer
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			setDucking(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
