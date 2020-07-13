package com.example.cmpt276project.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataHandler extends AsyncTask<Void,Void,Void> {

    private Context mContext;
    public DataHandler(Context context) {
        mContext = context;
        Log.v("DataHandler","This is bullshit");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //TODO make URL dynamic
        try {
            URL url = new URL("http://data.surrey.ca/api/3/action/package_show?id=restaurants");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            Log.v("DataHandler","Reached doInBackground");
            writeCSV();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void writeCSV() throws IOException {
        Log.d("DataHandler","Reached writeCSV");
        URL restaurantURL = new URL("http://data.surrey.ca/api/3/action/package_show?id=restaurants");

        HttpURLConnection httpURLConnection = (HttpURLConnection) restaurantURL.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        // Line to read CSV
        String fileName = "restaurant_data.csv";
        File file = new File(mContext.getFilesDir() + fileName);


        CSVWriter writer = new CSVWriter(new FileWriter(file));

        String line = "";
        while((line = bufferedReader.readLine()) != null){

            line = bufferedReader.readLine();

            String[] comps = line.split(",");

            Log.d("DataHandler","Value of line is :" +line);
            writer.writeNext(comps);
        }
        writer.close();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
