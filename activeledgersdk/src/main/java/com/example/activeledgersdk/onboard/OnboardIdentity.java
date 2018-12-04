package com.example.activeledgersdk.onboard;



import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;


import com.example.activeledgersdk.ActiveLedgerSDK;
import com.example.activeledgersdk.utility.KeyType;
import com.example.activeledgersdk.utility.Utility;


import org.json.JSONException;
import org.json.JSONObject;

public class OnboardIdentity {

    private static OnboardIdentity instance=null;

    public static synchronized OnboardIdentity getInstance(){
        if(instance==null){
             instance = new OnboardIdentity();
        }
        return instance;
    }

	public JSONObject onboard(KeyPair keyPair ,KeyType type )
	{

		JSONObject transaction = new JSONObject();
		JSONObject $sigs = new JSONObject();
		JSONObject identity = new JSONObject();
		JSONObject $i = new JSONObject();
		JSONObject $tx = new JSONObject();

		try {

		String pubKey = null;
		try {
			pubKey = Utility.readFileAsString(Utility.PUBLICKEY_FILE);
			System.out.println("public:::" + pubKey.toString());
			String priKey = Utility.readFileAsString(Utility.PRIVATEKEY_FILE);
			System.out.println("public:::" + priKey.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}


		$tx.put("$contract","onboard");
		$tx.put("$namespace","default");
		identity.put("publicKey",pubKey);

		if(type == KeyType.RSA)
			identity.put("type","rsa");
		else if(type == KeyType.EC)
			identity.put("type","secp256k1");
		$i.put(ActiveLedgerSDK.KEYNAME,identity);
		$tx.put("$i",$i);


		try {

			String signTransactionObject = Utility.getInstance().convertJSONObjectToString($tx);
			$sigs.put(ActiveLedgerSDK.KEYNAME,signMessage(signTransactionObject.getBytes(),keyPair, type));

		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Unable to sign object:"+e.getMessage());
		}


		transaction.put("$tx",$tx);
		transaction.put("$selfsign",true);
		transaction.put("$sigs",$sigs);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return transaction;
	}
	
	  public static String signMessage(byte[] message, KeyPair keyPair ,KeyType type) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		  Signature sign =null;
		  KeyFactory factory = null;

		  if(type == KeyType.RSA)
		  {
			   sign = Signature.getInstance("SHA256withRSA", "BC");
			   factory = KeyFactory.getInstance("RSA", "BC");
		  }
		  else
		  {
			  sign = Signature.getInstance("SHA256withECDSA", "BC");
			   factory = KeyFactory.getInstance("EC", "BC");

		  }

//		  sign.initSign(Utility.generatePrivateKeyFromFile(factory,Utility.PRIVATEKEY_FILE));
		  sign.initSign(keyPair.getPrivate());

		  sign.update(message);
  	      byte[] signature = sign.sign();
		  System.out.println("Sign byte::::::::"+signature.toString());

		  String s= android.util.Base64.encodeToString(signature, 16);
	       System.out.println("Signautre::::::::"+s);




	        return s;
	    }


}
