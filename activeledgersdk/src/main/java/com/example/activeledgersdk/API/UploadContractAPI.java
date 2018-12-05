package com.example.activeledgersdk.API;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.activeledgersdk.utility.Utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UploadContractAPI extends AsyncTask<String, String, String> {

    public String json;
    public Context context;

    public  String onboard_id;
    public  String onboard_name;
    public UploadContractAPI(String json, Context context){
        this.json = json;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {


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

            String jsonData = response.body().string().toString();
            Log.e("hit response", jsonData);



            return null;
        } catch (IOException e) {
            Log.e("http error",e.getMessage());
        }


        return null;
    }



}