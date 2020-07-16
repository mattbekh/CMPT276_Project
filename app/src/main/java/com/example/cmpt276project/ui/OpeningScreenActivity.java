package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.DataHandler;
import com.example.cmpt276project.model.DownloadData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class plays a small animation before launching the main app
 */
public class OpeningScreenActivity extends AppCompatActivity {

    private static String RESTAURANTS_URL = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private static String INSPECTIONS_URL = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";

    private static String RESTAURANTS_CSV_URL = "https://data.surrey.ca/dataset/3c8cb648-0e80-4659-9078-ef4917b90ffb/resource/0e5d04a2-be9b-40fe-8de2-e88362ea916b/download/restaurants.csv";
    private static String INSPECTIONS_CSV_URL = "https://data.surrey.ca/dataset/948e994d-74f5-41a2-b3cb-33fa6a98aa96/resource/30b38b66-649f-4507-a632-d5f6f5fe87f1/download/fraserhealthrestaurantinspectionreports.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        loadCSVData();
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

        DownloadData downloadData = new DownloadData(OpeningScreenActivity.this,RESTAURANTS_URL);

//        DataHandler dataHandler = new DataHandler(OpeningScreenActivity.this);
//        dataHandler.downloadCSVData(RESTAURANTS_CSV_URL,"restaurant_data.csv");
//        dataHandler.clearSharedPrefs();
    }

    private void launchMainMenu() {
        Intent intent = new Intent(OpeningScreenActivity.this, RestaurantListActivity.class);
        startActivity(intent);
    }
}