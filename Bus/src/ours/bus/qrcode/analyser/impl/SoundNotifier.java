package ours.bus.qrcode.analyser.impl;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundNotifier {
	
	public static void playAuthorizedSound(){
		URL url = SoundNotifier.class.getClassLoader().getResource("/res/Authorized.mp3");
		playSound(url);
	}
	
	public static void playUnauthorizedSound(){
		URL url = SoundNotifier.class.getClassLoader().getResource("/res/Not valid.mp3");
		playSound(url);
	}
	
	public static void playBugSound(){
		// TODO !
		URL url = SoundNotifier.class.getClassLoader().getResource("/res/Not valid.mp3");
		playSound(url);
	}
	
	public static void playHackerSound(){
		URL url = SoundNotifier.class.getClassLoader().getResource("/res/res/Forged.mp3");
		playSound(url);
	}
	
	private static void playSound(URL sound){
		final Media media = new Media(sound.toString());
	    final MediaPlayer mediaPlayer = new MediaPlayer(media);
	    mediaPlayer.play();
	}

}
