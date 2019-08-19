package com.example.activeledgersdk;

import android.util.Log;

import com.example.activeledgersdk.utility.KeyType;
import com.example.activeledgersdk.utility.PreferenceManager;
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


    public static JSONObject registerNamespaceTransaction(String namespace, String identityStream, String identifier) {

        JSONObject transaction = new JSONObject();

        JSONObject $tx = registerNamespaceTransactionObject(namespace, identityStream);
        try {

            transaction.put("$tx", $tx);

            JSONObject $sigs = new JSONObject();
            String signTransactionObject = Utility.getInstance().convertJSONObjectToString($tx);
            String signature = ActiveLedgerSDK.getInstance().signMessage(signTransactionObject.getBytes(), ActiveLedgerSDK.getInstance().getKeyPair(), ActiveLedgerSDK.getInstance().getKeyType(), identifier);
            $sigs.put(identityStream, signature);
            transaction.put("$sigs", $sigs);

            Log.e("Namespace Transaction", transaction.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return transaction;
    }


    public static JSONObject smartContractDeploymentTransactionObject(String version, String namespace, String name, String base64TSContract, String identityStream) {

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

    // this method can be used to create a transaction that uploads a contracr to the ledger
    public static JSONObject createContractUploadTransaction(KeyType keyType) {
        JSONObject transaction = new JSONObject();

        try {
            JSONObject $tx = new JSONObject();
            $tx.put("$namespace", "default");
            $tx.put("$contract", "contract");
            JSONObject $i = new JSONObject();
            JSONObject identity = new JSONObject();
            identity.put("version", "<version>");
            identity.put("namespace", "<namespace>");
            identity.put("name", "<name>");
            identity.put("contract", "<base64 encoded smart contract>");
            $i.put(PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.onboard_id)), identity);
            $tx.put("$i", $i);
            transaction.put("$tx", $tx);

            String signature = null;
//        signature = OnboardIdentity.signMessage(gson.toJson($tx).getBytes("UTF-8"),keyType);
//        Log.e("new ---->",signature);

            JSONObject $sigs = new JSONObject();
            $sigs.put(PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.onboard_id)), signature);


            transaction.put("$sigs", $sigs);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return transaction;
    }


    public static JSONObject createFundsTransferTransaction() {

        JSONObject transaction = new JSONObject();
        try {


            JSONObject $tx = new JSONObject();
            $tx.put("$namespace", "default");
            $tx.put("$contract", "fund");
            $tx.put("$entry", "transfer");
            JSONObject $i = new JSONObject();
            JSONObject identity = new JSONObject();
            identity.put("symbol", "B$");
            identity.put("amount", 5);
            $i.put(PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.onboard_id)), identity);
            $tx.put("$i", $i);
            JSONObject $o = new JSONObject();
            JSONObject outputIdentity = new JSONObject();
            outputIdentity.put("amount", 5);
            $o.put("output identity", outputIdentity);
            $tx.put("$o", $o);
            transaction.put("$tx", $tx);


            String signature = null;
//        signature = OnboardIdentity.signMessage(gson.toJson($tx).getBytes("UTF-8"),keyType);
//        Log.e("new ---->",signature);

            JSONObject $sigs = new JSONObject();
            $sigs.put(PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.onboard_id)), signature);


            transaction.put("$sigs", $sigs);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return transaction;
    }

    // method creates the basic transaction object this is included in a transaction
    public static JSONObject createTXObject(String namespace, String contract, String entry, JSONObject $i, JSONObject $o, JSONObject $r) {

        JSONObject $tx = new JSONObject();

        try {
            $tx.put("$namespace", namespace);

            $tx.put("$contract", contract);
            if (entry != null)
                $tx.put("$entry", entry);
            if ($i != null)
                $tx.put("$i", $i);
            if ($o != null)
                $tx.put("$o", $o);
            if ($r != null)
                $tx.put("$r", $r);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return $tx;
    }


    // this method creates the basic transaction that can save time while creating transactions
    public static JSONObject createBaseTransaction(String territorialityReference, JSONObject $tx, Boolean selfsign, JSONObject $sigs) {

        JSONObject transaction = new JSONObject();


        try {
            if (territorialityReference != null)
                transaction.put("$territoriality", territorialityReference);

            transaction.put("$tx", $tx);

            if (selfsign != null)
                transaction.put("$selfsign", selfsign);
            if ($sigs != null)
                transaction.put("$sigs", $sigs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Base Transaction", transaction.toString());


        return transaction;
    }


    public static JSONObject smartContractDeploymentTransaction(String version, String namespace, String name, String base64TSContract, String identityStream, String identifier) {

        JSONObject transaction = new JSONObject();

        JSONObject $tx = smartContractDeploymentTransactionObject(version, namespace, name, base64TSContract, identityStream);
        try {

            transaction.put("$tx", $tx);

            JSONObject $sigs = new JSONObject();
            String signTransactionObject = Utility.getInstance().convertJSONObjectToString($tx);
            String signature = ActiveLedgerSDK.getInstance().signMessage(signTransactionObject.getBytes(), ActiveLedgerSDK.getInstance().getKeyPair(), ActiveLedgerSDK.getInstance().getKeyType(), identifier);
            $sigs.put(identityStream, signature);
            transaction.put("$sigs", $sigs);

            Log.e("Namespace Transaction", transaction.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return transaction;

    }



}
