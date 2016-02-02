package ours.bus.qrcode.analyser.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.bouncycastle.openssl.PEMReader;

import de.tum.score.transport4you.bus.data.datacontroller.DataControllerInterfaceCoordinator;
import de.tum.score.transport4you.bus.data.datacontroller.error.ConfigurationLoadingException;
import de.tum.score.transport4you.bus.data.datacontroller.error.DataControllerInitializingException;
import de.tum.score.transport4you.bus.data.datacontroller.impl.PropertiesConfigurationEntries;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeNotDeserializableException;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeStringNotSetException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.QRCode;
import ours.bus.qrcode.analyser.IQRCodeAnalyser;
import ours.bus.qrcode.analyser.error.QRCodeAnalyserInitializingException;
import ours.bus.qrcode.scanner.error.QRCodeScannerInitializingException;
import ours.bus.qrcode.scanner.impl.QRCodeScanner;

public class QRCodeAnalyser extends Thread implements IQRCodeAnalyser {
	
	private Logger logger = Logger.getLogger("QRAnalyser");
	private static QRCodeAnalyser instance = null;
	
	private static Queue<String> queue = new LinkedBlockingQueue<String>();
	private String qrStringToProcess;
	private QRCode qrCode;
	private ETicket ticket;
	private boolean initialized;
	
	private PublicKey pub;
	
	public void init(File configFile){
		PropertiesConfiguration propertiesConfiguration;
		try {
			File publicKeyRes = new File(this.getClass().getClassLoader().getResource("BlobEncryptionKey-cert.pem").toURI());
			FileReader fr = new FileReader(publicKeyRes);
			PEMReader r = new PEMReader(fr);
			X509Certificate cert = (X509Certificate) r.readObject();
			pub = (PublicKey) cert.getPublicKey();
			r.close();
			fr.close();
		} catch (IOException | URISyntaxException e) {
			logger.error("Couldn't load public key for QR-Code verification");
			e.printStackTrace();
		}
		this.setName("QR-Code Analyser Thread");
		this.start();
	}
	
	public void run(){
		while ( !isInterrupted() ) {
            try {
                synchronized ( queue ) {
                    while ( queue.isEmpty() )
                        queue.wait();
                    // Get the next work item off of the queue
                    qrStringToProcess = queue.remove();
                }
                checkETicketFromString(qrStringToProcess);
                
            }
            catch ( InterruptedException ie ) {
                break;
			}
        }
	}
	
	public void checkETicketFromString(String qrString){
		qrCode = new QRCode();
        try {
        	qrCode.deserialize(qrString);
        	ticket = qrCode.getETicket();
        	boolean validSignature = qrCode.validSignature(pub);
        	boolean validTicket = isTicketValid(ticket);
        	
			if(validSignature){
				if(validTicket){
					logger.info("Ticket signature valid & ETicket valid");
					BusNotifier.playAuthorizedSound();
				} else{
					logger.info("Ticket signature valid but ETicket not valid");
					BusNotifier.playUnauthorizedSound();
				}
			} else{
				if(validTicket){
					logger.info("Ticket signature invalid, yet ETicket valid");
					BusNotifier.playHackerSound();
				} else {
					logger.info("Not a valid signature, the ticket is also not valid");
					BusNotifier.playHackerSound();
				}
			}
		} catch (QRCodeNotDeserializableException e) {
			logger.warn("Could not deserialize QRCode");
			BusNotifier.playBugSound();
			e.printStackTrace();
		} catch (QRCodeStringNotSetException e) {
			logger.warn("The detected QRCode is not valid");
			e.printStackTrace();
		}
	}

	private boolean isTicketValid(ETicket ticket) {
		return (ticket.getValidUntil().compareTo(new Date()) >= 0);
	}

	@Override
	public void analyseQRCode(String detectedQRCode) {
		synchronized ( queue ) {
		    queue.add(detectedQRCode);
		    // Notify the monitor object that all the threads
		    // are waiting on. This will awaken just one to
		    // begin processing work from the queue
		    queue.notify();
		}
	}
	
	/**
	 * The method to return the singleton instance of the Data Controller
	 * @return
	 */
	public static QRCodeAnalyser getInstance(){
		if(instance==null) {
			instance = new QRCodeAnalyser();
		}
		return instance;
	}
	
	/**
	 * This method needs to be called to initialize the Data Controller correctly.<br>Note that this method must only be called once.
	 * @param configurationFile
	 * @throws DataControllerInitializingException 
	 */
	public void init() throws QRCodeAnalyserInitializingException {
		logger.debug("Initializing QRCode Analyser");
		
		if(initialized) {
			//Class was already initialized
			logger.error("QR-Analyser was already initialized");
			throw new QRCodeAnalyserInitializingException("QR-Analyser was already initialized");
		} else {
			//Initialize
			QRCodeScanner.getInstance().start();
			logger.debug("QR-Analyser thread started");
			this.initialized = true;
		}
	}

}
