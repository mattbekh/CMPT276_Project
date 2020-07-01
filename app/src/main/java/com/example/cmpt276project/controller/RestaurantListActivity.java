package com.example.cmpt276project.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cmpt276project.R;
import com.example.cmpt276project.ui.RestaurantListAdapter;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView restaurantList;

    private String restaurantNames[];
    private int image = R.drawable.fork_spoon_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        restaurantNames = getResources().getStringArray(R.array.restaurant_names);

        restaurantList = findViewById(R.id.restaurantList);

        RestaurantListAdapter adapter = new RestaurantListAdapter(this,restaurantNames);
        restaurantList.setAdapter(adapter);
        restaurantList.setLayoutManager(new LinearLayoutManager(this));

    }
}