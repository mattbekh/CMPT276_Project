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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;

import java.util.Comparator;
import java.util.GregorianCalendar;

/**
 * This class creates a ListView and populates it with data from a specific restaurant.
 * This class creates OnClick intent for each Inspection inside the Restaurant which
 * contains reference to restaurant & inspection
 */
public class RestaurantActivity extends AppCompatActivity {

    private RestaurantManager manager;
    private Intent intent;
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
        switch (item.getItemId()){
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
        setupToolbar();


        intent = getIntent();
        manager = RestaurantManager.getInstance();

        getData(intent);
        setData(restaurant);

        populateListView();
        registerClickCallback();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void populateListView() {
        inspectionList = findViewById(R.id.inspectionList);

        // Sort in descending order
        Inspection.DateDescendingComparator comparator = new Inspection.DateDescendingComparator();
        restaurant.sort(comparator);

        InspectionListAdapter adapter = new InspectionListAdapter(this,R.layout.inspection_row,restaurant.getInspectionList());
        inspectionList.setAdapter(adapter);

    }

    private void registerClickCallback() {
        inspectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = InspectionActivity.makeIntent(RestaurantActivity.this);
                // put index of Restaurant as extra
                intent.putExtra("restaurant",restaurantPos);
                // put index of Inspection as extra
                intent.putExtra("inspection",position);
                startActivity(intent);
            }
        });
    }

    private void setData(Restaurant restaurant) {
        restaurantName = findViewById(R.id.restaurantName);
        restaurantAddress = findViewById(R.id.restaurantAddress);
        restaurantGPS = findViewById(R.id.restaurantGPS);

        restaurantName.setText(restaurant.getName());
        restaurantAddress.setText(restaurant.getAddress()+", "+restaurant.getCity());
        restaurantGPS.setText(""+restaurant.getLatitude()+" , "+restaurant.getLongitude());
    }

    private void getData(Intent it) {
        if (getIntent().hasExtra("restaurant")) {
            Bundle extras = it.getExtras();
            assert extras != null;
            restaurantPos = extras.getInt("restaurant");
            restaurant = manager.get(restaurantPos);
        }
    }
}