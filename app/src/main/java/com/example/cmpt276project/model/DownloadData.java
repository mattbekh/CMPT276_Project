package com.example.cmpt276project.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.Integer.parseInt;

public class DownloadData {

    private static String inputUrl;
    private Context mContext;
    private String modifiedDate = "None";


    public DownloadData(Context context, String url) {
        inputUrl = url;
        mContext = context;
        new ModifiedDate().execute();
//        modifiedDate = modiDate.getModificationDate();
//        Log.v("DownloadData","The date is " + modifiedDate);
    }

    public static class ModifiedDate extends AsyncTask<String, Void, Void> {
        private String modifiedDate = "None";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void s) {
            // If modified date more recent than stored date then launch fragment to ask user if they want to download

            //convertToDate(modifiedDate);
        }

        @Override
        protected Void doInBackground(String... urls) {
            try {
                // Convert URL content to a JSON object to get data
//                BufferedReader rd = new BufferedReader(responseBodyReader);
                String jsonText = readUrl(inputUrl);
                JSONObject json = new JSONObject(jsonText);

                modifiedDate = (String) json.getJSONObject("result").get("metadata_modified");
                modifiedDate = modifiedDate.replaceAll("[^0-9]", "");

                Log.v("ModiDate","The date is " + modifiedDate);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
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

        private String getModificationDate(){
            return modifiedDate;
        }

    }

    private String getModDateSharedPrefs(String url) {
        SharedPreferences prefs = mContext.getSharedPreferences("CSVData",Context.MODE_PRIVATE);

        return prefs.getString(url,"None");
    }
}
