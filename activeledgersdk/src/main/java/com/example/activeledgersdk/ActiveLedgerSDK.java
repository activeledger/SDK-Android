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
package com.example.activeledgersdk;

import android.content.Context;
import android.util.Log;

import com.example.activeledgersdk.key.KeyGenApi;
import com.example.activeledgersdk.model.Territoriality;
import com.example.activeledgersdk.onboard.OnboardIdentity;
import com.example.activeledgersdk.utility.ContractUploading;
import com.example.activeledgersdk.utility.KeyType;
import com.example.activeledgersdk.utility.Utility;

import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ActiveLedgerSDK {

    public static KeyType keyType;
    public static String KEYNAME = "AwesomeKey";
    private static ActiveLedgerSDK instance = null;
    private KeyPair keyPair;


    public static synchronized ActiveLedgerSDK getInstance() {
        if (instance == null)
            instance = new ActiveLedgerSDK();
        return instance;
    }

    // function takes trnascation JSON object, sigs JSON object and self sign flag) and creates and return an onboard transaction
    public static JSONObject createBaseTransaction(JSONObject $tx, Boolean selfsign, JSONObject
            $sigs) {
        return ContractUploading.createBaseTransaction(null, $tx, selfsign, $sigs);
    }

    // this method can be used to sign a message using private key
    public static String signMessage(byte[] message, KeyPair keyPair, KeyType type,String identifier) {
        try {
            return OnboardIdentity.signMessage(message, keyPair, keyType, identifier);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    // by given a file name this function reads the file from application directory and returns content as String.
    public static String readFileAsString(String fileName) throws IOException {
        return Utility.getInstance().readFileAsString(fileName);
    }

    // base method that has to be called before using SDK
    public void initSDK(Context context, String protocol, String url, String port) {
        Utility.getInstance().initSDK(context, protocol, url, port);
    }

    // function generates and set the default keypair of the SDK
    public Observable<KeyPair> generateAndSetKeyPair(KeyType keyType, boolean saveKeysToFile, String identifier) {

        KeyGenApi keyGenApi = new KeyGenApi();
        setKeyType(keyType);
        return Observable.just(keyGenApi.generateKeyPair(keyType, saveKeysToFile,identifier));
    }

    // creates an onboard transaction and execute the http request to the ledger
    public Observable<String> onBoardKeys(KeyPair keyPair, String keyName, String identifier) {

        KEYNAME = keyName;
        JSONObject transaction = OnboardIdentity.getInstance().onboard(keyPair, getKeyType(),identifier);

        String transactionJson = Utility.getInstance().convertJSONObjectToString(transaction);

        Log.e("Onboard Transaction", transactionJson);
        return executeTransaction(transactionJson);
    }

    // this method is used to an http request and execute a transaction over the ledger
    public Observable<String> executeTransaction(String transactionJson) {

        return HttpClient.getInstance().sendTransaction(transactionJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    // does an HTTP hit and return territoriality details
    public Observable<Territoriality> getTerritorialityStatus() {

        return HttpClient.getInstance().getTerritorialityStatus()
                .flatMap(new Function<String, Observable<Territoriality>>() {
                    @Override
                    public Observable<Territoriality> apply(String s) throws Exception {
                        JSONObject jsonObject = new JSONObject(s);


                        Territoriality territorialityObj = new Territoriality();
                        territorialityObj.setStatus(jsonObject.getString("status"));
                        territorialityObj.setReference(jsonObject.getString("reference"));
                        territorialityObj.setLeft(jsonObject.getString("left"));
                        territorialityObj.setRight(jsonObject.getString("right"));
                        territorialityObj.setPem(jsonObject.getString("pem"));

                        Iterator<String> keys = jsonObject.getJSONObject("neighbourhood").getJSONObject("neighbours").keys();


                        List<String> neighbours = new ArrayList<>();
                        while (keys.hasNext()) {
                            neighbours.add(keys.next());
                        }
                        territorialityObj.setNeighbours(neighbours);

                        return Observable.just(territorialityObj);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }


    //method used to retrieve the transaction data using id
    public Observable<String> getTransactionData(String id) {
        return HttpClient.getInstance().getTransactionData(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
