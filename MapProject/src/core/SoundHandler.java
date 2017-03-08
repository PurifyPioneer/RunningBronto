package core;

import java.io.File;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class SoundHandler {

	//TODO
	private static Sound jump;
	
	 public SoundHandler() {
		 TinySound.init();
		 jump = TinySound.loadSound(new
		 File(System.getProperty("user.dir") + "/res/sound/jump.wav"));
	 }
	
	public static void playJump() {
		new SoundHandler();
		jump.play();
	}
}
