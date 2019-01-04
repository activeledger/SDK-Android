package com.example.activeledgersdk;

import android.content.Context;

import com.example.activeledgersdk.key.KeyGenApi;
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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ActiveLedgerSDK {

    public static KeyType keyType;
    public static String KEYNAME = "AwesomeKey";
    private static ActiveLedgerSDK instance = null;

    public static synchronized ActiveLedgerSDK getInstance() {
        if (instance == null)
            instance = new ActiveLedgerSDK();
        return instance;
    }

    // function takes trnascation JSON object, sigs JSON object and self sign flag) and creates and return an onboard transaction
    public static JSONObject createBaseTransaction(JSONObject $tx, Boolean selfsign, JSONObject
            $sigs) {
        return ContractUploading.createBaseTransaction($tx, selfsign, $sigs);
    }

    // this method can be used to sign a message using private key
    public static String signMessage(byte[] message, KeyPair keyPair, KeyType type) {
        try {
            return OnboardIdentity.signMessage(message, keyPair, keyType);
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
    public Observable<KeyPair> generateAndSetKeyPair(KeyType keyType, boolean saveKeysToFile) {

        KeyGenApi keyGenApi = new KeyGenApi();
        setKeyType(keyType);
        return Observable.just(keyGenApi.generateKeyPair(keyType, saveKeysToFile));
    }

    // creates an onboard transaction and execute the http request to the ledger
    public Observable<String> onBoardKeys(KeyPair keyPair, String keyName) {

        KEYNAME = keyName;
        JSONObject transaction = OnboardIdentity.getInstance().onboard(keyPair, getKeyType());

        String transactionJson = Utility.getInstance().convertJSONObjectToString(transaction);
        return executeTransaction(transactionJson);
    }

    // this method is used to an http request and execute a transaction over the ledger
    public Observable<String> executeTransaction(String transactionJson) {

        return HttpClient.getInstance().sendTransaction(transactionJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }


}
