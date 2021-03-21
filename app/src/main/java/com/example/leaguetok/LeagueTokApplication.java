package com.example.leaguetok;

import android.app.Application;
import android.content.Context;

public class LeagueTokApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
