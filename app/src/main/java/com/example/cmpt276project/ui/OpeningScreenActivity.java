package com.example.cmpt276project.ui;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.cmpt276project.MapsActivity;
import com.example.cmpt276project.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Integer.parseInt;

/**
 * This class plays a small animation while checking if update is needed
 */

public class OpeningScreenActivity extends FragmentActivity {

    private static String RESTAURANTS_URL = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private static String INSPECTIONS_URL = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        // Set up handler for delay of Main Menu (might not be necessary anymore)
        boolean isUpdateNeeded = updatedNeeded();
        ActivityLauncherRunnable runnable;
        runnable = new ActivityLauncherRunnable(isUpdateNeeded);

        Handler handler = new Handler();
        handler.postDelayed(runnable,3200);
    }

    private Boolean updatedNeeded(){
        SharedPreferences prefs = this.getSharedPreferences("CSVData",Context.MODE_PRIVATE);

        String lastUpdateOn = prefs.getString("updatedOn","None");
        assert lastUpdateOn != null;

        // Never been updated before
        if(lastUpdateOn.equals("None")){
            Toast.makeText(this,"Update Needed, Last Update = None",Toast.LENGTH_LONG).show();
            return true;
        }

        // Get current program time and date formatted
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(currentTime.getTime());
        formattedDate = formattedDate.replaceAll("[^0-9]", "");

        // Compare the Years, Months, Days and Hours of the two dates to see if its been longer than 20 hours
        if (parseInt(formattedDate.substring(0, 4)) > parseInt(lastUpdateOn.substring(0, 4)) ||
                parseInt(formattedDate.substring(4, 6)) > parseInt(lastUpdateOn.substring(4, 6)) ||
                parseInt(formattedDate.substring(6, 8)) > parseInt(lastUpdateOn.substring(6, 8)) ||
                (parseInt(formattedDate.substring(8, 12)) - parseInt(lastUpdateOn.substring(8, 12)) > 2000)) {
            Toast.makeText(this,"Update Needed",Toast.LENGTH_LONG).show();
            return true;
        }

        Toast.makeText(this,"Update Not Needed",Toast.LENGTH_LONG).show();
//        return convertToDate(lastUpdateOn).compareTo(convertToDate(formattedDate)) < 0;
        return false;
    }

    private GregorianCalendar convertToDate(String numberOnly) {
        int year = parseInt(numberOnly.substring(0, 4));
        int month = parseInt(numberOnly.substring(4, 6));
        int day = parseInt(numberOnly.substring(6, 8));
        return new GregorianCalendar(year, month, day);
    }

    private void launchMainActivity(boolean isUpdateNeeded) {
        Intent intent = MapsActivity.makeIntent(this, isUpdateNeeded);
        startActivity(intent);
    }

    private class ActivityLauncherRunnable implements Runnable {

        private boolean isUpdateNeeded;

        public ActivityLauncherRunnable(boolean isUpdateNeeded) {
            this.isUpdateNeeded = isUpdateNeeded;
        }

        @Override
        public void run() {
            launchMainActivity(isUpdateNeeded);
            finish();
        }
    }

}