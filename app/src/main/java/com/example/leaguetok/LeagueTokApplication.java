package com.example.leaguetok;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class LeagueTokApplication extends MultiDexApplication {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
