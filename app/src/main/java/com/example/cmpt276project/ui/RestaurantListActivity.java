package com.example.cmpt276project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
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
public class RestaurantListActivity extends AppCompatActivity implements SearchAndFilterFragment.UpdateFilterListener {

    private RecyclerView restaurantList;
    RestaurantManager manager;
    RestaurantListAdapter restaurantListAdapter;

    public static Intent makeIntent(Context context) {
        return new Intent(context, RestaurantListActivity.class);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        manager = RestaurantManager.getInstance();
//        manager.sortByRestaurantName();
//        populateRecyclerView();
//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//
//       // if(intent.hasExtra("favourited")){
//
//         //   Log.v("RestaurantListActivity", "onResume called");
//          //  assert extras != null;
//           // String restaurantId = extras.getString("restaurantId");
//          //  DatabaseManager dbManager = DatabaseManager.getInstance();
//           // Restaurant restaurant = dbManager.getRestaurant(restaurantId);
//           // dbManager.updateRestaurantFav(restaurantId, 1);
//    }

    public void updateFav(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

   //     if(intent.hasExtra("restaurantId")) {

   //         Log.v("RestaurantListActivity", "update fav");
   //         assert extras != null;
    //        Boolean favourite = extras.getBoolean("fav", true);
    //        String restaurantId = extras.getString("restaurantId");
    //        DatabaseManager dbManager = DatabaseManager.getInstance();
     //       Restaurant restaurant = dbManager.getRestaurant(restaurantId);

     //        if(favourite) {
     //            restaurant.setFavourite(1);
     //            dbManager.updateRestaurantFav(restaurantId, 1);
                 Log.v("RestaurantListActivity", "isFav");
       //      }
      //       else{
      //           restaurant.setFavourite(0);
       //          dbManager.updateRestaurantFav(restaurantId, 0);
       //      }
      //  }
    }

    @Override
    public void onResume(){
        super.onResume();
    //    doUpdate();
        updateRecyclerView();
        Log.v("RestaurantListActivity", "onResume");

    }

    public void doUpdate() {

        manager = RestaurantManager.getInstance();
        manager.sortByRestaurantName();
        populateRecyclerView();

        Log.v("RestaurantListActivity", "onResume");


        if (manager.doesListNeedUpdate()) {
            populateRecyclerView();
            manager.setListNeedUpdate();
        }
    }

    // Setup toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);

        MenuItem viewMapItem = menu.findItem(R.id.ToolbarMenu_switch_context);
//        MenuItem searchMapItem = menu.findItem(R.id.ToolbarMenu_search);

        viewMapItem.setVisible(true);
        viewMapItem.setTitle(R.string.RestaurantListActivity_toolbar_map_btn_text);

//        SearchView searchView = (SearchView) searchMapItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

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
            case R.id.ToolbarMenu_search:
                FragmentManager manager = getSupportFragmentManager();
                SearchAndFilterFragment dialog = new SearchAndFilterFragment();
                dialog.show(manager, "SearchAndFilterActivity");
                return true;
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


        restaurantListAdapter = new RestaurantListAdapter(this, manager);
        restaurantListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
        restaurantList.setAdapter(restaurantListAdapter);
        restaurantList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateRecyclerView(){

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

      //  if(intent.hasExtra("restaurantPos")){
            Log.v("RestaurantListActivity", "position");
            restaurantList = findViewById(R.id.restaurantList);
            RestaurantListAdapter adapter = (RestaurantListAdapter) restaurantList.getAdapter();
        //  int position = extras.getInt("restaurantPos");
            adapter.notifyItemChanged(0);
      //  }

    }

    @Override
    public void updateFilter() {
//        populateRecyclerView();
        onResume();
    }
}