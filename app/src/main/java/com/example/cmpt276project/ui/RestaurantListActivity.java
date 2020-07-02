package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView restaurantList;
    RestaurantManager restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        restaurants = RestaurantManager.getInstance();

        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Pattullo A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Matts A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Ronnys A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Grants A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));
        restaurants.addRestaurant(new Restaurant("SDFO-8HKP7E","Sevenas A&W","12808 King George Blvd","Surrey",49.20610961,-122.8668064));

        populateRecyclerView();


    }

    private void populateRecyclerView() {

        restaurantList = findViewById(R.id.restaurantList);

        RestaurantListAdapter adapter = new RestaurantListAdapter(this,restaurants);
        restaurantList.setAdapter(adapter);
        restaurantList.setLayoutManager(new LinearLayoutManager(this));
    }
}