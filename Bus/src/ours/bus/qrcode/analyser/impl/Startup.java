package ours.bus.qrcode.analyser.impl;

import org.apache.log4j.Logger;

import ours.bus.qrcode.analyser.IStartup;
import ours.bus.qrcode.analyser.impl.Startup;

public class Startup implements IStartup {
	
	/* Used for storing the singleton instance */
	private static Startup instance = null;
	
	/* Logger */
	private Logger logger = Logger.getLogger("Application");

	@Override
	public void init() {
		QRCodeAnalyser.getInstance().start();
	}
	
	/**
	 * Returns the singleton instance of this class
	 * @return
	 */
	public static IStartup getInstance() {
		if(instance==null){
			instance = new Startup();
		}
		
		return instance;
	}

}
