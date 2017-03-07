package core;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceHandler {

	private static final String RES = "/res/";
	private static final String IMAGES = RES + "img/";

	private static Map<String, Image> imageCache = new HashMap<String, Image>();

	public static Image getImage(String file) {
		if (!imageCache.containsKey(file)) {
			imageCache.put(file, new ImageIcon(System.getProperty("user.dir") + IMAGES + file).getImage());
		}
		return imageCache.get(file);
	}

	public static Image getTransparentImage(String file) {
		if (!imageCache.containsKey(file)) {
			try {
				imageCache.put(file, ImageIO.read(new File(System.getProperty("user.dir") + IMAGES + file)));
			} catch (IOException e) {
				System.out.println("file: " + System.getProperty("user.dir") + IMAGES + file);
				e.printStackTrace();
				return null;
			}
		}
		return imageCache.get(file);
	}
	
}
