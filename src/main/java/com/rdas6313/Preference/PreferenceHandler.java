package com.rdas6313.Preference;

public interface PreferenceHandler {
    void store(String key,int value);
    void store(String key,String value);
    void store(String key,boolean value);
    int retrieveInt(String key);
    String retrieveString(String key);
    boolean retrieveBoolean(String key);
}
