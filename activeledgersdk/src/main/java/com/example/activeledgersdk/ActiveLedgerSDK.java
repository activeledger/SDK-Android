package com.example.activeledgersdk;

import android.content.Context;

import com.example.activeledgersdk.API.ExecuteTransactionAPI;
import com.example.activeledgersdk.API.OnboardAPI;
import com.example.activeledgersdk.Interface.OnTaskCompleted;
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
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutionException;

public class ActiveLedgerSDK {

    private static ActiveLedgerSDK instance = null;

    public static KeyType keyType;
    public static String KEYNAME = "AwesomeKey";



    public static synchronized ActiveLedgerSDK getInstance(){
        if(instance == null)
            instance = new ActiveLedgerSDK();
        return instance;
    }

    public void initSDK(Context context,String protocol, String url, String port){
        Utility.getInstance().initSDK(context,protocol,url,port);
    }


    public KeyPair generateAndSetKeyPair(KeyType keyType, boolean saveKeysToFile){

            KeyGenApi keyGenApi = new KeyGenApi();
            setKeyType(keyType);
           return keyGenApi.generateKeyPair(keyType,saveKeysToFile);


    }

    public void onBoardKeys(KeyPair keyPair, String keyName,OnTaskCompleted listener){
        KEYNAME = keyName;
        JSONObject transaction= OnboardIdentity.getInstance().onboard(keyPair, getKeyType());

        String transactionJson = Utility.getInstance().convertJSONObjectToString(transaction);

        OnboardAPI onboardAPI = new OnboardAPI(transactionJson, Utility.getInstance().getContext(), listener);
        onboardAPI.execute();
    }

    public void setKeyType(KeyType keyType){
        this.keyType = keyType;
    }

    public KeyType getKeyType(){
        return keyType;
    }


    public static JSONObject createBaseTransaction(JSONObject $tx, Boolean selfsign, JSONObject $sigs) {
        return ContractUploading.createBaseTransaction($tx, selfsign, $sigs);
    }

    public static String signMessage(byte[] message, KeyPair keyPair ,KeyType type) {
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


    public static String executeTransaction(String json){
        ExecuteTransactionAPI executeTransactionAPI = new ExecuteTransactionAPI(json , Utility.getInstance().getContext());
        try {
            return executeTransactionAPI.execute().get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String readFileAsString(String fileName) throws IOException
    {
        return Utility.getInstance().readFileAsString(fileName);
    }



}
