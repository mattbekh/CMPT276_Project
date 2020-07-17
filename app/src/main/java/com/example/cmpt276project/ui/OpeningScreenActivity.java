package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.ModificationDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Integer.parseInt;

/**
 * This class plays a small animation before launching the main app
 */
public class OpeningScreenActivity extends AppCompatActivity {

    private static String RESTAURANTS_URL = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private static String INSPECTIONS_URL = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";

    private Handler openingHandler = new Handler();
    private ProgressDialog progressBarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        if(updatedNeeded()){
            loadCSVData();
        }

        // Set up handler for delay of Main Menu
        Runnable r = new Runnable() {
            @Override
            public void run() {
//                launchMainMenu();
//                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(r,3200);
    }

    private void loadCSVData() {
        Looper looper = getMainLooper();
        FragmentManager manager = getSupportFragmentManager();
        ModificationDate modificationDate = new ModificationDate(OpeningScreenActivity.this,looper,manager,RESTAURANTS_URL);

//        progressBarDialog = new ProgressDialog(this);
//
//        setupProgressBar();
//        DownloadCSVData downloadCSVData = new DownloadCSVData(OpeningScreenActivity.this,RESTAURANTS_URL);
//        new Thread(downloadCSVData).start();
//        progressBarDialog.show();

//        DataHandler dataHandler = new DataHandler(OpeningScreenActivity.this);
//        dataHandler.downloadCSVData(RESTAURANTS_CSV_URL,"restaurant_data.csv");
//        dataHandler.clearSharedPrefs();
    }
    private void downloadCSVData() {
        progressBarDialog = new ProgressDialog(this);

        setupProgressBar();
        DownloadCSVData downloadCSVData = new DownloadCSVData(OpeningScreenActivity.this,RESTAURANTS_URL);
        new Thread(downloadCSVData).start();
        progressBarDialog.show();
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

    private void launchMainMenu() {
        Intent intent = new Intent(OpeningScreenActivity.this, RestaurantListActivity.class);
        startActivity(intent);
    }

    private void setupProgressBar() {
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                // Toast.makeText(getBaseContext(),
                //       "OK clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        progressBarDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        progressBarDialog.setProgress(0);
    }

    public class DownloadCSVData implements Runnable {



        private Context mContext;
        private String mUrl;


        public DownloadCSVData(Context context, String url) {
            mContext = context;
            mUrl = url;


        }



        @Override
        public void run() {
            boolean downloading = true;

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

                // Test
                Uri uri = Uri.parse(linkCSV);

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
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "restaurant_data.csv");

                DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                long downloadID = manager.enqueue(request);

                Log.i("CSV_DATA", "downloadID is : " +downloadID);

                while (downloading) {

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadID); //filter by id which you have receieved when reqesting download from download manager
                    Cursor cursor = manager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            progressBarDialog.setProgress(dl_progress);

                        }
                    });
                    // Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                    cursor.close();
                }





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


        private String readUrl(String urlString) throws Exception {
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
}