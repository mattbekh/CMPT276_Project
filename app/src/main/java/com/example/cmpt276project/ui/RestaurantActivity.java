package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Restaurant;

public class RestaurantActivity extends AppCompatActivity {

    TextView restaurantName;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);

        restaurantName = findViewById(R.id.restaurantName);

        getData();
        setData();
    }

    private void getData() {
        if(getIntent().hasExtra("restaurant")){


        }
    }

    private void setData() {
        restaurantName.setText(name);
    }

}