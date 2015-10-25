package com.mdareports.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferenceAdapter {
    private Context cntx;
    private static SharedPreferenceAdapter instance;

    /**
     * Get instance of the class. implements the singleton design pattern.
     *
     * @return instance of the class to work with
     */
    public synchronized static SharedPreferenceAdapter getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceAdapter(context);
        }
        return instance;
    }

    /**
     * Constructs the session manager object with context. The context should be
     * the activity that holds the operations.
     */
    private SharedPreferenceAdapter(Context context) {
        this.cntx = context;
    }

    /**
     * Getting the string the represents the inputed id in the resources files
     *
     * @param id
     *            - the id of the resouce. for example: R.id.bla or
     *            R.string.keyvalue
     */
    private String getStringFromResources(int id) {
        return cntx.getResources().getString(id);
    }

    /**
     * Getting instance of the SharedPreferences that supply the basic
     * functionallity
     */
    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this.cntx);
    }

    /**
     * Getting editor for writing to the preferences. already performs call to
     * the getSharedPreferences() to get instance to work with.
     */
    private Editor getEditor() {
        return getSharedPreferences().edit();
    }

    /**
     * Checking whether the inputed key is existed in the preferences
     */
    public boolean checkIsSet(String key) {
        return getSharedPreferences().contains(key);
    }

    // Reading string
    public String readString(int id) {
        return this.readString(getStringFromResources(id));
    }

    public String readString(int id, String defValue) {
        return readString(getStringFromResources(id), defValue);
    }

    public String readString(String key) {
        return this.readString(key, "Default Value");
    }

    public String readString(String key, String defValue) {
    	String readString = getSharedPreferences().getString(key, defValue);
		readString = readString == null ? defValue : readString;
        return readString;
    }

    public void writeString(String key, String value){
        getEditor().putString(key, value).commit();
    }

    public void writeString(int id, String value){
        this.writeString(getStringFromResources(id), value);
    }

    // Reading & Writing boolean
    public void writeBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public void writeBoolean(int id, boolean value) {
        this.writeBoolean(getStringFromResources(id), value);
    }

    public boolean readBoolean(int id) {
        return this.readBoolean(getStringFromResources(id));
    }

    public boolean readBoolean(int id, boolean defValue) {
        return this.readBoolean(getStringFromResources(id), defValue);
    }

    public boolean readBoolean(String key) {
        return getSharedPreferences().getBoolean(key, true);
    }

    public boolean readBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    // Reading & Writing integer
    public void writeInteger(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public void writeInteger(int id, int value) {
        this.writeInteger(getStringFromResources(id), value);
    }

    public int readInteger(int id) {
        return this.readInteger(getStringFromResources(id));
    }

    public int readInteger(String key) {
        return getSharedPreferences().getInt(key, -1);
    }

    // Reading & Writing long
    public void writeLong(String key, long value) {
        getEditor().putLong(key, value).commit();
    }

    public void writeLong(int id, long value) {
        this.writeLong(getStringFromResources(id), value);
    }

    public long readLong(int id) {
        return this.readLong(getStringFromResources(id));
    }

    public long readLong(String key) {
        return getSharedPreferences().getLong(key, -1);
    }

}
