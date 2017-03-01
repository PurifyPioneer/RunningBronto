package actionlogger;

import java.awt.Color;
import java.util.LinkedList;

/**
 * Utility class that gives us the ability to log
 * events that happened within our program.
 * This events can be viewed everywhere and may also
 * be displayed on the screen.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class ActionLogger {
	
	// Members
	private static LinkedList<Action> actions  = new LinkedList<Action>();
	private static int lifetime = 2500;
	
	/**
	 * Logs an Action and assigns it a timestamp so it
	 * can later be removed when it got irrilevant because of
	 * age.
	 * @param action action to log
	 */
	public static void LogAction(String action) {
		actions.add(new Action(action, System.currentTimeMillis()));
	}
	
	/**
	 * Logs a new action that has a specific color.
	 * @param action
	 * @param color
	 */
	public static void LogAction(String action, Color color) {
		actions.add(new Action(action, color, System.currentTimeMillis()));
	}
	
	/**
	 * Returns a list of all actions that have recently happened.
	 * @return a list of recent actions
	 */
	public static LinkedList<Action> getActions() {
		return actions;
	}
	
	/**
	 * Adjust the lifetime of all actions.
	 * (How long they will be displayed)
	 * @param lifetime time in seconds
	 */
	public static void setLifetime(double lifetime) {
		ActionLogger.lifetime = (int) (lifetime * 1000);
	}
	
	/**
	 * Updates the ActionLogger and removes all old
	 * Actions from the log.
	 */
	public static void update() {
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < actions.size(); i++) {
			if (currentTime - actions.get(i).getTimestamp() > lifetime) {
				actions.remove(i);
			}
		}
	}
}
