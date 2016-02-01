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

public class SoundNotifier {
	
	private void playAuthorizedSound(){
		
	}
	
	private void playUnauthorizedSound(){
		
	}
	
	private void playSound(URL sound){
	    Clip clip;
		try {
			clip = AudioSystem.getClip();
			// getAudioInputStream() also accepts a File or InputStream
			AudioInputStream ais = AudioSystem.
					getAudioInputStream( sound );
			clip.open(ais);
			clip.loop(0);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					// A GUI element to prevent the Clip's daemon Thread
					// from terminating at the end of the main()
					JOptionPane.showMessageDialog(null, "Close to exit!");
				}
			});
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
