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
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeSerializationFailedException;
import de.tum.score.transport4you.shared.mobilebus.data.error.QRCodeStringNotSetException;

public class QRCode implements IQRCodeReadeable, IQRCodeWriteable{
	private ETicket eticket;
	private SignedObject signedETicket;
	
	@Override
	public void deserialize(String s) throws QRCodeNotDeserializableException {
		try {
		     byte b[] = s.getBytes("ISO-8859-1"); 
		     ByteArrayInputStream bi = new ByteArrayInputStream(b);
		     ObjectInputStream si = new ObjectInputStream(bi);
		     this.signedETicket = (SignedObject) si.readObject();
		     this.eticket = (ETicket)signedETicket.getObject();
		     
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
			return signedETicket.verify(k, Signature.getInstance("SHA256WithRSA","BC"));
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void setETicket(ETicket e) {
		this.eticket = e;
	}

	@Override
	public void signETicket(PrivateKey privateKey) {
		
		try {
			// SignedObject = ETicket + Sig = RSA ( HASH ( ETicket ))
			signedETicket = new SignedObject(this.eticket, privateKey, Signature.getInstance("SHA256WithRSA","BC"));
			
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException | NoSuchProviderException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String serialize() throws QRCodeNotBoundToEticketException, QRCodeNotSignedException, QRCodeSerializationFailedException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try {
			ObjectOutputStream so = new ObjectOutputStream(bo);
			so.writeObject(signedETicket);
			so.flush();
			return bo.toString("ISO-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
			throw new QRCodeSerializationFailedException();
		}
	}
}
