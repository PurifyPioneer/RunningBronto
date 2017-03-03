package lighthouse;

import java.awt.Color;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Controller that allows to set handles the connection to the lighthouse
 * and makes setting of colors for individual windows much easier by allowing
 * to choose windows by coordinate.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class LighthouseController {

	private LighthouseNetwork lhNetwork;
	private byte[] data;
	
	/**
	 * Creates a new LighthouseController that
	 * connects to localhost and port 8000
	 */
	public LighthouseController() {
		this("localhost", 8000);
	}
	
	/**
	 * Creates a new LighthouseController that
	 * can connect to any horst and port
	 * @param host
	 * @param port
	 */
	public LighthouseController(String host, int port) {
		lhNetwork = new LighthouseNetwork(host, port);
		data = new byte[1176];
	}
	
	public void connect() throws UnknownHostException, IOException {
			lhNetwork.connect();
	}
	
	/**
	 * Allows to set specific pixel of the lighthouse to a chosen color
	 * without the need to worry about the underlying data.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param color color
	 * @throws CoordinatesOutOfBoundsException 
	 */
	public void setLighthousePixel(int x, int y, Color color) throws CoordinatesOutOfBoundsException {
		
		// we accept the number of windows as a constant so they are hard coded ;)
		if (x < 0 || x > 27 || y < 0 || y > 13) {
			throw new CoordinatesOutOfBoundsException();
		}
		
		int xPosInArray = x * 3;  // 3 bytes per window
		int yPosInArray = y * 84; // 84 byte per story
		int acessArrayAt = xPosInArray + yPosInArray;
		
		data[acessArrayAt] = (byte) color.getRed();
		data[acessArrayAt+1] = (byte) color.getGreen();
		data[acessArrayAt+2] = (byte) color.getBlue();
		
		try {
			lhNetwork.send(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
