package com.example.cmpt276project.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cmpt276project.model.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * This class responsible for calling all restaurant related information and sorting restaurants by name
 */
public class RestaurantManager implements Iterable<Restaurant> {

    private ArrayList<Restaurant> restaurantList;
    private boolean doesMapNeedUpdate;
    private boolean doesListNeedUpdate;

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
        this.restaurantList = DatabaseManager.getInstance().getRestaurants();
    }

    // return whole restaurant list
    public ArrayList<Restaurant> getRestaurantList() {
        return instance.restaurantList;
    }

    // Sort restaurants by name
    public void sortByRestaurantName() {
        restaurantList.sort(new SortAscendingByName());
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
            return o1.getId().compareTo(o2.getId());
        }
    }

    // Return restaurant by tracking number
    public Restaurant getRestaurantByTrackingNumber(String trackingNumber) {
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getId().equals(trackingNumber)) {
                return restaurant;
            }
        }
        String errorMessage = String.format("Tracking number [%s] not found.", trackingNumber);
        throw new IllegalArgumentException(errorMessage);
    }

    public Restaurant getRestaurantByLatLng (double Lat, double Lng) {
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getLatitude() == Lat && restaurant.getLongitude() == Lng) {
                return restaurant;
            }
        }
        String errorMessage = String.format("Tracking number [%s] not found.", Lat, Lng);
        throw new IllegalArgumentException(errorMessage);
    }

    public void updateData() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.update();
        instance.restaurantList = dbManager.getRestaurants();
        instance.sortByRestaurantName();
    }

    public void applyFilter() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        instance.restaurantList = dbManager.getRestaurants();
        instance.sortByRestaurantName();
        requireUpdate();
    }
    public boolean doesMapNeedUpdate() {
        return doesMapNeedUpdate;
    }

    public boolean doesListNeedUpdate() {
        return doesListNeedUpdate;
    }

    public void requireUpdate() {
        doesListNeedUpdate = true;
        doesMapNeedUpdate = true;
    }
    public void setListNeedUpdate() {
        doesListNeedUpdate = false;
    }
    public void setMapNeedUpdate() {
        doesMapNeedUpdate = false;
    }


    @NonNull
    @Override
    public Iterator<Restaurant> iterator() {
        return restaurantList.iterator();
    }
}
