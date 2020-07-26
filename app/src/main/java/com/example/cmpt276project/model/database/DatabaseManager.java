package com.example.cmpt276project.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.cmpt276project.model.CsvDataParser;

public class DatabaseManager {

    private static final String DATABASE_NAME = "RestaurantDB";
    private static final int DATABASE_VERSION = 2;

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private static DatabaseManager instance;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Cannot get DatabaseManager instance before initialization");
        }
        return instance;
    }

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
//            CsvDataParser.readUpdatedRestaurantData();
        }
    }

    private DatabaseManager(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertToRestaurants(
            String id,
            String name,
            String address,
            String city,
            double latitude,
            double longitude
    ) {
        ContentValues values = new ContentValues();
        values.put(RestaurantTable.FIELD_ID, id);
        values.put(RestaurantTable.FIELD_NAME, name);
        values.put(RestaurantTable.FIELD_ADDRESS, address);
        values.put(RestaurantTable.FIELD_CITY, city);
        values.put(RestaurantTable.FIELD_LATITUDE, latitude);
        values.put(RestaurantTable.FIELD_LONGITUDE, longitude);

        return db.insert(RestaurantTable.NAME, null, values);
    }

    public long insertToInspections(
            int inspectionId,
            String restaurantId,
            int date,
            String type,
            String hazardRating,
            int numCritical,
            int numNonCritical
    ) {
        ContentValues values = new ContentValues();
        values.put(InspectionTable.FIELD_ID, inspectionId);
        values.put(InspectionTable.FIELD_RESTAURANT_ID, restaurantId);
        values.put(InspectionTable.FIELD_DATE, date);
        values.put(InspectionTable.FIELD_TYPE, type);
        values.put(InspectionTable.FIELD_HAZARD_RATING, hazardRating);
        values.put(InspectionTable.FIELD_CRITICAL_ISSUES, numCritical);
        values.put(InspectionTable.FIELD_NON_CRITICAL_ISSUES, numNonCritical);

        return db.insert(InspectionTable.NAME, null, values);
    }

    public long insertToViolations(
            int inspectionId,
            int code,
            String description,
            int isCritical,
            int isRepeat
    ) {
        ContentValues values = new ContentValues();
        values.put(ViolationTable.FIELD_INSPECTION_ID, inspectionId);
        values.put(ViolationTable.FIELD_CODE, code);
        values.put(ViolationTable.FIELD_DESCRIPTION, description);
        values.put(ViolationTable.FIELD_IS_CRITICAL, isCritical);
        values.put(ViolationTable.FIELD_IS_REPEAT, isRepeat);

        return db.insert(ViolationTable.NAME, null, values);
    }

    // Private class for DatabaseHelper
    // Must be private to ensure DatabaseManager is singleton object
    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(RestaurantTable.CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE + RestaurantTable.NAME);
            onCreate(db);
        }
    }
}
