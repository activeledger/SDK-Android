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
package com.example.activeledgersdk.utility;

import android.util.Log;

import com.example.activeledgersdk.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class ContractUploading {


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
            if(territorialityReference!=null)
                transaction.put("$territoriality",territorialityReference);

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

}
