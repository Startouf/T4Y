package ours.bus.qrcode.analyser.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BusNotifier {
	
	static final String SOUND_ROOT = "/Bus/res/";
	
	private static ClassLoader cl = BusNotifier.class.getClassLoader();
	static{
		// Must init JavaFX : http://stackoverflow.com/questions/14025718/javafx-toolkit-not-initialized-when-trying-to-play-an-mp3-file-through-mediap
		JFXPanel fxPanel = new JFXPanel();
	}
	
	public static void playAuthorizedSound(){
		playSound("permission granted.wav");
	}
	
	public static void playUnauthorizedSound(){
		playSound("negative.wav");
	}
	
	public static void playBugSound(){
		playSound("malfunction.wav");
	}
	
	public static void playHackerSound(){
		playSound("alien lifeform on board.wav");
	}
	
	private static void playSound(String sound){
		URL file = cl.getResource(sound);
		final Media media = new Media(file.toString());
		final MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

}
