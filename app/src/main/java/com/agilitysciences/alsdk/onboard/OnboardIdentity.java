package com.agilitysciences.alsdk.onboard;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.agilitysciences.alsdk.utility.KeyType;
import com.agilitysciences.alsdk.utility.PemFile;
import com.agilitysciences.alsdk.utility.Utility;
import com.google.gson.Gson;

public class OnboardIdentity {

    private static OnboardIdentity instance=null;

    public static synchronized OnboardIdentity getInstance(){
        if(instance==null){
             instance = new OnboardIdentity();
        }
        return instance;
    }

	
	public Transaction onboard(KeyPair keyPair, KeyType type ) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException, InvalidKeySpecException
	{
		Transaction transaction=new Transaction();
		IdentityList i=new IdentityList();
		Identity identity=new Identity();
		if(type == KeyType.RSA)
		identity.setType("rsa");
		else if(type == KeyType.EC)
			identity.setType("secp256k1");
		
		String pubKey= Utility.readFileAsString(Utility.PUBLICKEY_FILE);
		System.out.println("public:::"+pubKey.toString());
		identity.setPublicKey(pubKey);

		i.setIdentity(identity);
		Tx tx=new Tx();
		tx.setIdentity(i);
		
		tx.set$contract("onboard");
		tx.set$namespace("default");
		transaction.set$tx(tx);
		Gson gson = new Gson();
        String json = gson.toJson(tx);


		com.agilitysciences.alsdk.onboard.Signature sig=new com.agilitysciences.alsdk.onboard.Signature();
		sig.setIdentity(signMessage(json.getBytes("UTF-8"), keyPair,type));
		
		transaction.set$selfsign(true);
		transaction.setSig(sig);

		Log.e("JSON",json.toString());
		return transaction;
	}
	
	  public static String signMessage(byte[] message, KeyPair keyPair, KeyType type) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException, InvalidKeySpecException {
		  Signature sign =null;
		  KeyFactory factory = null;
          Signature rsaVerify;
		  if(type == KeyType.RSA)
		  {
			   sign = Signature.getInstance("SHA256withRSA", "BC");
			   factory = KeyFactory.getInstance("RSA", "BC");
              rsaVerify = Signature.getInstance("SHA256withRSA", "BC");
		  }
		  else
		  {
			  sign = Signature.getInstance("SHA256withECDSA", "SC");
			   factory = KeyFactory.getInstance("ECDSA", "SC");
              rsaVerify = Signature.getInstance("ECDSA", "SC");

		  }

		  sign.initSign(generatePrivateKey(factory,Utility.PRIVATEKEY_FILE));
		  sign.update(message);
  	      byte[] signature = sign.sign();
	      String s=Base64.getEncoder().encodeToString(signature);
	       System.out.println("Signautre::::::::"+s);


	        return s;
	    }

		private static PrivateKey generatePrivateKey(KeyFactory factory, String filename) throws InvalidKeySpecException, IOException, NoSuchProviderException, NoSuchAlgorithmException {

			String filePath = Utility.getInstance().getFilePath(filename);
            Log.e("File Reading","file path = "+filePath);
            File f = new File(filePath);
			PemFile pemFile = new PemFile(f);
			byte[] content = pemFile.getPemObject().getContent();
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
			return factory.generatePrivate(privKeySpec);


		}

		private static PublicKey generatePublicKey(KeyFactory factory, String filename) throws InvalidKeySpecException, IOException {
            String filePath = Utility.getInstance().getFilePath(filename);
            Log.e("File Reading","file path = "+filePath);
            File f = new File(filePath);
	        PemFile pemFile = new PemFile(f);
			byte[] content = pemFile.getPemObject().getContent();
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
			return factory.generatePublic(pubKeySpec);
		}
	
}
