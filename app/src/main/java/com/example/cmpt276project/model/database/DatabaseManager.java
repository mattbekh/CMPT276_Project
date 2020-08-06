package com.example.cmpt276project.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.cmpt276project.model.CsvDataParser;
import com.example.cmpt276project.model.DateHelper;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.Violation;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class DatabaseManager {

    private static final String DATABASE_NAME = "RestaurantDB";
    private static final int DATABASE_VERSION = 2;
    private static final int IS_FAVOURITE = 1;

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private static DatabaseManager instance;

    private boolean updateNeeded = false;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Cannot get DatabaseManager instance before initialization");
        }
        return instance;
    }

    public static void initialize(Context context) {
        if (instance == null) {
            File dbFilepath = context.getDatabasePath(DATABASE_NAME);
            if (dbFilepath.exists()) {
                instance = new DatabaseManager(context);
            } else {
                instance = new DatabaseManager(context);
                CsvDataParser.readUpdatedRestaurantData();
            }
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
        values.put(RestaurantTable.FIELD_IS_FAVOURITE, 0);

        return db.insert(RestaurantTable.NAME, null, values);
    }

    public boolean updateRestaurantFav (String id, int favourite) {

        open();

        ContentValues values = new ContentValues();
        values.put(RestaurantTable.FIELD_IS_FAVOURITE, favourite);

        int result = db.update(RestaurantTable.NAME,
                values,
                RestaurantTable.FIELD_ID + "='" + id +"'",
                null);

        close();
        return result != 0;
    }

    private void updateAllFavRestaurants(ArrayList<Restaurant> favourites) {
        if (favourites == null || favourites.size() == 0) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (Restaurant fav : favourites) {
            builder.append("'").append(fav.getId()).append("', ");
        }
        String favIds = builder.substring(0, builder.length() - 2);
        ContentValues values = new ContentValues();
        values.put(RestaurantTable.FIELD_IS_FAVOURITE, IS_FAVOURITE);
        String where = RestaurantTable.FIELD_ID + "IN'" + favIds + "'";
        open();
        db.update(RestaurantTable.NAME, values, where, null);
        close();
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
            int violationId,
            int inspectionId,
            int code,
            String description,
            int isCritical,
            int isRepeat
    ) {
        ContentValues values = new ContentValues();
        values.put(ViolationTable.FIELD_ID, violationId);
        values.put(ViolationTable.FIELD_INSPECTION_ID, inspectionId);
        values.put(ViolationTable.FIELD_CODE, code);
        values.put(ViolationTable.FIELD_DESCRIPTION, description);
        values.put(ViolationTable.FIELD_IS_CRITICAL, isCritical);
        values.put(ViolationTable.FIELD_IS_REPEAT, isRepeat);

        return db.insert(ViolationTable.NAME, null, values);
    }

    public void beginTransaction() {
        db.beginTransaction();
    }

    public void endTransaction() {
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<Restaurant> getRestaurants(String selection, String limit) {
        open();
        Cursor cursor = db.query(
                true,
                RestaurantTable.NAME,
                RestaurantTable.FIELDS,
                selection,
                null,
                null,
                null,
                null,
                limit
        );
        if (cursor == null) {
            return null;
        }

        ArrayList<Restaurant> restaurants = new ArrayList<>();
        if (cursor.getCount() == 0) {
            return restaurants;
        }

        cursor.moveToFirst();
        do {
            restaurants.add(new Restaurant(
                    cursor.getString(RestaurantTable.COL_ID),
                    cursor.getString(RestaurantTable.COL_NAME),
                    cursor.getString(RestaurantTable.COL_ADDRESS),
                    cursor.getString(RestaurantTable.COL_CITY),
                    cursor.getDouble(RestaurantTable.COL_LATITUDE),
                    cursor.getDouble(RestaurantTable.COL_LONGITUDE),
                    cursor.getInt(RestaurantTable.COL_IS_FAVOURITE)
            ));
        } while (cursor.moveToNext());

        close();

        return restaurants;
    }

    public ArrayList<Restaurant> getRestaurants() {
        String selection = RestaurantFilter.getFilterSelectionCriteria();
        return getRestaurants(selection, null);
    }

    public Restaurant getRestaurant(String restaurantId) {
        String selection = RestaurantTable.FIELD_ID + " = '" + restaurantId + "'";
        return getRestaurants(selection, "1").get(0);
    }

    public ArrayList<Inspection> getInspections(String selection, String limit) {
        open();
        String orderBy = InspectionTable.FIELD_DATE + " DESC";
        Cursor cursor = db.query(
                true,
                InspectionTable.NAME,
                InspectionTable.FIELDS,
                selection,
                null,
                null,
                null,
                orderBy,
                null
        );
        if (cursor == null) {
            String errorMessage = String.format("Invalid database query selection criteria [%s]", selection);
            throw new IllegalArgumentException(errorMessage);
        }

        ArrayList<Inspection> inspections = new ArrayList<>();
        if (cursor.getCount() == 0) {
            return inspections;
        }

        cursor.moveToFirst();

        do {
            String dateString = cursor.getString(InspectionTable.COL_DATE);
            GregorianCalendar date = DateHelper.getDateFromString(dateString);

            inspections.add(new Inspection(
                    cursor.getInt(InspectionTable.COL_ID),
                    cursor.getString(InspectionTable.COL_RESTAURANT_ID),
                    cursor.getString(InspectionTable.COL_TYPE),
                    cursor.getString(InspectionTable.COL_HAZARD_RATING),
                    date,
                    cursor.getInt(InspectionTable.COL_CRITICAL_ISSUES),
                    cursor.getInt(InspectionTable.COL_NON_CRITICAL_ISSUES)
            ));
        } while (cursor.moveToNext());

        close();

        return inspections;
    }

    public ArrayList<Inspection> getInspections(String restaurantId) {
        String selection = InspectionTable.FIELD_RESTAURANT_ID + " = '" + restaurantId + "'";
        return getInspections(selection, null);
    }

    public Inspection getInspection(int inspectionId) {
        String selection = InspectionTable.FIELD_ID + " = " + inspectionId;
        return getInspections(selection, "1").get(0);
    }

    public Inspection getMostRecentInspection(String restaurantId) {
        String selection = InspectionTable.FIELD_RESTAURANT_ID + " = '" + restaurantId + "'";
        ArrayList<Inspection> inspections = getInspections(selection, "1");
        if (inspections.size() == 0) {
            return null;
        }
        return inspections.get(0);
    }

    public ArrayList<Violation> getViolations(String selection, String limit) {
        open();
        Cursor cursor = db.query(
                true,
                ViolationTable.NAME,
                ViolationTable.FIELDS,
                selection,
                null,
                null,
                null,
                null,
                limit
        );
        if (cursor == null) {
            String errorMessage = String.format("Invalid database query selection criteria [%s]", selection);
            throw new IllegalArgumentException(errorMessage);
        }

        ArrayList<Violation> violations = new ArrayList<>();
        if (cursor.getCount() == 0) {
            return violations;
        }

        cursor.moveToFirst();

        do {

            boolean isCritical = cursor.getInt(ViolationTable.COL_IS_CRITICAL) == 1 ? true : false;
            boolean isRepeat = cursor.getInt(ViolationTable.COL_IS_REPEAT) == 1 ? true : false;

            violations.add(new Violation(
                    cursor.getInt(ViolationTable.COL_ID),
                    cursor.getInt(ViolationTable.COL_INSPECTION_ID),
                    cursor.getInt(ViolationTable.COL_CODE),
                    isCritical,
                    isRepeat,
                    cursor.getString(ViolationTable.COL_DESCRIPTION)
            ));
        } while (cursor.moveToNext());

        close();

        return violations;
    }

    public ArrayList<Violation> getViolations(int inspectionId) {
        String selection = ViolationTable.FIELD_INSPECTION_ID + " = " + inspectionId;
        return getViolations(selection, null);
    }

    public void update() {
        RestaurantFilter.setFilter(null, null, null, null, true);
        ArrayList<Restaurant> favourites = instance.getRestaurants();

        open();
        dbHelper.onUpgrade(db, 0, 0);
        close();

        CsvDataParser.readUpdatedRestaurantData();
        updateAllFavRestaurants(favourites);
        RestaurantFilter.setFilter(null, null, null, null, null);
    }

    public ArrayList<Restaurant> getUpdatedFavourites(String previousModifyTime) {
        String selection = RestaurantFilter.getUpdatedFavouritesSelection(previousModifyTime);
        return getRestaurants(selection, null);
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
            db.execSQL(InspectionTable.CREATE);
            db.execSQL(ViolationTable.CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE + ViolationTable.NAME);
            db.execSQL(DROP_TABLE + InspectionTable.NAME);
            db.execSQL(DROP_TABLE + RestaurantTable.NAME);
            onCreate(db);
        }
    }

    public void setUpdateNeeded(boolean update){
        updateNeeded = update;
    }

    public boolean getUpdateNeeded(){
        return updateNeeded;
    }
}
