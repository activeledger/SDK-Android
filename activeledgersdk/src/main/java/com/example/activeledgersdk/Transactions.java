package com.example.activeledgersdk;

import android.util.Log;

import com.example.activeledgersdk.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class Transactions {


    public static JSONObject registerNamespaceTransactionObject(String namespace, String identityStream) {

        JSONObject $tx = new JSONObject();
        JSONObject $i = new JSONObject();
        JSONObject IdentityStream = new JSONObject();

        try {
            $tx.put("$namespace", "default");
            $tx.put("$contract", "namespace");
            IdentityStream.put("namespace", namespace);
            $i.put(identityStream, IdentityStream);

            $tx.put("$i", $i);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return $tx;
    }

    public static JSONObject registerNamespaceTransaction(String namespace, String identityStream) {

        JSONObject transaction = new JSONObject();

        JSONObject $tx = registerNamespaceTransactionObject(namespace, identityStream);
        try {

            transaction.put("$tx", $tx);

            JSONObject $sigs = new JSONObject();
            String signTransactionObject = Utility.getInstance().convertJSONObjectToString($tx);
            String signature = ActiveLedgerSDK.getInstance().signMessage(signTransactionObject.getBytes(),ActiveLedgerSDK.getInstance().getKeyPair(),ActiveLedgerSDK.getInstance().getKeyType());
            $sigs.put(identityStream, signature);
            transaction.put("$sigs", $sigs);

            Log.e("Namespace Transaction", transaction.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return transaction;
    }


    public static JSONObject smartContractDeploymentTransactionObject(String version, String namespace, String name,String base64TSContract,String identityStream){

        JSONObject $tx = new JSONObject();
        JSONObject $i = new JSONObject();
        JSONObject IdentityStream = new JSONObject();

        try {
            $tx.put("$namespace", "default");
            $tx.put("$contract", "contract");
            IdentityStream.put("version", version);
            IdentityStream.put("namespace", namespace);
            IdentityStream.put("name", name);
            IdentityStream.put("contract", base64TSContract);
            $i.put(identityStream, IdentityStream);

            $tx.put("$i", $i);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return $tx;

    }


    public static JSONObject smartContractDeploymentTransaction(String version, String namespace, String name,String base64TSContract, String identityStream){

        JSONObject transaction = new JSONObject();

        JSONObject $tx = smartContractDeploymentTransactionObject(version,namespace,name,base64TSContract, identityStream);
        try {

            transaction.put("$tx", $tx);

            JSONObject $sigs = new JSONObject();
            String signTransactionObject = Utility.getInstance().convertJSONObjectToString($tx);
            String signature = ActiveLedgerSDK.getInstance().signMessage(signTransactionObject.getBytes(),ActiveLedgerSDK.getInstance().getKeyPair(),ActiveLedgerSDK.getInstance().getKeyType());
            $sigs.put(identityStream, signature);
            transaction.put("$sigs", $sigs);

            Log.e("Namespace Transaction", transaction.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return transaction;

    }


}
