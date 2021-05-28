package com.example.leaguetok;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class LeagueTokApplication extends MultiDexApplication {
    public static Context context;
    public static String serverUrl = "http://10.0.0.17:8080";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
