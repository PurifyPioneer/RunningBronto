package actionlogger;

import java.awt.Color;

/**
 * Tied to the action logger.
 * Actions have a text and can for example
 * be displayed to the user to make sure that
 * input got accepted/registered correctly.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class Action {

	// Members
	private String action;
	private Color color;
	private long timeStamp;
	
	/**
	 * Creates a new Action that only requires
	 * its context.
	 * @param action
	 */
	public Action(String action) {
		this(action, Color.BLACK, System.currentTimeMillis());
	}
	
	/**
	 * Creates a new Action that also requires a time stamp.
	 * @param action
	 * @param timeStamp
	 */
	public Action(String action, long timeStamp) {
		this(action, Color.BLACK, timeStamp);
	}
	
	/**
	 * Creates a new Action that offers to store a color value
	 * for special cases or more important events.
	 * @param action
	 * @param color
	 * @param timeStamp
	 */
	public Action(String action, Color color, long timeStamp) {
		this.action = action;
		this.color = color;
		this.timeStamp = timeStamp;
	}
	
	/**
	 * Returns the context of this action
	 * @return
	 */
	public String getAction() {
		return this.action;
	}
	
	/**
	 * Returns the color of this action.
	 * @return
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Returns the timestamp of this action.
	 * Used to determine how long this action is going to be shown.
	 * @return
	 */
	public long getTimestamp() {
		return this.timeStamp;
	}
}
