package com.example.cmpt276project.model;
import androidx.annotation.NonNull;

import java.util.ArrayList;
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
        }
        return instance;
    }

    private RestaurantManager(){
        // Ensure for singleton
    }

//    private int length=0;
//    private HashMap<String, Restaurant> hashMap = new HashMap<>();

    public int getLength() {
        return restaurants.size();
    }
    public void addRestaurant(Restaurant restaurant) {
        // Array List implementation
        restaurants.add(restaurant);

//        // HashMap Implementation
//        hashMap.put(name, restaurant);
//        length ++;
    }
    public Restaurant get(int position) {
        // Array List implementation
        return restaurants.get(position);

        // Hash Map implementation
//        hashMap.get(name);
    }

    public void removeRestaurant(Restaurant restaurant) {
        // Array List implementation
        restaurants.remove(restaurant);

        // Hash Map implementation
//        hashMap.remove(name);
//        length--;
    }

    @Override
    public Iterator<Restaurant> iterator() {
        return restaurants.iterator();
    }
}