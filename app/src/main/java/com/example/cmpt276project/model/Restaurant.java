package com.example.cmpt276project.model;

import androidx.annotation.NonNull;

import com.example.cmpt276project.ui.RestaurantActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * This class represents a restaurant and all the inspections it has been subject to.
 */
public class Restaurant implements Iterable<Inspection> {

    public enum RestaurantName{MCDONALDS, WENDYS, TOPTOWN, PIZZAHUT, AW, TIMS, STARBUCKS, ELEVEN, LEEYUEN, UNKNOWN};
    private RestaurantName restaurantName;

    private String trackingNumber;
    private String name;
    private String address;
    private String city;
    private double latitude;
    private double longitude;
    private ArrayList<Inspection> inspectionList;

    public Restaurant(String trackingNumber,
                      String name,
                      String address,
                      String city,
                      double latitude,
                      double longitude)
    {
        this.trackingNumber = trackingNumber;
        this.name = name;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.inspectionList = new ArrayList<>();
        this.restaurantName = getRestaurantFromName(name);
    }

    public String getTrackingNumber() {
        return trackingNumber;
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

    public RestaurantName getRestaurantName() {
        return restaurantName;
    }

    private RestaurantName getRestaurantFromName(String name) {
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
                return ".*((mcdonalds)).*";
            case WENDYS:
                return ".*((wendys)).*";
            case TOPTOWN:
                return ".*((top)|(town)).*";
            case PIZZAHUT:
                return ".*((hut)).*";
            case AW:
                return ".*((a&w)).*";
            case TIMS:
                return ".*((tim)|(hortons)).*";
            case STARBUCKS:
                return ".*((starbucks)).*";
            case ELEVEN:
                return ".*((7)).*";
            case LEEYUEN:
                return ".*((lee)|(yuen)).*";
            case UNKNOWN:
                return ".*(( )).*";
            default:
                String errorMessage = String.format("Illegal category [%s]", restaurantName.toString());
                throw new IllegalStateException(errorMessage);
        }
    }

    public Inspection getInspectionByIndex(int i) {
        if(inspectionList.size() == 0) {
            return new Inspection(
                "EMPTY",
                "Routine",
                "Low",
                new GregorianCalendar(2019, 11, 26)
            );
        }
        return inspectionList.get(i);
    }

    @NonNull
    @Override
    public Iterator<Inspection> iterator() {
        return inspectionList.iterator();
    }

    public void addInspection(Inspection inspection) {
        inspectionList.add(inspection);
    }

    public void sort(Comparator<Inspection> comparator) {
        inspectionList.sort(comparator);
    }

    public ArrayList<Inspection> getInspectionList() {
        return inspectionList;
    }
}
