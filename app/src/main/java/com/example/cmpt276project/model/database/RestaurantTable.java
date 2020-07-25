package com.example.cmpt276project.model.database;

public class RestaurantTable {

    public static final String NAME = "Restaurants";
    
    public static final String FIELD_ID = "RestaurantId";
    public static final String FIELD_NAME = "Name";
    public static final String FIELD_ADDRESS = "Address";
    public static final String FIELD_CITY = "City";
    public static final String FIELD_LATITUDE = "Latitude";
    public static final String FIELD_LONGITUDE = "Longitude";

    public static final int COL_ID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_ADDRESS = 2;
    public static final int COL_CITY = 3;
    public static final int COL_LATITUDE = 4;
    public static final int COL_LONGITUDE = 5;
    
    public static final String[] FIELDS = new String[] {
            FIELD_ID,
            FIELD_NAME,
            FIELD_ADDRESS,
            FIELD_CITY,
            FIELD_LATITUDE,
            FIELD_LONGITUDE
    };

    public static final String CREATE =
            "CREATE TABLE " + NAME + " (" +
                FIELD_ID + " INT PRIMARY KEY, " +
                FIELD_NAME + " TEXT NOT NULL," +
                FIELD_ADDRESS + " TEXT NOT NULL," +
                FIELD_CITY + " TEXT NOT NULL," +
                FIELD_LATITUDE + " REAL NOT NULL," +
                FIELD_LONGITUDE + " REAL NOT NULL" +
            ");";
}
