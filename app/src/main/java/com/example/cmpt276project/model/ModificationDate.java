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

public class ModificationDate {

    private static String inputUrl;
    private Context mContext;
    private String modifiedDate = "None";


    public ModificationDate(Context context, Looper looper, FragmentManager manager, String url) {
        inputUrl = url;
        mContext = context;

        GetModificationDate runnable = new GetModificationDate(looper, manager);

        // For Cleanup
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
                String jsonText = readUrl(inputUrl);
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

                Log.v("ModiDate","The current date is " + formattedDate);

                Log.v("ModiDate","The shared prefs for " +inputUrl+" is " + getModDateSharedPrefs(inputUrl));
                Log.v("ModiDate","The shared prefs for updatedOn is " + getModDateSharedPrefs("updatedOn"));

                if(modifiedDate != null){

                    if(getModDateSharedPrefs(inputUrl).equals("None")){
                        storeModDateSharedPrefs(inputUrl,modifiedDate);

                        Handler threadHandler = new Handler(mLooper);
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                UpdateDialog dialog = new UpdateDialog();
                                dialog.show(mManager,"TestDialog");
                                Toast.makeText(mContext,"Handler working",Toast.LENGTH_LONG).show();
                            }
                        });

                        // Store the current updated time to keep track of checks
                        storeModDateSharedPrefs("updatedOn",formattedDate);

                    }
                    else{
                        // If newly fetched date is more recent than stored data, do the work
                        if(convertToDate(getModDateSharedPrefs(inputUrl)).compareTo(convertToDate(modifiedDate))<0){
                            storeModDateSharedPrefs(inputUrl,modifiedDate);

                            Handler threadHandler = new Handler(mLooper);
                            threadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    UpdateDialog dialog = new UpdateDialog();
                                    dialog.show(mManager,"TestDialog");
                                    Toast.makeText(mContext,"Handler working",Toast.LENGTH_LONG).show();
                                }
                            });

                            // Store the current updated time to keep track of checks
                            storeModDateSharedPrefs("updatedOn",formattedDate);
                        }
                        else{
                            //do nothing because update is old
                        }
                    }
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

        private String getModDateSharedPrefs(String url) {
            SharedPreferences prefs = mContext.getSharedPreferences("CSVData",Context.MODE_PRIVATE);

            return prefs.getString(url,"None");
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
    }
}
