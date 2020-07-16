package com.example.cmpt276project.model;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.GregorianCalendar;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static java.lang.Integer.parseInt;

public class DownloadCSVData implements Runnable {


        private Context mContext;
        private String mUrl;

        public DownloadCSVData(Context context, String url) {
            mContext = context;
            mUrl = url;
        }


        @Override
        public void run() {
            try {
                // Convert URL content to a JSON object to get data
//                BufferedReader rd = new BufferedReader(responseBodyReader);
                String jsonText = readUrl(mUrl);
                JSONObject json = new JSONObject(jsonText);

                String linkCSV = (String) json
                        .getJSONObject("result")
                        .getJSONArray("resources")
                        .getJSONObject(0)
                        .get("url");

                Log.v("CSV_DATA_LINK","THE CSV DATA LINK IS : " + linkCSV);

                OkHttpClient client = new OkHttpClient();




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        private static String readUrl(String urlString) throws Exception {
            BufferedReader reader = null;
            try {
                URL url = new URL(urlString);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuffer buffer = new StringBuffer();
                int read;
                char[] chars = new char[1024];
                while ((read = reader.read(chars)) != -1)
                    buffer.append(chars, 0, read);

                return buffer.toString();
            } finally {
                if (reader != null)
                    reader.close();
            }
        }

        private GregorianCalendar convertToDate(String numberOnly) {
            int year = parseInt(numberOnly.substring(0, 4));
            int month = parseInt(numberOnly.substring(4, 6));
            int day = parseInt(numberOnly.substring(6, 8));
            return new GregorianCalendar(year, month, day);
        }

//        public void downloadCSVData(String url, String fileName) {
//
//        Uri uri = Uri.parse(url);
//
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//
//        //Restrict the types of networks over which this download may proceed.
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//        //Set whether this download may proceed over a roaming connection.
//        request.setAllowedOverRoaming(false);
//        //Set the title of this download, to be displayed in notifications (if enabled).
//        request.setTitle("Downloading");
//        //Set a description of this download, to be displayed in notifications (if enabled)
//        request.setDescription("Downloading File");
//        request.allowScanningByMediaScanner();
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        //Set the local destination for the downloaded file to a path within the application's external files directory
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//
//        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
//        downloadId = downloadManager.enqueue(request);
//
//    }
}

//    private String getModDateSharedPrefs(String url) {
//        SharedPreferences prefs = mContext.getSharedPreferences("CSVData", Context.MODE_PRIVATE);
//
//        return prefs.getString(url,"None");
//    }

