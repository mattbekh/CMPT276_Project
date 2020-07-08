package com.example.cmpt276project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import com.example.cmpt276project.R;
import com.example.cmpt276project.model.RestaurantManager;

/**
    This class creates and populates a RecyclerView which holds all restaurants with their details
 */
public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView restaurantList;
    RestaurantManager restaurants;

    public static Intent makeIntent(Context context) {
        return new Intent(context, RestaurantListActivity.class);
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
        // Could support more buttons
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
        setupToolbar();

        restaurants = RestaurantManager.getInstance();
        restaurants.sort();

        populateRecyclerView();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.Restaurant_first_activity_title);
    }

    private void populateRecyclerView() {

        restaurantList = findViewById(R.id.restaurantList);
        RestaurantListAdapter adapter = new RestaurantListAdapter(this,restaurants);
        restaurantList.setAdapter(adapter);
        restaurantList.setLayoutManager(new LinearLayoutManager(this));
    }
}