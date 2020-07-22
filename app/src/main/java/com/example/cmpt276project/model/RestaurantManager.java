package com.example.cmpt276project.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * This class responsible for calling all restaurant related information and sorting restaurants by name
 */
public class RestaurantManager implements Iterable<Restaurant> {

    private ArrayList<Restaurant> restaurantList;

    // Singleton Support
    private static RestaurantManager instance;
    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    // Ensure private for singleton
    private RestaurantManager() {
        this.restaurantList = CsvDataParser.readUpdatedRestaurantData();
    }

    // return whole restaurant list
    public ArrayList<Restaurant> getRestaurantList() {
        return instance.restaurantList;
    }

    // Return the length of restaurant list
    public int getLength() {
        return restaurantList.size();
    }

    // Add new restaurant to restaurant list
    public void addRestaurant(Restaurant restaurant) {
        restaurantList.add(restaurant);
    }

    // Return restaurant by position
    public Restaurant get(int position) {
        return restaurantList.get(position);
    }

    // Delete restaurant from restaurant list
    public void removeRestaurant(Restaurant restaurant) {
        restaurantList.remove(restaurant);
    }

    // Sort restaurants by name
    public void sortByRestaurantName() {
        Collections.sort(restaurantList, new SortAscendingByName());
    }

    // Override comparator function to sort restaurants by name
    static class SortAscendingByName implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    static class SortAscendingByTrackingNumber implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            return o1.getTrackingNumber().compareTo(o2.getTrackingNumber());
        }
    }

    // Return restaurant by tracking number
    public Restaurant getRestaurantByTrackingNumber(String trackingNumber) {
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getTrackingNumber().equals(trackingNumber)) {
                return restaurant;
            }
        }
        String errorMessage = String.format("Tracking number [%s] not found.", trackingNumber);
        throw new IllegalArgumentException(errorMessage);
    }

    public void updateData() {
        instance.restaurantList = CsvDataParser.readUpdatedRestaurantData();
        instance.sortByRestaurantName();
    }

    @NonNull
    @Override
    public Iterator<Restaurant> iterator() {
        return restaurantList.iterator();
    }
}
