package com.example.cmpt276project.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * This class responsible for calling all restaurant related information and sorting restaurants by name
 */
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

    // return the length of restaurant list
    public int getLength() {
        return restaurantList.size();
    }

    // add new restaurant to restaurant list
    public void addRestaurant(Restaurant restaurant) {
        restaurantList.add(restaurant);
    }

    // return restaurant by position
    public Restaurant get(int position) {
        return restaurantList.get(position);
    }

    // delete restaurant from restaurant list
    public void removeRestaurant(Restaurant restaurant) {
        restaurantList.remove(restaurant);

    }

    // sort restaurants by name
    public void sort() {
        Collections.sort(restaurantList, new SortByName());
    }

    // override comparator function to sort restaurants by name
    static class SortByName implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    // return restaurant by tracking number
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
