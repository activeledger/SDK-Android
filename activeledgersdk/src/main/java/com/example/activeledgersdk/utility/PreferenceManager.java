package com.example.activeledgersdk.utility;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {

    static final String PREF_NAME = "SDK Pref File";
    static Context context;
    private static PreferenceManager instance = null;
    SharedPreferences sharedPref;

    public static synchronized PreferenceManager getInstance() {
        if (instance == null) {
            instance = new PreferenceManager();
        }
        return instance;
    }

    public void init() {
        Context context = Utility.getInstance().getContext();
        sharedPref = context.getSharedPreferences(
                PREF_NAME, Context.MODE_PRIVATE);

    }

    //save a string to preferences
    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void saveFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void saveLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public String getStringValueFromKey(String key) {
        return sharedPref.getString(key, null);
    }

    public long getLongValueFromKey(String key) {
        return sharedPref.getLong(key, 0);
    }

    public boolean getBooleanValueFromKey(String key) {
        return sharedPref.getBoolean(key, false);
    }

    public int getIntValueFromKey(String key) {
        return sharedPref.getInt(key, 0);
    }

    public float getFloatValueFromKey(String key) {
        return sharedPref.getFloat(key, 0);
    }

}
