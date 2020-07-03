package com.example.cmpt276project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.Violation;

import java.util.Date;
import java.util.GregorianCalendar;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView restaurantList;
    RestaurantManager restaurants;

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
        setContentView(R.layout.activity_restaurant_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        restaurants = RestaurantManager.getInstance();


        // Adding test data
        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Pattullo A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
//        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Matts A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
//        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Ronnys A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
//        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Grants A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
//        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Sevenas A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
//        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Test A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
        Restaurant restaurantTest = restaurants.get(0);
        Inspection inspectionTest = new Inspection("SDF0-8HKP7E","Routine","Moderate",new GregorianCalendar(2019, 11, 26));
        inspectionTest.addViolation(new Violation(101,false,false,"Plans/construction/alterations not in accordance with the Regulation [s. 3; s. 4]"));
        restaurantTest.addInspection(inspectionTest);
        restaurantTest.getTopInspection().addViolation(new Violation(101,false,false,"Plans/construction/alterations not in accordance with the Regulation [s. 3; s. 4]"));


        populateRecyclerView();
    }

    private void populateRecyclerView() {

        restaurantList = findViewById(R.id.restaurantList);
        RestaurantListAdapter adapter = new RestaurantListAdapter(this,restaurants);
        restaurantList.setAdapter(adapter);
        restaurantList.setLayoutManager(new LinearLayoutManager(this));
    }
}