package com.example.cmpt276project.model;

import android.content.res.Resources;

import androidx.annotation.Nullable;

import com.example.cmpt276project.App;
import com.example.cmpt276project.R;
import com.example.cmpt276project.model.database.DatabaseManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * This class represents a restaurant and all the inspections it has been subject to.
 */
public class Restaurant implements ClusterItem {

    public enum RestaurantName { MCDONALDS, WENDYS, BLENZ, PIZZAHUT, AW, TIMS, STARBUCKS, ELEVEN, BOSTON, SUBWAY, UNKNOWN }

    private String id;
    private String name;
    private String address;
    private String city;
    private double latitude;
    private double longitude;
    private boolean isFavourite = false;
    private Inspection mostRecentInspection;

    public Restaurant(String id,
                      String name,
                      String address,
                      String city,
                      double latitude,
                      double longitude,
                      int favourite)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isFavourite = favourite == 1;
        this.mostRecentInspection = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getHazardResource() {
        if (mostRecentInspection == null) {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            mostRecentInspection = dbManager.getMostRecentInspection(id);
        }
        if (mostRecentInspection == null) {
            return "hazard_unknown";
        }
        switch (mostRecentInspection.getHazardRating()) {
            case LOW:
                return "hazard_low";
            case MODERATE:
                return "hazard_mid";
            case HIGH:
                return "hazard_high";
            default:
                return "hazard_unknown";
        }
    }

    @Nullable
    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Nullable
    @Override
    public String getTitle() {
        return name;
    }

    @Nullable
    @Override
    public String getSnippet() {
        if (mostRecentInspection == null) {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            mostRecentInspection = dbManager.getMostRecentInspection(id);
        }
        Resources res = App.resources();
        String noInspections = res.getString(R.string.Inspection_no_inspections_found);
        String hazardLevel = res.getString(R.string.Inspection_hazard_level);
        if (mostRecentInspection == null) {
           return String.format("%s %s%s", address, hazardLevel, noInspections);
        }
        return String.format("%s %s%s", address, hazardLevel, mostRecentInspection.getHazardRatingString());
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public RestaurantName getRestaurantName() {
        name = name.toLowerCase();
        for (RestaurantName restaurantName : RestaurantName.values()) {
            if (name.matches(getKeywordPattern(restaurantName))) {
                return restaurantName;
            }
        }
        String errorMessage = String.format("Failing description [%s]", name);
        throw new IllegalArgumentException(errorMessage);
    }


    private static String getKeywordPattern(RestaurantName restaurantName){
        switch(restaurantName){
            case MCDONALDS:
                return ".*((mcdonald)).*";
            case WENDYS:
                return ".*((wendy)).*";
            case BLENZ:
                return ".*((blenz)).*";
            case PIZZAHUT:
                return ".*((pizza hut)).*";
            case AW:
                return ".*((a&w)|(a & w #)).*";
            case TIMS:
                return ".*((horton)).*";
            case STARBUCKS:
                return ".*((starbucks)).*";
            case ELEVEN:
                return ".*((eleven)).*";
            case BOSTON:
                return ".*((boston)).*";
            case SUBWAY:
                return ".*((subway)).*";
            case UNKNOWN:
                return ".*(( )).*";
            default:
                String errorMessage = String.format("Illegal category [%s]", restaurantName.toString());
                throw new IllegalStateException(errorMessage);
        }
    }
}
