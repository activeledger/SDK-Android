package com.example.activeledgersdk.utility;

import android.content.Context;
import android.util.Log;

import com.example.activeledgersdk.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class Utility {

    private static Utility instance=null;
	private static PemObject pemObject;
	static Context context;

	public static String PRIVATEKEY_FILE = "priv-key.pem";
    public static String PUBLICKEY_FILE = "pub-key.pem";


    public static synchronized Utility getInstance(){
        if(instance==null){
            instance = new Utility();
        }
        return instance;
    }

	public  void setContext(Context context){
	this.context = context;
    }

    public Context getContext(){
	    return this.context;
    }


    public void initSDK(Context context,String protocol, String url, String port){

    	setContext(context);

		PreferenceManager.getInstance().init();


		PreferenceManager.getInstance().saveString(context.getString(R.string.protocol),protocol);
		PreferenceManager.getInstance().saveString(context.getString(R.string.ip),url);
		PreferenceManager.getInstance().saveString(context.getString(R.string.port),port);



	}

	public String getHTTPURL(){

		return PreferenceManager.getInstance().getStringValueFromKey(context.getString(R.string.protocol)) + "://"+
		PreferenceManager.getInstance().getStringValueFromKey(context.getString(R.string.ip)) + ":"+
		PreferenceManager.getInstance().getStringValueFromKey(context.getString(R.string.port));

	}


	@Override
	public String toString() {
		return "PemFile [pemObject=" + pemObject + "]";
	}

	public static void writePem(String filename,String description,Key key) throws FileNotFoundException, IOException {
        Log.e("File Writing","");
		String filePath = Utility.getInstance().getFilePath(filename);
        Log.e("File Writing","file path = "+filePath);

        File f = new File(filePath);
		PemWriter pemWriter = new PemWriter(new FileWriter(f));
		try {
			pemObject = new PemObject(description, key.getEncoded());
			pemWriter.writeObject(pemObject);
		} finally {
			pemWriter.close();
		}

	}
	
	public static String readFileAsString(String fileName) throws IOException
	{
        Log.e("File Reading","");
        String filePath = Utility.getInstance().getFilePath(fileName);
        Log.e("File Reading","file path = "+filePath);
        File f = new File(filePath);
		InputStream is = new FileInputStream(f);
		BufferedReader buf = new BufferedReader(new InputStreamReader(is)); 
		String line = buf.readLine(); 
		StringBuilder sb = new StringBuilder();
		while(line != null)
		{ 
			sb.append(line).append("\n"); 
			line = buf.readLine(); 
		} 
		String fileAsString = sb.toString();
		return fileAsString;
	}

	public String getFilePath(String filename){
       return getContext().getFilesDir().getPath().toString() + "/"+filename;
    }



	public PrivateKey getPrivateKeyFromPreference(){
		String privKeyStr = PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.private_key));
		byte[] sigBytes = Base64.decode(privKeyStr);
//		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
		KeyFactory keyFact = null;
		try {
			keyFact = KeyFactory.getInstance("RSA", "SC");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		try {
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(sigBytes);
			return  keyFact.generatePrivate(privKeySpec);

//			return  keyFact.generatePrivate(x509KeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}




	public PublicKey getPublicKeyFromPreference(){
		String pubKeyStr = PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.public_key));
		byte[] sigBytes = Base64.decode(pubKeyStr);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
		KeyFactory keyFact = null;
		try {
			keyFact = KeyFactory.getInstance("RSA", "SC");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		try {
			return  keyFact.generatePublic(x509KeySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

    public static PrivateKey generatePrivateKeyFromFile(String filename, KeyType type) throws InvalidKeySpecException, IOException, NoSuchProviderException, NoSuchAlgorithmException {

		KeyFactory factory = null;

		if(type == KeyType.RSA)
		{
			factory = KeyFactory.getInstance("RSA", "BC");
		}
		else
		{
			factory = KeyFactory.getInstance("EC", "BC");
		}

        String filePath = Utility.getInstance().getFilePath(filename);
        Log.e("File Reading","file path = "+filePath);
        File f = new File(filePath);
        PemFile pemFile = new PemFile(f);
        byte[] content = pemFile.getPemObject().getContent();
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
        return factory.generatePrivate(privKeySpec);


    }

    public static PublicKey generatePublicKeyFromFile(String filename, KeyType type) throws InvalidKeySpecException, IOException, NoSuchProviderException, NoSuchAlgorithmException {

		KeyFactory factory = null;

		if(type == KeyType.RSA)
		{
			factory = KeyFactory.getInstance("RSA", "BC");
		}
		else
		{
			factory = KeyFactory.getInstance("EC", "BC");
		}

        String filePath = Utility.getInstance().getFilePath(filename);
        Log.e("File Reading","file path = "+filePath);
        File f = new File(filePath);
        PemFile pemFile = new PemFile(f);
        byte[] content = pemFile.getPemObject().getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        return factory.generatePublic(pubKeySpec);
    }




    public String convertObjectToString(Object object) throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	public String convertByteArrayToString(byte[] byteData) throws JsonProcessingException {

		return android.util.Base64.encodeToString(byteData, 16);
	}
	public byte[] convertStringToByteArray(String data){
    	return data.getBytes();
	}

	public String convertJSONObjectToString(JSONObject jsonObject){
    	return jsonObject.toString().replace("\\/","/");
	}

	public void extractID(String response) throws JSONException {
		JSONObject Jobject = new JSONObject(response);
		String name = Jobject.optString("$streams");
		Log.e("stream", name);

		JSONObject JobjectName = new JSONObject(name);
		JSONArray jsonArray = JobjectName.getJSONArray("new");
		JSONObject idObj = jsonArray.getJSONObject(0);


		PreferenceManager.getInstance().saveString(context.getString(R.string.onboard_id),idObj.optString("id"));
		PreferenceManager.getInstance().saveString(context.getString(R.string.onboard_name),idObj.optString("name"));


		Log.e("id", PreferenceManager.getInstance().getStringValueFromKey(context.getString(R.string.onboard_id)));
		Log.e("name",  PreferenceManager.getInstance().getStringValueFromKey(context.getString(R.string.onboard_name)));
	}



}
