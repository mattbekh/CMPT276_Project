package com.example.cmpt276project.ui;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.cmpt276project.MapsActivity;
import com.example.cmpt276project.R;
import com.example.cmpt276project.model.DataUpdateChecker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class plays a small animation while checking if update is needed
 */

public class OpeningScreenActivity extends FragmentActivity {

    private static String RESTAURANTS_URL = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private static String INSPECTIONS_URL = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";

    Future<Boolean> updateCheckResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        startUpdate();
        waitForUpdate();
    }

    private void startUpdate() {
        SharedPreferences prefs = this.getSharedPreferences("CSVData", Context.MODE_PRIVATE);
        DataUpdateChecker updateChecker = new DataUpdateChecker(prefs);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        updateCheckResult = executor.submit(updateChecker);
    }

    private void waitForUpdate() {
        Handler handler = new Handler();
        handler.postDelayed(new ActivityLauncherRunnable(),3200);
    }

    private void launchMainActivity(boolean isUpdateNeeded) {
        Intent intent = MapsActivity.makeIntent(this, isUpdateNeeded);
        startActivity(intent);
    }

    private class ActivityLauncherRunnable implements Runnable {

        @Override
        public void run() {
            boolean isUpdateNeeded = false;

            try {
                isUpdateNeeded = updateCheckResult.get();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                launchMainActivity(isUpdateNeeded);
                finish();
            }
        }
    }

}