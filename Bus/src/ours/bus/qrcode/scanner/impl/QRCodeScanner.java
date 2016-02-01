package ours.bus.qrcode.scanner.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import de.tum.score.transport4you.bus.data.datacontroller.error.DataControllerInitializingException;
import de.tum.score.transport4you.bus.data.datacontroller.impl.DataController;
import ours.bus.qrcode.scanner.error.QRCodeScannerInitializingException;

/**
 * This class runs as a service and scans new images every second. 
 * It tries to detect a QR-Code and extract its information
 * @author Cyril
 *	
 */

public class QRCodeScanner extends Thread {
	
	private static QRCodeScanner instance = null;
	
	private Webcam webcam;
	private static Map hintMap = new HashMap();;
	static{
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	}
	private static String charset = "UTF-8";
	private String detectedQRCode;

	private boolean initialized = false;
	
	private Logger logger = Logger.getLogger("QRScanner");
	
	public QRCodeScanner(){
		webcam = Webcam.getDefault();
	}
	
	@Override
	public void run(){
		webcam.open();
		
		// While running, keep scanning for QR-codes
		while(!isInterrupted()){
			boolean codeSeen = scan();
			if(codeSeen){
				// Do something with class ETicket() ?
			}
			try {
				// Do not uselessly keep taking photos !
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				interrupt();
			}
		}
		release();
	}
	
	private void release() {
		webcam.close();
	}

	/**
	 * Performs a scan and if a (new) QR code was detected set it 
	 * @return true if QR code was decrypted successfully
	 */
	private boolean scan(){
		BufferedImage image = webcam.getImage();
		try {
			String newQRCode = readQRCode(image, charset, hintMap);
			if(newQRCode.equals(detectedQRCode)){
				return false;
			} else {
				detectedQRCode = newQRCode;
				return true;
			}
		} catch (NotFoundException e) {
			return false;
		}
	}
	
	/**
	 * http://javapapers.com/core-java/java-qr-code/
	 * @param image
	 * @param charset
	 * @param hintMap
	 * @return
	 * @throws NotFoundException
	 */
	private String readQRCode(BufferedImage image, String charset, Map hintMap)
			throws NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(
						image)));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
				hintMap);
		return qrCodeResult.getText();
	}
	
	/**
	 * The method to return the singleton instance of the Data Controller
	 * @return
	 */
	public static QRCodeScanner getInstance(){
		if(instance==null) {
			instance = new QRCodeScanner();
		}
		return instance;
	}
	
	/**
	 * This method needs to be called to initialize the Data Controller correctly.<br>Note that this method must only be called once.
	 * @param configurationFile
	 * @throws DataControllerInitializingException 
	 */
	public void init() throws QRCodeScannerInitializingException {
		logger.debug("Initializing Data Controller");
		
		if(initialized ) {
			//Class was already initialized
			logger.error("Scanner was already initialized");
			throw new QRCodeScannerInitializingException("Scanner was already initialized");
		} else {
			//Initialize
			QRCodeScanner.getInstance().start();
			logger.debug("Scanner finished");
			this.initialized = true;
		}
	}
}
