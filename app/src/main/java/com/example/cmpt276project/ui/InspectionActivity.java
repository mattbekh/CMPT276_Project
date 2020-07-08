package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.Violation;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Class to create the third activity to display an Inspection with its a violations list
 */
public class InspectionActivity extends AppCompatActivity {

    private ListView violationsList;
    private Intent intent;

    private Restaurant restaurant;
    private RestaurantManager manager;


    //Inspection Data
    private Inspection inspection;
    private TextView inspectDate;
    private TextView inspectType;
    private TextView inspectCritical;
    private TextView inspectNonCritical;
    private ImageView hazardIcon;
    private TextView inspectText;


    public static Intent makeIntent(Context context) {
        return new Intent(context, InspectionActivity.class);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backIcon:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        setupToolbar();

        intent = getIntent();

        manager = RestaurantManager.getInstance();

        getData(intent);
        setData(inspection);

        populateListView();
    }

    void populateListView() {
        violationsList = findViewById(R.id.violationList);

        ViolationsListAdapter adapter = new ViolationsListAdapter(this,R.layout.violation_row,inspection.getViolationsList1());
        violationsList.setAdapter(adapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void getData(Intent it) {
        if (getIntent().hasExtra("inspection")) {
            Bundle extras = it.getExtras();
            assert extras != null;
            int pos = extras.getInt("inspection");
            int restaurantPos = extras.getInt("restaurant");
            restaurant = manager.get(restaurantPos);
            inspection = restaurant.getInspectionByIndex(pos);

        }
    }

    private void setData(Inspection inspection) {

        inspectDate = findViewById(R.id.inspectDate);
        inspectType = findViewById(R.id.inspectType);
        inspectCritical = findViewById(R.id.inspectCritical);
        inspectNonCritical = findViewById(R.id.inspectNonCritical);
        hazardIcon = findViewById(R.id.hazardIcon);
        inspectText = findViewById(R.id.inspectHazard);

        inspectDate.setText(inspection.getFullDate());
        inspectType.setText(inspection.getInspectionTypeString());

        String criticalIssues = Integer.toString(inspection.getNumCriticalIssues());
        String nonCriticalIssues = Integer.toString(inspection.getNumNonCriticalIssues());

        inspectCritical.setText("Number of Critical Issues: " + criticalIssues);
        inspectNonCritical.setText("Number of NonCritical Issues: " + nonCriticalIssues);

        inspectText.setText("Hazard Level: " + inspection.getHazardRatingString());

        int orangeColor = Color.parseColor("#FFC229");

        switch (inspection.getHazardRating()) {

            case LOW:
                hazardIcon.setImageResource(R.drawable.happy_face_icon);
                hazardIcon.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                break;
            case MODERATE:
                hazardIcon.setImageResource(R.drawable.straight_face_icon);
                hazardIcon.setColorFilter(orangeColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case HIGH:
                hazardIcon.setImageResource(R.drawable.unhappy_face_icon);
                hazardIcon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                break;
        }
    }

}