package gameobjects;

public class Player extends GameObject {

	/**
	 * Idee:
	 * 
	 * Wir haben ein Sprite (Bronto).
	 * Dieser hat eine feste höhe in pixeln.
	 * Nun können wir eine beliebige höhe ingame festlegen.
	 * (z.B.: 2 meter)
	 * Daraus können wir nun den pixelPerMeter faktor berechnen (scaling)
	 * und alle anderne objekte, welche eine höhe/breite in meter haben
	 * dementsprechen zeichnen.
	 */
	
	private double ySpeed;
	
	public Player(double xPos, double yPos, double width, double height) {
		super(xPos, yPos, width, height);
	}
	
	public void update(long deltaTime) {
		// xpos
		this.xPos += this.speed * deltaTime;
		
		this.ySpeed += -0.000025 * deltaTime;
		
		
		this.yPos += this.ySpeed * deltaTime;
		
		if (this.yPos < 0) {
			this.ySpeed = 0;
			this.yPos = 0;
		}
	}
	
	public void setXSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setYSpeed(double yspeed) {
		this.ySpeed = yspeed;
	}
}
