package com.example.cmpt276project.model;

import android.content.SharedPreferences;

import java.util.concurrent.Callable;

import static com.example.cmpt276project.model.DateHelper.isTwentyHoursSince;


public class DataUpdateChecker implements Callable<Boolean> {

    SharedPreferences CsvDataPrefs;

    public DataUpdateChecker(SharedPreferences CsvDataPrefs) {
        this.CsvDataPrefs = CsvDataPrefs;
    }

    @Override
    public Boolean call() {
        String lastUpdateTime = CsvDataPrefs.getString("updatedOn","None");
        if (!isTwentyHoursSince(lastUpdateTime)) {
            return false;
        }

        return true;
    }
}
