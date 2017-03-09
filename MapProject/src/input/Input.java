package input;

/**
 * Class that holds general information about the input.
 * This gives us the possibility to easily implement
 * some sort of controller or other sorts of input.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class Input {

	// TODO maybe make further abstraction because currently this
	// is focused only on the player.
	// This is left here on purpose because the current version is enough for our needs.
	
	private boolean isJumping;
	private boolean isDucking;
	
	/**
	 * Set the player to jumping. (Request a jump. Actual logic is handler by GameObjectHandler)
	 * @param isJumping
	 */
	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}
	
	/**
	 * Returns if the player is currently jumping/wants to jump.
	 * @return
	 */
	public boolean isJumping() {
		return this.isJumping;
	}
	
	/**
	 * Set the player to ducking. (Request a duck. Actual logic is handler by GameObjectHandler)
	 * @param isDucking
	 */
	public void setDucking(boolean isDucking) {
		this.isDucking = isDucking;
	}
	
	/**
	 * Returns if the player is currently ducking/wants to duck.
	 * @return
	 */
	public boolean isDucking() {
		return this.isDucking;
	}
}
