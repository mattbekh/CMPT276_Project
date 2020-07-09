package com.example.cmpt276project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;

/**
 * This class creates a ListView and populates it with data from a specific restaurant.
 * This class creates OnClick intent for each Inspection inside the Restaurant which
 * contains reference to restaurant & inspection
 */
public class RestaurantActivity extends AppCompatActivity {

    private RestaurantManager manager;
    private Restaurant restaurant;
    private int restaurantPos;

    private TextView restaurantName;
    private TextView restaurantAddress;
    private TextView restaurantGPS;

    private ListView inspectionList;


    public static Intent makeIntent(Context context) {
        return new Intent(context, RestaurantActivity.class);
    }

    // Setup toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Switch to allow for future functionality
        switch (item.getItemId()) {
            case R.id.backIcon :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);

        manager = RestaurantManager.getInstance();

        setupToolbar();
        loadData();
        displayData();
        populateListView();
        registerClickCallback();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void loadData() {
        Intent intent = getIntent();
        if (intent.hasExtra("restaurant")) {
            Bundle extras = intent.getExtras();
            assert extras != null;
            restaurantPos = extras.getInt("restaurant");
            restaurant = manager.get(restaurantPos);
        }
    }

    private void displayData() {
        restaurantName = findViewById(R.id.restaurantName);
        restaurantAddress = findViewById(R.id.restaurantAddress);
        restaurantGPS = findViewById(R.id.restaurantGPS);

        String fullAddress = restaurant.getAddress() + ", " + restaurant.getCity();
        String coordinates = restaurant.getLatitude() + " , " + restaurant.getLongitude();
        restaurantName.setText(restaurant.getName());
        restaurantAddress.setText(fullAddress);
        restaurantGPS.setText(coordinates);
    }

    private void populateListView() {
        inspectionList = findViewById(R.id.inspectionList);

        // Sort in descending order
        restaurant.sort(new Inspection.DateDescendingComparator());

        InspectionListAdapter adapter = new InspectionListAdapter(this,
                                                                  R.layout.inspection_row,
                                                                  restaurant.getInspectionList());
        inspectionList.setAdapter(adapter);
    }

    private void registerClickCallback() {
        inspectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id)
            {
                Intent intent = InspectionActivity.makeIntent(RestaurantActivity.this);
                // put index of Restaurant as extra
                intent.putExtra("restaurant", restaurantPos);
                // put index of Inspection as extra
                intent.putExtra("inspection", position);
                startActivity(intent);
            }
        });
    }
}