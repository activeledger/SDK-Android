package com.example.activeledgersdk.API;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.activeledgersdk.Interface.OnTaskCompleted;
import com.example.activeledgersdk.R;
import com.example.activeledgersdk.utility.PreferenceManager;
import com.example.activeledgersdk.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OnboardAPI extends AsyncTask<String, String, String> {

    private OnTaskCompleted listener;


    public String json;
    public Context context;

    public  String onboard_id;
    public  String onboard_name;
    public OnboardAPI(String json,Context context,OnTaskCompleted listener){
        this.json = json;
        this.context = context;
        this.listener=listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {


        Log.e("---json----",json);

//        json = "{\"$selfsign\": true,\"$sigs\": {\"identity\": \"MEQCIBymAYk2UcDrpqRAV88lznvG5PxuGhUM+s3+wOzYCuwoAiBAqaQ7pN2foPqAOGr+SwL2Xs+vg4L6poOtILh4KYNNwA==\"},\"$tx\": {\"$contract\": \"onboard\",\"$namespace\": \"default\",\"$i\": {\"identity\": {\"publicKey\": \"-----BEGIN PUBLIC KEY-----\\nMFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEzSsRFvLAALl6R7p2myK8vU8OzwiQ8Xoq\\nuTG+5nCJWYZu5mJLnlItlkxXv7K/Jp66WhROB6weEUTS1aNAS5e11Q==\\n-----END PUBLIC KEY-----\\n\",\"type\": \"secp256k1\"}}}}";
//
//
//        Log.e("---json----",json);


        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        URL url= null;
        try {
//             url = new URL("http://127.0.0.1:5260");
//            url = new URL("http://10.0.2.2:5260");
//            url = new URL("http://35.195.221.172:5260");
//            url = new URL(context.getString(R.string.url));
            url = new URL(Utility.getInstance().getHTTPURL());


        } catch (MalformedURLException e) {
            Log.e("URL error",e.getMessage());
        }
        if(url == null){
            Log.e("url error","is null = "+url);
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type","application/json")
                .header("Content-Type","application/json")
                .post(body)
                .build();

        try {
            Log.e("req-->",""+request.toString());
            Response response = client.newCall(request).execute();

            String jsonData = response.body().string();
            Log.e("hit response", jsonData);




            return jsonData;
        } catch (IOException e) {
            Log.e("http error",e.getMessage());
        }

        return null;
    }


    private void extractID(String response) throws JSONException {
        JSONObject Jobject = new JSONObject(response);
        String name = Jobject.optString("$streams");
        Log.e("stream", name);

        JSONObject JobjectName = new JSONObject(name);
        JSONArray jsonArray = JobjectName.getJSONArray("new");
        JSONObject idObj = jsonArray.getJSONObject(0);

        onboard_id = idObj.optString("id");
        onboard_name = idObj.optString("name");

        PreferenceManager.getInstance().saveString(context.getString(R.string.onboard_id),onboard_id);
        PreferenceManager.getInstance().saveString(context.getString(R.string.onboard_name),onboard_name);


        Log.e("id", PreferenceManager.getInstance().getStringValueFromKey(context.getString(R.string.onboard_id)));
        Log.e("name",  PreferenceManager.getInstance().getStringValueFromKey(context.getString(R.string.onboard_name)));
    }

    protected void onPostExecute(String jsonData){

        try {
            extractID(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onTaskCompleted();
    }


}