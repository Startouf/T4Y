package ours.bus.qrcode.analyser;

import java.io.File;

/**
 * Describe interfaces to start this component
 * @author Cyril
 *
 */
public interface IStartup {
	
	/**
	 * Starts the QRCode analyser component
	 * @param configurationFile 
	 */
	public void init(File configurationFile);

}