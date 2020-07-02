package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cmpt276project.R;
import com.example.cmpt276project.ui.RestaurantListAdapter;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView restaurantList;

    private String restaurantNames[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        populateRecyclerView();


    }

    private void populateRecyclerView() {
        restaurantNames = getResources().getStringArray(R.array.restaurant_names);

        restaurantList = findViewById(R.id.restaurantList);

        RestaurantListAdapter adapter = new RestaurantListAdapter(this,restaurantNames);
        restaurantList.setAdapter(adapter);
        restaurantList.setLayoutManager(new LinearLayoutManager(this));
    }
}