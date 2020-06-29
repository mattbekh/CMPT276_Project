package com.example.cmpt276project.model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class represents a restaurant and all the inspections it has been subject to.
 */
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

    public void addInspection(Inspection inspection) {
        inspectionList.add(inspection);
    }

    public void sort(Comparator<Inspection> comparator) {
        inspectionList.sort(comparator);
    }
}
