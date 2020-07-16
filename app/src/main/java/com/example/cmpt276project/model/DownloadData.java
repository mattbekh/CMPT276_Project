package com.example.cmpt276project.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.cmpt276project.ui.TestFragment;

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


    public DownloadData(Context context, Looper looper, FragmentManager manager, String url) {
        inputUrl = url;
        mContext = context;
        GetModificationDate runnable = new GetModificationDate(looper, manager);
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

                Handler threadHandler = new Handler(mLooper);
                threadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TestFragment dialog = new TestFragment();
                        dialog.show(mManager,"TestDialog");
                        Toast.makeText(mContext,"Handler working",Toast.LENGTH_LONG).show();
                    }
                });



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
    }
}
