package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.cmpt276project.R;

/**
 * This class plays a small animation before launching the main app
 */
public class OpeningScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        // Set up handler for delay of Main Menu
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {

                finish();
                launchMainMenu();

            }
        };
        handler.postDelayed(r,2800);
    }

    private void launchMainMenu() {
        // Launch Main Menu
        Intent intent = new Intent(OpeningScreenActivity.this, RestaurantListActivity.class);
        startActivity(intent);
    }
}