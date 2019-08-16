/*
 * MIT License (MIT)
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.activeledgersdk.onboard;


import com.example.activeledgersdk.ActiveLedgerSDK;
import com.example.activeledgersdk.utility.KeyType;
import com.example.activeledgersdk.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class OnboardIdentity {

    private static OnboardIdentity instance = null;

    public static synchronized OnboardIdentity getInstance() {
        if (instance == null) {
            instance = new OnboardIdentity();
        }
        return instance;
    }

    // use this method to sign a transaction using private key
    public static String signMessage(byte[] message, KeyPair keyPair, KeyType type, String identifier) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {

        Signature sign = null;
        KeyFactory factory = null;

        if (type == KeyType.RSA) {
            sign = Signature.getInstance("SHA256withRSA", "BC");
            factory = KeyFactory.getInstance("RSA", "BC");
        } else {
            sign = Signature.getInstance("SHA256withECDSA", "BC");
            factory = KeyFactory.getInstance("EC", "BC");

        }

        try {
            sign.initSign(Utility.generatePrivateKeyFromFile(Utility.getPrivateKeyFileName(identifier), type));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//		  sign.initSign(Utility.generatePrivateKeyFromFile(factory,Utility.PRIVATEKEY_FILE));
//        sign.initSign(keyPair.getPrivate());

        sign.update(message);
        byte[] signature = sign.sign();
        System.out.println("Sign byte::::::::" + signature.toString());

        String s = android.util.Base64.encodeToString(signature, 16);
        System.out.println("Signautre::::::::" + s);


        return s;
    }

    // this method return the onboard transaction as JSON object
    public JSONObject onboard(KeyPair keyPair, KeyType type, String identifier) {

        JSONObject transaction = new JSONObject();
        JSONObject $sigs = new JSONObject();
        JSONObject identity = new JSONObject();
        JSONObject $i = new JSONObject();
        JSONObject $tx = new JSONObject();

        try {

            String pubKey = null;
            try {
                pubKey = Utility.readFileAsString(Utility.getPublicKeyFileName(identifier));
                System.out.println("public:::" + pubKey.toString());
                String priKey = Utility.readFileAsString(Utility.getPrivateKeyFileName(identifier));
                System.out.println("private:::" + priKey.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


            $tx.put("$contract", "onboard");
            $tx.put("$namespace", "default");

            identity.put("publicKey", pubKey);

            if (type == KeyType.RSA)
                identity.put("type", "rsa");
            else if (type == KeyType.EC)
                identity.put("type", "secp256k1");

            $i.put(ActiveLedgerSDK.KEYNAME, identity);
            $tx.put("$i", $i);


            try {

                String signTransactionObject = Utility.getInstance().convertJSONObjectToString($tx);
                $sigs.put(ActiveLedgerSDK.KEYNAME, signMessage(signTransactionObject.getBytes(), keyPair, type, identifier));

            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to sign object:" + e.getMessage());
            }


            transaction.put("$tx", $tx);
            transaction.put("$selfsign", true);
            transaction.put("$sigs", $sigs);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return transaction;
    }


}
