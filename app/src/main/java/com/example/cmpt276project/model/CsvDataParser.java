package com.example.cmpt276project.model;

import com.example.cmpt276project.App;
import com.example.cmpt276project.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import static com.example.cmpt276project.model.DataUpdater.FILE_PATH;
import static com.example.cmpt276project.model.DataUpdater.RESTAURANTS_FILE;
import static com.example.cmpt276project.model.DataUpdater.INSPECTION_FILE;

/**
 * This class parses the CSV data sets in order to construct all instances of Restaurant, Inspection,
 * and Violation that are used in the app.
 */
public class CsvDataParser {

    public static void readUpdatedRestaurantData(RestaurantManager manager) {
        try {
            String restaurantFilePath = FILE_PATH + RESTAURANTS_FILE;
            String inspectionFilePath = FILE_PATH + INSPECTION_FILE;
            File restaurantFile = new File(restaurantFilePath);
            File inspectionFile = new File(inspectionFilePath);
            InputStream restaurantStream = new FileInputStream(restaurantFile);
            InputStream inspectionStream = new FileInputStream(inspectionFile);
            readRestaurantData(manager, restaurantStream);
            readInspectionData(manager, inspectionStream);
        } catch (Exception e) {
            readDefaultRestaurantData(manager);
        }
    }

    public static void readDefaultRestaurantData(RestaurantManager manager) {
        try {
            InputStream inputStream = App.resources().openRawResource(R.raw.restaurants_itr1);
            readRestaurantData(manager, inputStream);
            inputStream = App.resources().openRawResource(R.raw.inspectionreports_itr1);
            readInspectionData(manager, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readRestaurantData(RestaurantManager manager, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );

        String line;
        try {
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                try {
                    Restaurant restaurant = getRestaurantFromData(line);
                    manager.addRestaurant(restaurant);
                } catch (IllegalArgumentException e) {
                    // No way to handle corrupt data, just skip it
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }

    private static void readInspectionData(RestaurantManager manager, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );

        String line;
        try {
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                try {
                    Inspection inspection = getInspectionFromData(line);
                    String trackingNumber = inspection.getTrackingNumber();
                    Restaurant restaurant = manager.getRestaurantByTrackingNumber(trackingNumber);
                    restaurant.addInspection(inspection);
                } catch (IllegalArgumentException e) {
                    // If inspection line data is corrupt, inspection cannot be added
                    // If restaurant is not found inspections cannot be added
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }

    public static Restaurant getRestaurantFromData(String restaurantData) {
        try {
            ArrayList<String> tokens = tokenize(restaurantData, ',');

            String trackingNumber = withQuotesRemoved(tokens.get(0));
            String name = withQuotesRemoved(tokens.get(1));
            String address = withQuotesRemoved(tokens.get(2));
            String city = withQuotesRemoved(tokens.get(3));
            double latitude = Double.parseDouble(tokens.get(5));
            double longitude = Double.parseDouble(tokens.get(6));
            return new Restaurant(
                    trackingNumber,
                    name,
                    address,
                    city,
                    latitude,
                    longitude
            );
        } catch (Exception e) {
            String errorMessage = String.format("Illegal string of restaurant data [%s]", restaurantData);
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    public static Inspection getInspectionFromData(String inspectionData) {
        try {
            ArrayList<String> tokens = tokenize(inspectionData, ',');

            // The violation lump and hazard rating are not always in the same column
            // Check which column is the rating and assign them accordingly
            String rating;
            String violations;
            if (tokens.get(5).toLowerCase().matches("(low)|(moderate)|(high)")) {
                rating = tokens.get(5);
                violations = tokens.get(6);
            } else {
                violations = tokens.get(5);
                rating = tokens.get(6);
            }

            String trackingNumber = withQuotesRemoved(tokens.get(0));
            GregorianCalendar date = DateHelper.getDateFromString(tokens.get(1));
            String inspectionType = withQuotesRemoved(tokens.get(2));
            String hazardRating = withQuotesRemoved(rating);

            Inspection inspection = new Inspection(
                    trackingNumber,
                    inspectionType,
                    hazardRating,
                    date
            );

            addViolationsToInspection(violations, inspection);

            return inspection;
        } catch (Exception e) {
            String errorMessage = String.format("Illegal string of inspection data [%s]", inspectionData);
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    public static Violation getViolationFromData(String violationData) {

        try {

            ArrayList<String> tokens = tokenize(violationData, ',');

            int id = Integer.parseInt(tokens.get(0));
            String description = tokens.get(2);
            String criticalString = tokens.get(1);
            String repeatString = tokens.get(3);
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
        } catch (Exception e) {
                String errorMessage = String.format("Illegal string of violation data [%s]", violationData);
                throw new IllegalArgumentException(errorMessage, e);
            }
    }

    private static void addViolationsToInspection(String violationLump, Inspection inspection) {
        if (violationLump.length() == 0) {
            return;
        }

        violationLump = withQuotesRemoved(violationLump);
        ArrayList<String> violationStrings = tokenize(violationLump, '|');
        for (String violationData : violationStrings) {
            Violation violation = getViolationFromData(violationData);
            inspection.addViolation(violation);
        }
    }

    private static ArrayList<String> tokenize(String s, char delimiter) {
        StringBuilder currentToken = new StringBuilder();
        ArrayList<String> tokens = new ArrayList<>();
        boolean isInQuotes = false;

        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);

            if (currentChar == delimiter && !isInQuotes) {
                tokens.add(currentToken.toString());
                currentToken = new StringBuilder();
            } else {
                currentToken.append(currentChar);
            }

            if (currentChar == '"') {
                isInQuotes = !isInQuotes;
            }
        }
        tokens.add(currentToken.toString());

        return tokens;
    }

    private static String withQuotesRemoved(String s) {
        if (s.charAt(0) == '"') {
            s = s.substring(1, s.length());
        }
        if (s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
