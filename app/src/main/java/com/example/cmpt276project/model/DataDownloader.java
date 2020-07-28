package com.example.cmpt276project.model;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONObject;

import java.util.concurrent.Callable;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;
import static android.app.DownloadManager.Request.NETWORK_WIFI;
import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;

/**
 * Class which downloads the data and keeps track of progress
 */
public abstract class DataDownloader implements Callable<Boolean> {

    private static int NUM_DOWNLOADS = 2;
    public static String RESTAURANTS_URL = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
    public static String INSPECTIONS_URL = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";

    private DownloadManager downloadManager;
    private int totalProgress;

    public DataDownloader(Context context) {
        this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public Boolean call() {
        try {
            downloadFile(RESTAURANTS_URL, DataUpdater.TEMP_RESTAURANT_FILE);
            downloadFile(INSPECTIONS_URL, DataUpdater.TEMP_INSPECTION_FILE);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void downloadFile(String url, String fileName) {
        DownloadManager.Request request = getDownloadRequest(url, fileName);

        long downloadId = downloadManager.enqueue(request);
        processRequest(downloadId);

        totalProgress += 100 / NUM_DOWNLOADS;
    }

    private void processRequest(long downloadId) {
        while (true) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = downloadManager.query(query);
            cursor.moveToFirst();

            int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            if (bytesTotal != -1) {
                int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int currentProgress = bytesDownloaded * (100 / NUM_DOWNLOADS) / bytesTotal;
                updateProgress(totalProgress + currentProgress);
            }

            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            cursor.close();
            if (status == DownloadManager.STATUS_SUCCESSFUL
                || status == DownloadManager.STATUS_FAILED)
            {
                break;
            }
        }
    }

    private DownloadManager.Request getDownloadRequest(String url, String fileName) {
        try {
            JSONObject response = HttpRequestHandler.get(url);
            String linkCsv = (String) response
                    .getJSONObject("result")
                    .getJSONArray("resources")
                    .getJSONObject(0)
                    .get("url");

            Uri uri = Uri.parse(linkCsv);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(NETWORK_WIFI | NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            //TODO Potentially internationalize this Download/ Downloading file Notification in dropdown?
            request.setTitle("Download");
            request.setDescription("Downloading File");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            return request;
        } catch (Exception e) {
            return null;
        }
    }

    public abstract void updateProgress(int progress);
}
