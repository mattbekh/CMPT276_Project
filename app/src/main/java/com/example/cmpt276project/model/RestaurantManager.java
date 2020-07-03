package com.example.cmpt276project.model;
import androidx.annotation.NonNull;

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
        }
        return instance;
    }

    private RestaurantManager(){
        // Ensure for singleton
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
