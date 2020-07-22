package com.example.cmpt276project.ui;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.cmpt276project.MapsActivity;
import com.example.cmpt276project.R;
import com.example.cmpt276project.model.DataUpdateChecker;
import com.example.cmpt276project.model.RestaurantManager;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class plays a small animation while checking if update is needed
 */

public class OpeningScreenActivity extends FragmentActivity {

    Future<Boolean> updateCheckResult;
    Future<Boolean> loadDataResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        SharedPreferences prefs = this.getSharedPreferences("CSVData", Context.MODE_PRIVATE);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        updateCheckResult = executor.submit(new DataUpdateChecker(prefs));
        loadDataResult = executor.submit(new DataLoader());
        executor.submit(new ActivityLauncherRunnable());
    }

    private void launchMainActivity(boolean isUpdateNeeded) {
        Intent intent = MapsActivity.makeIntent(this, isUpdateNeeded);
        startActivity(intent);
        finish();
    }

    private class DataLoader implements Callable<Boolean> {
        @Override
        public Boolean call() {
            RestaurantManager manager = RestaurantManager.getInstance();
            if (manager == null) {
                return false;
            }
            return true;
        }
    }

    private class ActivityLauncherRunnable implements Runnable {

        @Override
        public void run() {
            int timeBetweenChecks = 100; // milliseconds
            while (!updateCheckResult.isDone()
                    || !loadDataResult.isDone())
            {
                try {
                    Thread.sleep(timeBetweenChecks);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            boolean isUpdateNeeded = false;

            try {
                isUpdateNeeded = updateCheckResult.get();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                launchMainActivity(isUpdateNeeded);
            }
        }
    }

}