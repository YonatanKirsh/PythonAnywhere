package com.kirsh.pythonanywhere;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SuperApp extends Application {
    public String mToken;

    @Override
    public void onCreate(){
        super.onCreate();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sp.getString(Shared.TOKEN_TAG, null);
    }
}
