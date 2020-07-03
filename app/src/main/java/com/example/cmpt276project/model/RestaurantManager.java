package com.example.cmpt276project.model;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cmpt276project.App;
import com.example.cmpt276project.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class RestaurantManager implements Iterable<Restaurant>{
    private List<Restaurant> restaurants = new ArrayList<>();

    // Singleton Support
    private static RestaurantManager instance;
    public static RestaurantManager getInstance(){
        if(instance == null){
            instance = new RestaurantManager();
            readRestaurantData();
        }
        return instance;
    }

    private RestaurantManager(){
        // Ensure for singleton
    }

    private static void readRestaurantData() {
        InputStream is = App.resources().openRawResource(R.raw.restaurants_itr1);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line;
        while(true) {
            try {
                // step over headers
                reader.readLine();

                while ((line = reader.readLine()) != null) {
                    // split by ','
                    String[] tokens = line.split(",");

                    // read the data
                    Restaurant tmpData = new Restaurant(
                            // String trackingNumber
                            tokens[0],
                            // String name
                            tokens[1],
                            // String address,
                            tokens[2],
                            // String city
                            tokens[3],
                            // double latitude
                            Double.parseDouble(tokens[5]),
                            // double longitude
                            Double.parseDouble(tokens[6])
                            );

                    // put data into instance
                    instance.addRestaurant(tmpData);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public int getLength() {
        return restaurants.size();
    }
    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }
    public Restaurant get(int position) {
        return restaurants.get(position);
    }

    public void removeRestaurant(Restaurant restaurant) {
        restaurants.remove(restaurant);

    }

    public void sort() {
        Collections.sort(restaurants, new SortByName());
    }

    static class SortByName implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    @Override
    public Iterator<Restaurant> iterator() {
        return restaurants.iterator();
    }
}