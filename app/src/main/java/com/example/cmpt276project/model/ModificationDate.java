package com.example.cmpt276project.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.cmpt276project.ui.UpdateDialog;

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
 * Class to keep track of modification date. Gets modification date from URL and compares it with current date and time.
 * If shared prefs dont have a modification date stored, it stores current date. If modification date from URL is newer than
 * stored date, updates the stored date to modification date from URL
 */
public class ModificationDate {

    private static String inputRestaurantUrl;
    private Context mContext;
    private String modifiedDate = "None";


    public ModificationDate(Context context, Looper looper, FragmentManager manager, String url) {
        inputRestaurantUrl = url;
        mContext = context;

        // Launches inner class to get modification date
        GetModificationDate runnable = new GetModificationDate(looper, manager);

        // For Cleanup, to show up in UI for each launch
        runnable.clearSharedPrefs();

        new Thread(runnable).start();
    }
    public class GetModificationDate implements Runnable {

        private Looper mLooper;
        private FragmentManager mManager;

        GetModificationDate(Looper looper,FragmentManager manager){
            mLooper = looper;
            mManager = manager;
        }

        @Override
        public void run() {
            try {
                // Convert URL content to a JSON object to get data
//                BufferedReader rd = new BufferedReader(responseBodyReader);
                String jsonText = readUrl(inputRestaurantUrl);
                JSONObject json = new JSONObject(jsonText);

                modifiedDate = (String) json.getJSONObject("result").get("metadata_modified");
                modifiedDate = modifiedDate.replaceAll("[^0-9]", "");

                Log.v("ModiDate","The date is " + modifiedDate);

                // Get current program time and date formatted
                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(currentTime.getTime());
                formattedDate = formattedDate.replaceAll("[^0-9]", "");

                // Nice looking date for a TextView
//                String currentTime = java.text.DateFormat.getDateTimeInstance().format(new Date());

                if(modifiedDate != null){

                    // Compares stored preference date with the date fetched from URL
                    if(getModDateSharedPrefs(inputRestaurantUrl).equals("None")) {
                        storeModDateSharedPrefs(inputRestaurantUrl, modifiedDate);

                        // Store the current updated time to keep track of checks
                        storeModDateSharedPrefs("updatedOn", formattedDate);

                        // Communicates with Activity which launched it to create a UpdateDialog Fragment to ask if the user wants to download data
                        Handler threadHandler = new Handler(mLooper);
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                UpdateDialog dialog = new UpdateDialog();
                                dialog.show(mManager, "TestDialog");
                            }
                        });

                        // If newly fetched date is more recent than stored data, do the work
                    }else if(convertToDate(getModDateSharedPrefs(inputRestaurantUrl)).compareTo(convertToDate(modifiedDate))<0){
                            storeModDateSharedPrefs(inputRestaurantUrl,modifiedDate);

                            // Communicates with Activity which launched it to create a UpdateDialog Fragment to ask if the user wants to download data
                            Handler threadHandler = new Handler(mLooper);
                            threadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    UpdateDialog dialog = new UpdateDialog();
                                    dialog.show(mManager,"TestDialog");
                                }
                            });

                            // Store the current updated time to keep track of checks
                            storeModDateSharedPrefs("updatedOn",formattedDate);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Stores date in shared prefs with the url as key and date as value
        private void storeModDateSharedPrefs(String key, String date) {
            SharedPreferences prefs = mContext.getSharedPreferences("CSVData",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(key,date);
            editor.apply();
        }

        public void clearSharedPrefs(){
            SharedPreferences prefs = mContext.getSharedPreferences("CSVData",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.clear();
            editor.apply();
            Toast.makeText(mContext,"Shared prefs cleared",Toast.LENGTH_SHORT).show();
        }

        // Uses URL as key to fetch the stored date
        private String getModDateSharedPrefs(String url) {
            SharedPreferences prefs = mContext.getSharedPreferences("CSVData",Context.MODE_PRIVATE);

            return prefs.getString(url,"None");
        }

        // Parses URL content into text which is then converted to a JSON
        private String readUrl(String urlString) throws Exception {
            BufferedReader reader = null;
            try {
                URL url = new URL(urlString);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder buffer = new StringBuilder();
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
    }
}
