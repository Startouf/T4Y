package ours.bus.qrcode.scanner.impl;

import org.apache.log4j.Logger;

import ours.bus.qrcode.scanner.IStartup;
import ours.bus.qrcode.scanner.error.QRCodeScannerInitializingException;

public class Startup implements IStartup {

	/* Used for storing the singleton instance */
	private static Startup instance = null;
	
	/* Logger */
	private Logger logger = Logger.getLogger("Application");
	
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
	
	@Override
	public void init() {
		logger.debug("Starting the QR-Code Scanner Component");
		try {
			QRCodeScanner.getInstance().init();
			logger.info("QRCode Scanner thread started");
		} catch (QRCodeScannerInitializingException e) {
			logger.error("QRCode Scanner thread failed to start");
			e.printStackTrace();
		}

	}

}
