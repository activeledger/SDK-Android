package com.agilitysciences.alsdk.key;



import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import com.agilitysciences.alsdk.onboard.OnboardAPI;
import com.agilitysciences.alsdk.onboard.OnboardIdentity;
import com.agilitysciences.alsdk.onboard.Transaction;
import com.agilitysciences.alsdk.utility.KeyType;
import com.agilitysciences.alsdk.utility.Utility;
import com.google.gson.Gson;

import org.spongycastle.crypto.CryptoException;
import org.spongycastle.crypto.DataLengthException;


public class KeyGenApi {


        public void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, FileNotFoundException, IOException, InvalidKeyException, SignatureException, InvalidKeySpecException, CryptoException, InvalidAlgorithmParameterException {

	    	try {
	    		String type="rsa";
				Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
				System.out.println("BouncyCastle provider added.");
	    		Transaction transaction=new Transaction();
	    		System.out.println("Type::::"+type);
	    		if(type.equalsIgnoreCase("rsa")) {
	    			
	    			KeyPair keyPair = createRSAKeyPair();
	    			PrivateKey priv =keyPair.getPrivate();
	    			PublicKey pub =  keyPair.getPublic();
	    			boolean valid=isValidRSAPair(keyPair);
	    			System.out.println("Format:"+priv.getFormat());
	    			Utility.writePem("priv-key.pem","PRIVATE KEY",priv);
					Utility.writePem("pub-key.pem","PUBLIC KEY",pub);
	                transaction=OnboardIdentity.getInstance().onboard(keyPair, KeyType.RSA);
	    		}
	    		else {
		    		KeyPair keyPair=createSecp256k1KeyPair();
					PrivateKey priv=keyPair.getPrivate();
					PublicKey pub=keyPair.getPublic();
					System.out.println("ec priv :::"+priv.getFormat());
					System.out.println("ec pub:::"+pub.getFormat());
					System.out.println("ec priv:::"+priv);
					System.out.println("ec pub:::"+pub);

					Log.e("EC pub key is ",  ""+ java.util.Base64.getEncoder().encodeToString(pub.getEncoded()));
		            Log.e("EC priv key is ",  ""+java.util.Base64.getEncoder().encodeToString(priv.getEncoded()));

					Utility.writePem(Utility.PRIVATEKEY_FILE,"PRIVATE KEY",priv);
					Utility.writePem(Utility.PRIVATEKEY_FILE,"PUBLIC KEY",pub);
				
					transaction =OnboardIdentity.getInstance().onboard(keyPair,KeyType.EC);
	    		}
	    		
		
                Gson gson = new Gson();
                System.out.println("Sig----"+transaction.getSig().getIdentity());
                String json = gson.toJson(transaction);
                System.out.println("Transaction:::"+json);
                
           
                try {
					OnboardAPI onboardAPI = new OnboardAPI(json, Utility.getInstance().getContext());
					onboardAPI.execute();

                }catch(Exception e)
                {
                	System.out.println("exception::");;
                	e.printStackTrace();
                }

	    		
			} 
	    	catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DataLengthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	

	        
	    }
		
		public KeyPair createSecp256k1KeyPair() throws NoSuchProviderException,NoSuchAlgorithmException, InvalidAlgorithmParameterException {


        	KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "SC");
//			 KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC");
			 ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");

			 keyPairGenerator.initialize(ecGenParameterSpec);
			 return keyPairGenerator.generateKeyPair();
		}
	
		private KeyPair createRSAKeyPair() throws NoSuchProviderException,NoSuchAlgorithmException, InvalidAlgorithmParameterException {

			 KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
			 keyPairGenerator.initialize(1024);
			 return keyPairGenerator.generateKeyPair();
		}
		
		static boolean isValidRSAPair(KeyPair pair)
		{
		  Key key = pair.getPrivate();
		  if (key instanceof RSAPrivateCrtKey) {
		    RSAPrivateCrtKey pvt = (RSAPrivateCrtKey) key;
		    BigInteger e = pvt.getPublicExponent();
		    RSAPublicKey pub = (RSAPublicKey) pair.getPublic();
		    return e.equals(pub.getPublicExponent()) && 
		      pvt.getModulus().equals(pub.getModulus());
		  }
		  else {
		    throw new IllegalArgumentException("Not a CRT RSA key.");
		  }
		}

	
		

		
	}

