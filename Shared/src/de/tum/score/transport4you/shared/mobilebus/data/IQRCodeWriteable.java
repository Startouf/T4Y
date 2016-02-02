package de.tum.score.transport4you.shared.mobilebus.data;

import java.security.PrivateKey;

import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeNotBoundToEticketException;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeNotSignedException;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeSerializationFailedException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;

/**
 * Interface responsible for building a QRCode from an Existing ETicket
 * The QR-Code should contain
 * * The Serialized version of an ETicket
 * * The signature = RSA( Hash( Serialized string))
 * @author Cyril
 *
 */
public interface IQRCodeWriteable {
	
	/**
	 * Set the ETicket this QR-Code should bind.
	 * @param e
	 */
	public void setETicket(ETicket e);
	
	/**
	 * Sign the ETicket with a key. 
	 * signature = RSA( Hash( Serialized ETicket))
	 * @param key Private Key used for signature
	 */
	public void signETicket(PrivateKey key);
	
	
	/**
	 * 
	 * @return String that can be represented as QRCode image
	 * @throws QRCodeNotBoundToEticketException
	 * @throws QRCodeNotSignedException
	 * @throws QRCodeSerializationFailedException 
	 */
	public String serialize() throws QRCodeNotBoundToEticketException, QRCodeNotSignedException, QRCodeSerializationFailedException;

}
