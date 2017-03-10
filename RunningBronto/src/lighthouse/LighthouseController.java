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

	private int lighthouseWidth = 27;
	private int lighthouseHeight = 14;
	
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
	
	public void disconnect() throws IOException {
		lhNetwork.disconnect();
	}
	
	public boolean isConnected() {
		return lhNetwork.isConnected();
	}
	
	/**
	 * When everything has been drawn we push
	 * the data/image and then reset the data
	 * for next loop.
	 */
	public void pushFullImage() {
		try {
			lhNetwork.send(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
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
		if (x < 0 || x > lighthouseWidth || y < 0 || y > lighthouseHeight) {
			throw new CoordinatesOutOfBoundsException();
		} else {
			int xPosInArray = x * 3;  // 3 bytes per window
			int yPosInArray = y * 84; // 84 byte per story
			int acessArrayAt = xPosInArray + yPosInArray;
			
			data[acessArrayAt] = (byte) color.getRed();
			data[acessArrayAt+1] = (byte) color.getGreen();
			data[acessArrayAt+2] = (byte) color.getBlue();
		}
	}
	
	/**
	 * Constructs a filled rectangle at a chosen position on the
	 * lighthouse.
	 * Tries to emulate the functionality we can normally have when using
	 * a graphics object.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void fillRectangle(int x, int y, int width, int height, Color color) {
		
		boolean xOkay = false;
		boolean yOkay = false;
		
		int xPosNew = 0;
		int yPosNew = 0;
		int remainingWidth = 0;
		int remainingHeight = 0;
		
		// check x
		if (!(x > lighthouseWidth)) {
			// check left border
			if ((x < 0) && (x + width > 0)) {
				xPosNew = 0;
				remainingWidth = width - x -2;
				xOkay = true;
			} else if(x >= 0) {
				// x + width within border
				if (x + width < lighthouseWidth) {
					xPosNew = x;
					remainingWidth = width;
					xOkay = true;
				} else {
					xPosNew = x + 1;
					remainingWidth = lighthouseWidth - x;
					xOkay = true;
				}
			}
		}
		
		if (!(y > lighthouseHeight)) {
			if ((y < 0) && (y + height > 0)) {
				yPosNew = 0;
				remainingHeight = height - y;
				yOkay  = true;
			} else if (y >= 0) {
				if (y + height < lighthouseHeight) {
					yPosNew = y;
					remainingHeight = height;
					yOkay = true;
				} else {
					yPosNew = y;
					remainingHeight = lighthouseHeight - y;
					yOkay = true;
				}
			}
		}
		
		if (xOkay && yOkay) {
			for (int i = 0; i < remainingWidth; i++) {
				for (int j = 0; j < remainingHeight; j++) {
					try {
						setLighthousePixel(xPosNew + i, yPosNew + j, color);
					} catch (CoordinatesOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public int getLighthouseWidth() {
		return this.lighthouseWidth;
	}
	
	public int getLighthouseHeight() {
		return this.lighthouseHeight;
	}
	
}
