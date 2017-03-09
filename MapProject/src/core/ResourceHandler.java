package core;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 * Handles all our resources for the game like Images and Sounds.
 * Loads data on demand and then caches the data for further use.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class ResourceHandler {

	private static final String RES = "/res/";
	private static final String IMAGES = RES + "img/";
	private static final String SOUNDS = RES + "sound/";

	private static Map<String, Image> imageCache = new HashMap<String, Image>();
	private static Map<String, Sound> soundChache = new HashMap<String, Sound>();

	/**
	 * Load an Image.
	 * @param file
	 * @return
	 */
	public static Image getImage(String file) {
		if (!imageCache.containsKey(file)) {
			imageCache.put(file, new ImageIcon(System.getProperty("user.dir") + IMAGES + file).getImage());
		}
		return imageCache.get(file);
	}

	/**
	 * Load a transparent Image.
	 * @param file
	 * @return
	 */
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
	
	/**
	 * Returns a sound. If it is not already loaded
	 * it will be loaded from the file system and cached for later use.
	 * @param file
	 * @return
	 */
	public static Sound getSound(String file) {
		if (!soundChache.containsKey(file)) {
			soundChache.put(file, TinySound.loadSound(new File(System.getProperty("user.dir") + SOUNDS + file)));
		}
		return soundChache.get(file);
	}
	
}
