package input;

public class Input {

	private boolean isJumping;
	private boolean isDucking;
	
	public Input() {
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}
	
	public boolean isJumping() {
		return this.isJumping;
	}
	
	public void setDucking(boolean isDucking) {
		this.isDucking = isDucking;
	}
	
	public boolean isDucking() {
		return this.isDucking;
	}
}
