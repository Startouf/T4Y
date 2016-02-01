package ours.bus.qrcode.analyser;

import ours.bus.qrcode.scanner.IStartup;
import ours.bus.qrcode.scanner.impl.Startup;

public class QRCodeAnalyserInterfaceCoordinator {
	/**
	 * Returns the startup class
	 * @return
	 */
	public static IStartup getStartup(){
		return Startup.getInstance();
	}

	public static IQRCodeAnalyser getIQRCodeAnalyser() {
		return null;
	}
	
	// TODO : get the analyser interface to push stuff on stack
}
