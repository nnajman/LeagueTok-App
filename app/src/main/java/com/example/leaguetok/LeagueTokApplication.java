package com.example.leaguetok;

import android.app.Application;
import android.content.Context;

public class LeagueTokApplication extends Application {
    public static Context context;
    public static String serverUrl = "http://192.168.1.107:8080";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
