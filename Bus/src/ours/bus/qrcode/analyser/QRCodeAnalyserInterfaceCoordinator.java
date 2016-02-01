package ours.bus.qrcode.analyser;

import ours.bus.qrcode.analyser.impl.Startup;

public class QRCodeAnalyserInterfaceCoordinator {
	/**
	 * Returns the startup class
	 * @return
	 */
	public static IStartup getStartup(){
		// TODO adapter
		return null;
	}

	public static IQRCodeAnalyser getIQRCodeAnalyser() {
		return null;
	}
	
	// TODO : get the analyser interface to push stuff on stack
}
