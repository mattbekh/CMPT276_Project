package com.example.cmpt276project.model;

import com.example.cmpt276project.App;
import com.example.cmpt276project.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;

/**
 * This class parses the CSV data sets in order to construct all instances of Restaurant, Inspection,
 * and Violation that are used in the app.
 */
public class CsvDataParser {

    public static void readRestaurantData(RestaurantManager manager) {
        InputStream inputStream = App.resources().openRawResource(R.raw.restaurants_itr1);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );

        String line;
        try {
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                Restaurant restaurant = getRestaurantFromData(line);
                manager.addRestaurant(restaurant);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        readInspectionData(manager);
    }

    private static void readInspectionData(RestaurantManager manager) {
        InputStream inputStream = App.resources().openRawResource(R.raw.inspectionreports_itr1);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );

        String line;
        try {
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                Inspection inspection = getInspectionFromData(line);
                String trackingNumber = inspection.getTrackingNumber();
                Restaurant restaurant = manager.getRestaurantByTrackingNumber(trackingNumber);
                restaurant.addInspection(inspection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Restaurant getRestaurantFromData(String restaurantData) {
        String[] tokens = restaurantData.split(",");

        String trackingNumber = withQuotesRemoved(tokens[0]);
        String name = withQuotesRemoved(tokens[1]);
        String address = withQuotesRemoved(tokens[2]);
        String city = withQuotesRemoved(tokens[3]);
        double latitude = Double.parseDouble(tokens[5]);
        double longitude = Double.parseDouble(tokens[6]);
        return new Restaurant(
                trackingNumber,
                name,
                address,
                city,
                latitude,
                longitude
        );
    }

    public static Inspection getInspectionFromData(String inspectionData) {
        int numFields = 7;
        String[] tokens = inspectionData.split(",", numFields);

        String trackingNumber = withQuotesRemoved(tokens[0]);
        GregorianCalendar date = DateHelper.getDateFromString(tokens[1]);
        String inspectionType = withQuotesRemoved(tokens[2]);
        String hazardRating = withQuotesRemoved(tokens[5]);

        Inspection inspection = new Inspection(
                trackingNumber,
                inspectionType,
                hazardRating,
                date
        );

        addViolationsToInspection(tokens[6], inspection);

        return inspection;
    }

    public static Violation getViolationFromData(String violationData) {

        String[] tokens = violationData.split(",");

        int id = Integer.parseInt(tokens[0]);
        String description = tokens[2];
        String criticalString = tokens[1];
        String repeatString = tokens[3];
        boolean isCritical = true;
        boolean isRepeat = true;

        if (criticalString.toLowerCase().equals("not critical")) {
            isCritical = false;
        }
        if (repeatString.toLowerCase().equals("not repeat")) {
            isRepeat = false;
        }

        return new Violation(
                id,
                isCritical,
                isRepeat,
                description
        );
    }

    private static void addViolationsToInspection(String violationLump, Inspection inspection) {
        if (violationLump.length() == 0) {
            return;
        }

        violationLump = withQuotesRemoved(violationLump);
        String[] violationStrings = violationLump.split("\\|");
        for (String violationData : violationStrings) {
            Violation violation = getViolationFromData(violationData);
            inspection.addViolation(violation);
        }
    }

    private static String withQuotesRemoved(String s) {
        return s.substring(1, s.length() - 1);
    }
}
