package ours.bus.qrcode.scanner;

import ours.bus.qrcode.scanner.impl.Startup;

public class QRCodeScannerInterfaceCoordinator {
	
	/**
	 * Returns the startup class
	 * @return
	 */
	public static IStartup getStartup(){
		return Startup.getInstance();
	}
}
