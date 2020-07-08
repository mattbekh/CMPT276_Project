package com.example.cmpt276project;

import android.app.Application;
import android.content.res.Resources;

/**
 * This class extends the generic Application in order to provide access to resources for model
 * classes with no access.
 */
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
