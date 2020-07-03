package com.example.cmpt276project.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class RestaurantManager implements Iterable<Restaurant>{
    private List<Restaurant> restaurantList = new ArrayList<>();

    // Singleton Support
    private static RestaurantManager instance;
    public static RestaurantManager getInstance(){
        if(instance == null){
            instance = new RestaurantManager();
            CsvDataParser.readRestaurantData(instance);
        }
        return instance;
    }

    private RestaurantManager(){
        // Ensure for singleton
    }

    public int getLength() {
        return restaurantList.size();
    }
    public void addRestaurant(Restaurant restaurant) {
        restaurantList.add(restaurant);
    }
    public Restaurant get(int position) {
        return restaurantList.get(position);
    }

    public void removeRestaurant(Restaurant restaurant) {
        restaurantList.remove(restaurant);

    }

    public void sort() {
        Collections.sort(restaurantList, new SortByName());
    }

    static class SortByName implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public Restaurant getRestaurantByTrackingNumber(String trackingNumber) {
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getTrackingNumber().equals(trackingNumber)) {
                return restaurant;
            }
        }
        String errorMessage = String.format("Tracking number [%s] not found.", trackingNumber);
        throw new IllegalArgumentException(errorMessage);
    }

    @NonNull
    @Override
    public Iterator<Restaurant> iterator() {
        return restaurantList.iterator();
    }
}