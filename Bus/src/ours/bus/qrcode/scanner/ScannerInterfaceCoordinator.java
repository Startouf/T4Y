package ours.bus.qrcode.scanner;

import ours.bus.qrcode.scanner.impl.Startup;

public class ScannerInterfaceCoordinator {
	
	/**
	 * Returns the startup class
	 * @return
	 */
	public static IStartup getStartup(){
		return Startup.getInstance();
	}
}
