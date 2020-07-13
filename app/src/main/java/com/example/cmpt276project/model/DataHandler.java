package com.example.cmpt276project.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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

import static java.lang.Integer.parseInt;

public class DataHandler extends AsyncTask<URL, Void, Boolean> {

    private Context mContext;

    private int year;
    private int month;
    private int day;

    public DataHandler(Context context){
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(URL... voids) {
        if(updateNeeded()){
            downloadCSVData("restaurant_data");
        }
        return updateNeeded();
    }

    private void downloadCSVData(String fileName) {

    }

    /**
     * Compares stored pref date to Modification Date fetched from URL
     * returns True if Update is needed
      */

    private Boolean updateNeeded() {

        if(getModifiedDate()!=null){
            if(getModDateSharedPrefs().equals("None")){
                storeModDateSharedPrefs(getModifiedDate());
                return true;
            }
            else{
                if(convertToDate(getModDateSharedPrefs()).compareTo(convertToDate(getModifiedDate()))<0){
                    storeModDateSharedPrefs(getModifiedDate());
                    return true;
                }
                else{
                    //do nothing
                }
            }
        }

        return false;
    }

    private void storeModDateSharedPrefs(String date) {
        SharedPreferences prefs = mContext.getSharedPreferences("AppData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("ModificationDate",date);
        editor.apply();
    }

    private String getModDateSharedPrefs() {
        SharedPreferences prefs = mContext.getSharedPreferences("AppData",Context.MODE_PRIVATE);

        return prefs.getString("ModificationDate","None");
    }

    private String getModifiedDate() {

        URL mUrl = null;
        String[] content;
        try {
            mUrl = new URL("https://data.surrey.ca/api/3/action/package_show?id=restaurants");
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
        year = parseInt(numberOnly.substring(0,4));
        month = parseInt(numberOnly.substring(4,6));
        day = parseInt(numberOnly.substring(6,8));
        return new GregorianCalendar(year, month, day);
    }

    @Override
    protected void onPostExecute(Boolean update) {
    }
}

