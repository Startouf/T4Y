package ours.bus.qrcode.analyser;

public interface IQRCodeAnalyser {

	/**
	 * Push a String on the analyse list
	 * @param detectedQRCode
	 */
	void analyseQRCode(String detectedQRCode);

}
