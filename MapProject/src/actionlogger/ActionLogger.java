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
	private static Color bgColor = new Color(125, 245, 160); 
	private static boolean loggingEnabled = false;
	
	/**
	 * Logs an Action and assigns it a timestamp so it
	 * can later be removed when it got irrilevant because of
	 * age.
	 * @param action action to log
	 */
	public static void LogAction(String action) {
		LogAction(action, Color.BLACK);
	}
	
	/**
	 * Logs a new action that has a specific color.
	 * @param action
	 * @param color
	 */
	public static void LogAction(String action, Color color) {
		if (loggingEnabled) {
			actions.add(new Action(action, color, System.currentTimeMillis()));
		}
	}
	
	/**
	 * Returns a list of all actions that have recently happened.
	 * @return a list of recent actions
	 */
	public static LinkedList<Action> getActions() {
		return actions;
	}
	
	/**
	 * Set the color of the background.
	 * @param color
	 */
	public static void setBackgroundColor(Color color) {
		ActionLogger.bgColor = color;
	}
	
	/**
	 * Returns the current color of the background.
	 * @return bgColor
	 */
	public static Color getBackgroundColor() {
		return ActionLogger.bgColor;
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
	 * Returns if logging is enabled.
	 * @return
	 */
	public static boolean isLoggingEnabled() {
		return loggingEnabled;
	}

	/**
	 * Set if logging should be enabled. It is disabled by default.
	 * @param loggingEnabled
	 */
	public static void setLoggingEnabled(boolean loggingEnabled) {
		ActionLogger.loggingEnabled = loggingEnabled;
	}

	/**
	 * Updates the ActionLogger and removes all old
	 * Actions from the log.
	 */
	public static void update() {
		for (int i = 0; i < actions.size(); i++) {
			if (System.currentTimeMillis() - actions.get(i).getTimestamp() > lifetime) {
				actions.remove(i);
			}
		}
	}
}
