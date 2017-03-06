package utility;

/**
 * Represents a Vector that can either be interpreted as a
 * Location or Velocity.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class Vector2D {

	private double xComponent;
	private double yComponent;
	
	public Vector2D(double xComponent, double yComponent) {
		this.xComponent = xComponent;
		this.yComponent = yComponent;
	}

	
	public double getXComponent() {
		return xComponent;
	}

	
	public void setXComponent(double xComponent) {
		this.xComponent = xComponent;
	}

	
	public double getYComponent() {
		return yComponent;
	}

	
	public void setYComponent(double yComponent) {
		this.yComponent = yComponent;
	}
	
}
