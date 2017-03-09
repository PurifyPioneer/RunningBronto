package lighthouse;

/**
 * Exception to be thrown when coordinates exceed
 * the specified bounds. The caller needs to handle this
 * even if he chooses to do nothing.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class CoordinatesOutOfBoundsException extends Exception {

	/**
	 * Instantiates a new CoordinatesOutOfBoundsException.
	 */
    public CoordinatesOutOfBoundsException() {
    	super();
    }

	/**
	 * Instantiates a new CoordinatesOutOfBoundsException.
	 */
    public CoordinatesOutOfBoundsException(String message) {
        super (message);
    }

	/**
	 * Instantiates a new CoordinatesOutOfBoundsException.
	 */
    public CoordinatesOutOfBoundsException(Throwable cause) {
        super (cause);
    }

	/**
	 * Instantiates a new CoordinatesOutOfBoundsException.
	 */
    public CoordinatesOutOfBoundsException(String message, Throwable cause) {
        super (message, cause);
    }

}

