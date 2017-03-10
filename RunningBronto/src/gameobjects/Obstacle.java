package gameobjects;

/**
 * Reperesents an obstacle the player can collide with.
 * And acts as a super class for more specific obstacles like trees and pteras that can have individual
 * sprites/textures.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class Obstacle extends GameObject {

	public Obstacle(double xPos, double yPos, double xSpeed, double ySpeed, double width, double height) {
		super(xPos, yPos, xSpeed, ySpeed, width, height);
	}
}
