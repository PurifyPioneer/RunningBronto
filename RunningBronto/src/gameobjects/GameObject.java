package gameobjects;

import java.awt.geom.Rectangle2D;

import utility.Vector2D_Double;

/**
 * Represents an object that can live in our game world.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class GameObject {

	private Vector2D_Double position;
	
	protected double width;
	protected double height;
	
	private Vector2D_Double speed;
	
	private Rectangle2D.Double boundingBox;
	
	public GameObject(double xPos, double yPos, double xSpeed, double ySpeed, double width, double height) {
		
		this.position = new Vector2D_Double(xPos, yPos);
		this.speed = new Vector2D_Double(xSpeed, ySpeed);
		
		this.width = width;
		this.height = height;
		
		boundingBox = new Rectangle2D.Double(position.getXComponent(), position.getYComponent(), getWidth(), getHeight());
	}
	
	public Vector2D_Double getPosition() {
		return this.position;
	}
	
	public void setPosition(double xPos, double yPos) {
		setXPos(xPos);
		setYPos(yPos);
	}
	
	public double getXPos() {
		return position.getXComponent();
	}

	public void setXPos(double xPos) {
		this.position.setXComponent(xPos);
	}
	
	public double getYPos() {
		return position.getYComponent();
	}

	public void setYPos(double yPos) {
		this.position.setYComponent(yPos);
	}
	
	public double getWidth() {
		return width;
	}

	
	public double getHeight() {
		return height;
	}
	
	public Vector2D_Double getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(double xSpeed, double ySpeed) {
		setXSpeed(xSpeed);
		setYSpeed(ySpeed);
	}
	
	public double getXSpeed() {
		return this.speed.getXComponent();
	}
	
	public void setXSpeed(double xSpeed) {
		this.speed.setXComponent(xSpeed);
	}
	
	public double getYSpeed() {
		return this.speed.getYComponent();
	}
	
	public void setYSpeed(double ySpeed) {
		this.speed.setYComponent(ySpeed);
	}

	
	public Rectangle2D.Double getBoundingBox() {
		return boundingBox;
	}

	
	public void updateBoundingBox() {
		this.boundingBox.setRect(getXPos(), getYPos(), getWidth(), getHeight());
	}
}
