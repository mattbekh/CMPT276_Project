package com.example.cmpt276project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class creates a ListView and populates it with data from a specific restaurant.
 * This class creates OnClick intent for each Inspection inside the Restaurant which
 * contains reference to restaurant & inspection
 */
public class RestaurantActivity extends AppCompatActivity {

    private RestaurantManager manager;
    private Restaurant restaurant;
    private ArrayList<Inspection> inspections;
    private int restaurantPos;

    private TextView restaurantName;
    private TextView restaurantAddress;
    private TextView restaurantGPS;

    private boolean favouriteToggled;

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


    //Takes user back to RestaurantList
    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Switch to allow for future functionality
        switch (item.getItemId()) {
            case R.id.ToolbarMenu_back:
                if(favouriteToggled){

                }
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void loadData() {
        Intent intent = getIntent();
        if (intent.hasExtra("restaurantId")) {
            Bundle extras = intent.getExtras();
            assert extras != null;
            String restaurantId = extras.getString("restaurantId");
            DatabaseManager dbManager = DatabaseManager.getInstance();
            restaurant = dbManager.getRestaurant(restaurantId);
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

        Switch sb = (Switch)findViewById(R.id.favSwitch);
        sb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                restaurant.setFavourite(1);
                favouriteToggled = true;
                DatabaseManager dbManager = DatabaseManager.getInstance();
                dbManager.updateRestaurantFav(restaurant.getId(), 1);
            }

            else{
                restaurant.setFavourite(0);
                favouriteToggled = false;
                DatabaseManager dbManager = DatabaseManager.getInstance();
                dbManager.updateRestaurantFav(restaurant.getId(), 0);
            }

        });


        restaurantGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = MapsActivity.makeIntent(RestaurantActivity.this,false);

                intent.putExtra("restaurantId", restaurant.getId());
                startActivity(intent);
                finish();



            }
        });
    }

    private void populateListView() {
        inspectionList = findViewById(R.id.inspectionList);

        DatabaseManager dbManager = DatabaseManager.getInstance();
        inspections = dbManager.getInspections(restaurant.getId());

        InspectionListAdapter adapter = new InspectionListAdapter(
                this,
                R.layout.inspection_row,
                inspections
        );

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
                intent.putExtra("restaurantId", restaurant.getId());
                // put index of Inspection as extra
                int inspectionId = inspections.get(position).getId();
                intent.putExtra("inspectionId", inspectionId);
                startActivity(intent);
            }
        });
    }

}