package com.example.cmpt276project.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 *  This class is used for clustering
 * */

public class AllRestaurant implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final String mHazard;
    private final String mId;


    public AllRestaurant() {
        mId = "";
        mPosition = null;
        mTitle = "";
        mSnippet = "";
        mHazard = "";
    }

    public AllRestaurant(double lat, double lng, String title, String hazard) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = "";
        mHazard = hazard;
        mId = "";
    }

    public AllRestaurant(double lat, double lng, String title, String snippet, String hazard, String id) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mHazard = hazard;
        mId = id;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public String getHazard() {
        return mHazard;
    }

    public String getId() {
        return mId;
    }
}