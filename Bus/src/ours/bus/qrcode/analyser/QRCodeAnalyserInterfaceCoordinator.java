package ours.bus.qrcode.analyser;

import ours.bus.qrcode.analyser.impl.QRCodeAnalyser;
import ours.bus.qrcode.analyser.impl.Startup;

public class QRCodeAnalyserInterfaceCoordinator {
	/**
	 * Returns the startup class
	 * @return
	 */
	public static IStartup getStartup(){
		return Startup.getInstance();
	}

	public static IQRCodeAnalyser getIQRCodeAnalyser() {
		return QRCodeAnalyser.getInstance();
	}
	
	// TODO : get the analyser interface to push stuff on stack
}
