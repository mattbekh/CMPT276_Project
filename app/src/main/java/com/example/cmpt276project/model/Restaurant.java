package com.example.cmpt276project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class represents a restaurant and all the inspections it has been subject to.
 */

// implement Serializable to pass object through intent
public class Restaurant {

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

    public Inspection[] getInspectionList() {
        return (Inspection[]) inspectionList.toArray();
    }

    public Inspection getTopInspection(){

//        if(inspectionList.isEmpty()){
//            // Check for not empty
//            // TODO : Default for never had an inspection?
//        }
        return inspectionList.get(0);
    }
    public void addInspection(Inspection inspection) {
        inspectionList.add(inspection);
    }

    public void sort(Comparator<Inspection> comparator) {
        inspectionList.sort(comparator);
    }
}
