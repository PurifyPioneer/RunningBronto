package gameobjects;


public class Obstacle extends GameObject {

	public Obstacle(double xPos, double yPos, double width, double height) {
		super(xPos, yPos, width, height);
	}

	public void move(long deltaTime) {
		this.xPos += -0.005 * deltaTime;
		
	}
	
}
