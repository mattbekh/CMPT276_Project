package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.Violation;
import com.example.cmpt276project.model.database.DatabaseManager;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class to create the third activity to display an Inspection with its a violations list
 */
public class InspectionActivity extends AppCompatActivity {

    private Intent intent;

    private Restaurant restaurant;
    private RestaurantManager manager;

    //Inspection Data
    private Inspection inspection;
    private ListView violationsList;
    private TextView inspectDate;
    private TextView inspectType;
    private TextView inspectCritical;
    private TextView inspectNonCritical;
    private TextView inspectText;
    private ImageView hazardIcon;


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
        // Switch to allow for future functionality
        switch (item.getItemId()) {
            case R.id.ToolbarMenu_back:
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
        setData();

        populateListView();
    }

    void populateListView() {
        violationsList = findViewById(R.id.violationList);
        DatabaseManager dbManager = DatabaseManager.getInstance();
        ArrayList<Violation> violations = dbManager.getViolations(inspection.getId());
        ViolationsListAdapter adapter = new ViolationsListAdapter(
            this,
            R.layout.violation_row,
            violations
        );
        violationsList.setAdapter(adapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void getData(Intent intent) {
        if (getIntent().hasExtra("inspectionId")) {
            Bundle extras = intent.getExtras();
            assert extras != null;
            int inspectionId = extras.getInt("inspectionId");
            DatabaseManager dbManager = DatabaseManager.getInstance();
            inspection = dbManager.getInspection(inspectionId);
        }
    }

    private void setData() {
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

        inspectCritical.setText(getString(R.string.Inspection_num_critical_issues) + criticalIssues);
        inspectNonCritical.setText(getString(R.string.Inspection_num_non_critical_issues) + nonCriticalIssues);
        inspectText.setText(getString(R.string.Inspection_hazard_level) + inspection.getHazardRatingString());

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