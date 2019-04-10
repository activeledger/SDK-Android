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
package com.example.activeledgersdk.key;

import com.example.activeledgersdk.utility.KeyType;
import com.example.activeledgersdk.utility.Utility;

import org.spongycastle.crypto.DataLengthException;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;

public class KeyGenApi {

    // checks the validity of RSA keypair
    static boolean isValidRSAPair(KeyPair pair) {
        Key key = pair.getPrivate();
        if (key instanceof RSAPrivateCrtKey) {
            RSAPrivateCrtKey pvt = (RSAPrivateCrtKey) key;
            BigInteger e = pvt.getPublicExponent();
            RSAPublicKey pub = (RSAPublicKey) pair.getPublic();
            return e.equals(pub.getPublicExponent()) &&
                    pvt.getModulus().equals(pub.getModulus());
        } else {
            throw new IllegalArgumentException("Not a CRT RSA key.");
        }
    }

    // generates RSC/EC keypair
    public KeyPair generateKeyPair(KeyType keyType, boolean saveKeysToFile, String identifier) {

        KeyPair keyPair = null;
        try {

            Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());


            if (keyType == KeyType.RSA) {
                keyPair = createRSAKeyPair();
            } else {
                keyPair = createSecp256k1KeyPair();
            }


            PrivateKey priv = keyPair.getPrivate();
            PublicKey pub = keyPair.getPublic();

            System.out.println("Format pri key:" + priv.getFormat());
            System.out.println("Format pub key:" + pub.getFormat());


            try {
                Utility.writePem(Utility.getPublicKeyFileName(identifier), "PUBLIC KEY", pub);

                if (saveKeysToFile) {

                    Utility.writePem(Utility.getPrivateKeyFileName(identifier), "PRIVATE KEY", priv);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (DataLengthException e) {
            e.printStackTrace();
        }

        return keyPair;
    }

    public KeyPair createSecp256k1KeyPair() {

        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC");

            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return keyPairGenerator.generateKeyPair();

    }

    private KeyPair createRSAKeyPair() {

        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA", "SC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }


}

