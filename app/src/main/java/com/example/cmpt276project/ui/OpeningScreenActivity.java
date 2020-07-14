package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.DataHandler;

import java.io.File;

/**
 * This class plays a small animation before launching the main app
 */
public class OpeningScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        // Set up handler for delay of Main Menu
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DataHandler dataHandler = new DataHandler(OpeningScreenActivity.this);
                dataHandler.downloadCSVData("https://data.surrey.ca/dataset/3c8cb648-0e80-4659-9078-ef4917b90ffb/resource/0e5d04a2-be9b-40fe-8de2-e88362ea916b/download/restaurants.csv","restaurant_data.csv");
                File file = new File(Environment.
                        DIRECTORY_DOWNLOADS, "restaurant_data.csv");
                Toast.makeText(OpeningScreenActivity.this,
                        file.toString(),
                        Toast.LENGTH_LONG).show();
//                launchMainMenu();
//
//                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(r,10000);
    }

    private void launchMainMenu() {
        Intent intent = new Intent(OpeningScreenActivity.this, RestaurantListActivity.class);
        startActivity(intent);
    }
}