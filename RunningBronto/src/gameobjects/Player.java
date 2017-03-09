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
	
	public Player(double xPos, double yPos, double xSpeed, double ySpeed, double width, double height) {
		super(xPos, yPos, xSpeed, ySpeed, width, height);
	}
	
	public void setHeight(double x) {
		this.height = x;
	}
}
