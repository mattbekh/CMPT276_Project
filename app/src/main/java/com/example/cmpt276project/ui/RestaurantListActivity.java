package com.example.cmpt276project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.database.DatabaseManager;

import java.util.Objects;

/**
    This class creates and populates a RecyclerView which holds all restaurants with their details
 */
public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView restaurantList;
    RestaurantManager manager;
    public static Intent makeIntent(Context context) {
        return new Intent(context, RestaurantListActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        manager = RestaurantManager.getInstance();
        manager.sortByRestaurantName();

        populateRecyclerView();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

       // if(intent.hasExtra("favourited")){

         //   Log.v("RestaurantListActivity", "onResume called");
          //  assert extras != null;
           // String restaurantId = extras.getString("restaurantId");
          //  DatabaseManager dbManager = DatabaseManager.getInstance();
           // Restaurant restaurant = dbManager.getRestaurant(restaurantId);
           // dbManager.updateRestaurantFav(restaurantId, 1);



    }


    // Setup toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);

        MenuItem viewMapItem = menu.findItem(R.id.ToolbarMenu_switch_context);
        viewMapItem.setVisible(true);
        viewMapItem.setTitle(R.string.RestaurantListActivity_toolbar_map_btn_text);

        return true;
    }

    //Exit Application when back Button pressed
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ToolbarMenu_back:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);
                return true;
            case R.id.ToolbarMenu_switch_context:
                startActivity(MapsActivity.makeIntent(this, false));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        setupToolbar();

        manager = RestaurantManager.getInstance();
        manager.sortByRestaurantName();

        populateRecyclerView();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void populateRecyclerView() {

        restaurantList = findViewById(R.id.restaurantList);
        RestaurantListAdapter adapter = new RestaurantListAdapter(this, manager);
        restaurantList.setAdapter(adapter);
        restaurantList.setLayoutManager(new LinearLayoutManager(this));
    }
}