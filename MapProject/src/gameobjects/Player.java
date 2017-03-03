package gameobjects;

public class Player extends GameObject {

	/**
	 * Idee:
	 * 
	 * Wir haben ein Sprite (Bronto).
	 * Dieser hat eine feste h�he in pixeln.
	 * Nun k�nnen wir eine beliebige h�he ingame festlegen.
	 * (z.B.: 2 meter)
	 * Daraus k�nnen wir nun den pixelPerMeter faktor berechnen (scaling)
	 * und alle anderne objekte, welche eine h�he/breite in meter haben
	 * dementsprechen zeichnen.
	 */
	
	public Player(double xPos, double yPos, double xSpeed, double ySpeed, double width, double height) {
		super(xPos, yPos, xSpeed, ySpeed, width, height);
	}
}
