package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;

import de.tum.score.transport4you.shared.mobilebus.data.IQRCodeReadeable;
import de.tum.score.transport4you.shared.mobilebus.data.IQRCodeWriteable;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeNotBoundToEticketException;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeNotDeserializableException;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeNotSignedException;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeStringNotSetException;

public class QRCode implements IQRCodeReadeable, IQRCodeWriteable{
	private ETicket eticket;
	private SignedObject signature;
	
	private String serializedEticket;
	private String serializedSignature;
	
	private String qrCodeString; // = ETicketSerialised + signature
	
	@Override
	public void deserialize(String s) throws QRCodeNotDeserializableException {
		String [] both = s.split("|||");
		serializedEticket = both[0];
		serializedSignature = both[1];
		try {
		     byte b[] = serializedEticket.getBytes("ISO-8859-1"); 
		     ByteArrayInputStream bi = new ByteArrayInputStream(b);
		     ObjectInputStream si = new ObjectInputStream(bi);
		     this.eticket = (ETicket) si.readObject();
		     
		     byte b2[] = serializedSignature.getBytes("ISO-8859-1"); 
		     ByteArrayInputStream bi2 = new ByteArrayInputStream(b2);
		     ObjectInputStream si2 = new ObjectInputStream(bi2);
		     this.signature = (SignedObject) si2.readObject();
		     
		 } catch (Exception e) {
		     System.out.println(e);
		 }
	}

	@Override
	public ETicket getETicket() throws QRCodeStringNotSetException {
		return this.eticket;
	}

	@Override
	public boolean validSignature(PublicKey k) throws QRCodeStringNotSetException {
		try {
			return signature.verify(k, Signature.getInstance("RSA","BC"));
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void setETicket(ETicket e) {
		this.eticket = e;
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);
			so.writeObject(e);
			so.flush();
			this.serializedEticket = bo.toString("ISO-8859-1");
		} catch (Exception exc) {
			System.out.println(e);
		}
	}

	@Override
	public void signETicket(PrivateKey privateKey) {
		
		try {
			// Sig = RSA (SHA1 hash)
			signature = new SignedObject(hashTicket(), privateKey, Signature.getInstance("RSA","BC"));
			
			// Serialize signature
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);
			so.writeObject(signature);
			so.flush();
			this.serializedSignature = bo.toString("ISO-8859-1");
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException | NoSuchProviderException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Eticket + signature => we can get our QRCode representation
		concatenate();
	}
	
	private String hashTicket(){
		MessageDigest crypt;
		try {
			crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(this.serializedEticket.getBytes("UTF-8"));
			
			return new BigInteger(1, crypt.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void concatenate(){
		qrCodeString = serializedEticket+"|||"+this.serializedSignature; 
		
	}

	@Override
	public String serialize() throws QRCodeNotBoundToEticketException, QRCodeNotSignedException {
		return qrCodeString;
	}
}
