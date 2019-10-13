package com.mgosu.imagepreview.ui.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {

    private SharedPreferences preferences;
    private static PreferencesUtils instance;

    private PreferencesUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferencesUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesUtils(context);
        }
        return instance;
    }


    public void setVersion(int i) {
        preferences.edit().putInt("KEY_Version", i).apply();
    }

    public int getVersion() {
        return preferences.getInt("KEY_Version", 0);
    }

    public void setUrl(String i) {
        preferences.edit().putString("KEY_Url", i).apply();
    }

    public String getUrl() {
        return preferences.getString("KEY_Url", "");
    }


    public void setChangeImage(boolean i) {
        preferences.edit().putBoolean("KEY_ImageChange", i).apply();
    }

    public boolean getChangeImage() {
        return preferences.getBoolean("KEY_ImageChange", false);
    }

    public void setChangeText(boolean i) {
        preferences.edit().putBoolean("KEY_Text", i).apply();
    }

    public boolean getChangeText() {
        return preferences.getBoolean("KEY_Text", false);
    }
}
