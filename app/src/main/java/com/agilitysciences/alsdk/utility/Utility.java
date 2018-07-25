package com.agilitysciences.alsdk.utility;

import android.content.Context;
import android.util.Log;

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

//import org.bouncycastle.util.io.pem.PemObject;
//import org.bouncycastle.util.io.pem.PemWriter;

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
}
