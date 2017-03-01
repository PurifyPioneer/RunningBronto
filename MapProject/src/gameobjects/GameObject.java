package gameobjects;


public class GameObject {

	double xPos;
	double yPos;
	
	double width;
	double height;
	
	// TODO make speed/velocity a vector for easier access/modifiability
	double speed;
	
	public GameObject(double xPos, double yPos, double width, double height) {
		
		this.xPos = xPos;
		this.yPos = yPos;
		
		this.width = width;
		this.height = height;
	}
	
	public double getXPos() {
		return xPos;
	}

	
	public double getYPos() {
		return yPos;
	}

	
	public double getWidth() {
		return width;
	}

	
	public double getHeight() {
		return height;
	}
	
}
