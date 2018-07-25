package com.agilitysciences.alsdk;

import android.os.AsyncTask;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.agilitysciences.alsdk.key.KeyGenApi;
import com.agilitysciences.alsdk.utility.Utility;

//import org.bouncycastle.crypto.CryptoException;

import org.spongycastle.crypto.CryptoException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECFieldFp;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;








import java.security.KeyFactory;
import java.security.KeyPairGenerator;

import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;


import org.spongycastle.asn1.sec.SECNamedCurves;



import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.EllipticCurve;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        signMessage();


        try {
            Utility.getInstance().setContext(this);
            KeyGenApi keyGenApi = new KeyGenApi();
            keyGenApi.generateKeyPair();



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (CryptoException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }



    }



    public void signMessage(){

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                            "key1",
                            KeyProperties.PURPOSE_SIGN)
                            .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                            .setDigests(KeyProperties.DIGEST_SHA256,
                                    KeyProperties.DIGEST_SHA384,
                                    KeyProperties.DIGEST_SHA512)
                            // Only permit the private key to be used if the user authenticated
                            // within the last five minutes.
//                            .setUserAuthenticationRequired(true)
//                            .setUserAuthenticationValidityDurationSeconds(5 * 60)
                            .build());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(keyPair.getPrivate());


            // The key pair can also be obtained from the Android Keystore any time as follows:
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey("key1", null);
            PublicKey publicKey = keyStore.getCertificate("key1").getPublicKey();

//            Log.e("Private key = ",java.util.Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
            Log.e("Public key = ",java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded()));


            String messagestr = "hello world";
            signature.update(messagestr.getBytes("UTF-8"));

            byte[] signature2 = signature.sign();
            String s2= java.util.Base64.getEncoder().encodeToString(signature2);
            System.out.println("Signautre2::::::::"+s2);

            byte[] signature3 = signature.sign();
            String s3= java.util.Base64.getEncoder().encodeToString(signature3);
            System.out.println("Signautre3::::::::"+s3);


        }
        catch(Exception e){
            e.printStackTrace();
//            Log.e("Exception", e.getMessage());
        }

    }






    KeyFactory kf;
    KeyPairGenerator kpg;

    synchronized KeyPair generateKeyPairParams(ECParams ecp) throws Exception {
        EllipticCurve curve = toCurve(ecp);
        ECParameterSpec esSpec = new ECParameterSpec(curve, ecp.getG(),
                ecp.getN(), ecp.h);

        kpg.initialize(esSpec);

        return kpg.generateKeyPair();
    }


      synchronized KeyPair generateKeyPairNamedCurve(String curveName)
            throws Exception {
        ECGenParameterSpec ecParamSpec = new ECGenParameterSpec(curveName);
        kpg.initialize(ecParamSpec);

        return kpg.generateKeyPair();
    }


    static EllipticCurve toCurve(ECParams ecp) {
        ECFieldFp fp = new ECFieldFp(ecp.getP());

        return new EllipticCurve(fp, ecp.getA(), ecp.getB());
    }




}
