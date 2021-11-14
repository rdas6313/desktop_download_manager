package com.rdas6313.Preference;

import java.util.prefs.Preferences;

public class PreferenceApi implements PreferenceHandler{

    private Preferences pref;

    public PreferenceApi(Preferences pref) throws IllegalArgumentException {
        if(pref == null)
            throw new IllegalArgumentException("Preference object can not be null");
        this.pref = pref;
    }

    @Override
    public void store(String key, int value) {
        pref.putInt(key, value);
    }

    @Override
    public void store(String key, String value) {
        pref.put(key, value);
    }

    @Override
    public void store(String key, boolean value) {
        pref.putBoolean(key, value);
    }

    @Override
    public boolean retrieveBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    @Override
    public int retrieveInt(String key) {
        return pref.getInt(key, -1);
    }

    @Override
    public String retrieveString(String key) {
        return pref.get(key, null);
    }

    
    
}
