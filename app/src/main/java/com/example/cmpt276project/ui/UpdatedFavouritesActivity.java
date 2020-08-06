package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.RestaurantManager;

public class UpdatedFavouritesActivity extends AppCompatActivity {

    private RecyclerView restaurantList;
    RestaurantManager manager;

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, UpdatedFavouritesActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_favourites);

        manager = RestaurantManager.getInstance();
        manager.sortByRestaurantName();

        setupButton();

        populateRecyclerView();
    }

    private void setupButton(){
        Button done = (Button)findViewById(R.id.exitUpdate);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MapsActivity.makeIntent(UpdatedFavouritesActivity.this, false));
                finish();
            }
        });
    }

    private void populateRecyclerView() {

    }
}