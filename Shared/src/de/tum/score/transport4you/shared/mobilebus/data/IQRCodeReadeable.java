package de.tum.score.transport4you.shared.mobilebus.data;

import java.security.PublicKey;

import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeNotDeserializableException;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeStringNotSetException;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;

/**
 * Interface responsible for extracting and verifying an ETicket from a String
 * The QRCode should code
 * * The Serialized version of an ETicket
 * * The signature = RSA( Hash( Serialized string))
 * @author Cyril
 *
 */
public interface IQRCodeReadeable {
	
	/**
	 * Set the String that codes the QR-Code
	 * @param s The QR-Code String read from the camera
	 */
	public void deserialize(String s) throws QRCodeNotDeserializableException;
	
	/**
	 * Get the ETicket represented by the QRCode
	 * @return
	 */
	public ETicket getETicket() throws QRCodeStringNotSetException;
	
	/**
	 * Verify signature = Compares the hash of the ETicket with the decryption of the signature
	 * @param k
	 * @return true if the signature is valid
	 */
	public boolean validSignature(PublicKey k) throws QRCodeStringNotSetException;

}
