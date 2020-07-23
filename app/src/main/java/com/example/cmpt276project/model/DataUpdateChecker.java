package com.example.cmpt276project.model;

import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.concurrent.Callable;

import static com.example.cmpt276project.model.DateHelper.isMoreRecentThan;
import static com.example.cmpt276project.model.DateHelper.isTwentyHoursSince;


public class DataUpdateChecker implements Callable<Boolean> {

    private SharedPreferences csvDataPrefs;

    public DataUpdateChecker(SharedPreferences csvDataPrefs) {
        this.csvDataPrefs = csvDataPrefs;
    }

    @Override
    public Boolean call() {
        String lastUpdateTime = csvDataPrefs.getString("updatedOn", DateHelper.DEFAULT_TIME);
        String localModifiedTime = csvDataPrefs.getString("localModifyTime", DateHelper.DEFAULT_TIME);

        if (!isTwentyHoursSince(lastUpdateTime)) {
            return false;
        }

        try {
            JSONObject response = HttpRequestHandler.get(DataDownloader.RESTAURANTS_URL);
            String remoteModifiedTime = (String) response.getJSONObject("result").get("metadata_modified");
            remoteModifiedTime = remoteModifiedTime.replaceAll("[^0-9]", "");
            remoteModifiedTime = remoteModifiedTime.substring(0,14);

            return isMoreRecentThan(remoteModifiedTime, localModifiedTime);

        } catch (Exception e) {
            return false;
        }
    }
}
