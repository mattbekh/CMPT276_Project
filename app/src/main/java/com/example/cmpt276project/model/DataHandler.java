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
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;
import static java.lang.Integer.parseInt;

public class DataHandler {

    private long downloadId;

    private Context mContext;

    public DataHandler(Context context){
        mContext = context;
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

        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Downloading");
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading File");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);

    }

    public void cancelDownload(){
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.remove(downloadId);
    }

    /**
     * Compares stored pref date to Modification Date fetched from URL
     * returns True if Update is needed
      */

    public Boolean updateNeeded(String url) {

        if(getModifiedDate(url)!= null){
            if(getModDateSharedPrefs(url).equals("None")){

                storeModDateSharedPrefs(getModifiedDate(url),url);
                return true;

            }
            else{
                if(convertToDate(getModDateSharedPrefs(url)).compareTo(convertToDate(getModifiedDate(url)))<0){
                    storeModDateSharedPrefs(getModifiedDate(url),url);
                    return true;
                }
                else{
                    //do nothing
                }
            }
        }

        return false;
    }

    private void storeModDateSharedPrefs(String date, String url) {
        SharedPreferences prefs = mContext.getSharedPreferences("CSVData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(url,date);
        editor.apply();
    }

    public void clearSharedPrefs(){
        SharedPreferences prefs = mContext.getSharedPreferences("AppData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.clear();
        editor.apply();
        Toast.makeText(mContext,"Shared prefs cleared",Toast.LENGTH_SHORT).show();
    }

    private String getModDateSharedPrefs(String url) {
        SharedPreferences prefs = mContext.getSharedPreferences("CSVData",Context.MODE_PRIVATE);

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

    private GregorianCalendar convertToDate(String numberOnly) {
        int year = parseInt(numberOnly.substring(0, 4));
        int month = parseInt(numberOnly.substring(4, 6));
        int day = parseInt(numberOnly.substring(6, 8));
        return new GregorianCalendar(year, month, day);
    }

    public long getDownloadId(){
        return downloadId;
    }
}

