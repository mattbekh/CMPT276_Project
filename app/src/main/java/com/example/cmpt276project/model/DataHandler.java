package com.example.cmpt276project.model;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;
import static java.lang.Integer.parseInt;

public class DataHandler {

    private static String RESTAURANTS_URL = "http://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private static String INSPECTIONS_URL = "http://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";

    private static String RESTAURANTS_CSV_URL = "https://data.surrey.ca/dataset/3c8cb648-0e80-4659-9078-ef4917b90ffb/resource/0e5d04a2-be9b-40fe-8de2-e88362ea916b/download/restaurants.csv";
    private static String INSPECTIONS_CSV_URL = "https://data.surrey.ca/dataset/948e994d-74f5-41a2-b3cb-33fa6a98aa96/resource/30b38b66-649f-4507-a632-d5f6f5fe87f1/download/fraserhealthrestaurantinspectionreports.csv";

    private long downloadId;

    private Context mContext;

    public DataHandler(Context context){
        mContext = context;

        // Something like this needs to go inside UI to display status

//        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        mContext.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                long broadcastDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
//
//                if(broadcastDownloadID == downloadId) {
//                    if(getDownloadStatus() == DownloadManager.STATUS_SUCCESSFUL) {
//                        Toast.makeText(mContext,"Download Complete", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(mContext,"Download Failed",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }, filter);
    }

    private int getDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        Cursor cursor = downloadManager.query(query);

        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);

            return status;
        }

        return DownloadManager.ERROR_UNKNOWN;
    }


    public void downloadCSVData(String url, String fileName) {

        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Downloading Data");

        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,fileName);

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);

        Toast.makeText(mContext,"Download Started",Toast.LENGTH_SHORT).show();
    }

    public void cancelDownload(){
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.remove(downloadId);
    }

    /**
     * Compares stored pref date to Modification Date fetched from URL
     * returns True if Update is needed
      */

    private Boolean updateNeeded(String url) {
        String fetchedDateString = getModifiedDate(url);
        if (fetchedDateString != null) {
            String storedDateString = getModDateSharedPrefs(url);
            if (storedDateString.equals("None")){
                storeModDateSharedPrefs(fetchedDateString, url);
                return true;
            }

            GregorianCalendar storedDate = DateHelper.getDateFromString(storedDateString);
            GregorianCalendar fetchedDate = DateHelper.getDateFromString(fetchedDateString);
            boolean isFetchedDateMoreRecent = storedDate.compareTo(fetchedDate) < 0;
            if (isFetchedDateMoreRecent) {
                storeModDateSharedPrefs(fetchedDateString, url);
                return true;
            }
        }

        return false;
    }

    private void storeModDateSharedPrefs(String date, String url) {
        SharedPreferences prefs = mContext.getSharedPreferences("AppData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(url,date);
        editor.apply();
    }

    private String getModDateSharedPrefs(String url) {
        SharedPreferences prefs = mContext.getSharedPreferences("AppData",Context.MODE_PRIVATE);

        return prefs.getString(url,"None");
    }

    private String getModifiedDate(String url) {

        URL mUrl = null;
        String[] content;
        try {
            mUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert mUrl != null;
            URLConnection connection = mUrl.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = br.readLine();
            String search = "metadata_modified";

            content = line.split(",");

            for(int i=0; i<content.length;i++){

                if(content[i].toLowerCase().contains(search.toLowerCase())){
                    // Found line
                    // Modify text to output only integers, which will later be truncated for yyyy,MM,dd
                    return content[i].replaceAll("[^0-9]", "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

