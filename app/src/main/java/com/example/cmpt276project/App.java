package com.example.cmpt276project;

import android.app.Application;
import android.content.res.Resources;

public class App extends Application {
    private static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        res = this.getResources();
    }

    public static Resources resources() {
        return res;
    }
}
