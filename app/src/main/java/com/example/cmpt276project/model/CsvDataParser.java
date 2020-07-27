package com.example.cmpt276project.model;

import com.example.cmpt276project.App;
import com.example.cmpt276project.R;
import com.example.cmpt276project.model.database.DatabaseManager;

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

    public static void readUpdatedRestaurantData() {
        String restaurantFilePath = FILE_PATH + RESTAURANTS_FILE;
        String inspectionFilePath = FILE_PATH + INSPECTION_FILE;
        DatabaseManager dbManager = DatabaseManager.getInstance();
        try {
            dbManager.open();
            File restaurantFile = new File(restaurantFilePath);
            File inspectionFile = new File(inspectionFilePath);
            InputStream restaurantStream = new FileInputStream(restaurantFile);
            InputStream inspectionStream = new FileInputStream(inspectionFile);

            readRestaurantData(dbManager, restaurantStream);
            readInspectionData(dbManager, inspectionStream);
        } catch (IOException e) {
            readDefaultRestaurantData();
        } catch (Exception e) {
            readDefaultRestaurantData();
        } finally {
            if (dbManager != null) {
                dbManager.close();
            }
        }
    }

    public static void readDefaultRestaurantData() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        try {
            dbManager.open();
            InputStream restaurantStream = App.resources().openRawResource(R.raw.restaurants_itr1);
            InputStream inspectionStream = App.resources().openRawResource(R.raw.inspectionreports_itr1);

            readRestaurantData(dbManager, restaurantStream);
            readInspectionData(dbManager, inspectionStream);
        } catch (IllegalArgumentException e) {

        } finally {
            if (dbManager != null) {
                dbManager.close();
            }
        }
    }

    private static void readRestaurantData(DatabaseManager dbManager, InputStream inputStream) {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        )) {
            String line;
            reader.readLine();
            dbManager.beginTransaction();

            while ((line = reader.readLine()) != null) {
                try {
                    insertRestaurantToDatabase(line, dbManager);
                } catch (Exception e) {
                    // No way to handle corrupt data, just skip it
                    continue;
                }
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            throw new IllegalArgumentException(errorMessage);
        } finally {
            dbManager.endTransaction();
        }
    }

    private static void readInspectionData(DatabaseManager dbManager, InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        )) {
            String line;
            int inspectionId = 0;
            int violationId = 0;
            reader.readLine();
            dbManager.beginTransaction();

            while ((line = reader.readLine()) != null) {
                try {
                    violationId = insertInspectionToDatabase(line, dbManager, inspectionId, violationId);
                    inspectionId++;
                } catch (Exception e) {
                    // If inspection line data is corrupt, inspection cannot be added
                    // If restaurant is not found inspections cannot be added
                    continue;
                }
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            throw new IllegalArgumentException(errorMessage);
        } finally {
            dbManager.endTransaction();
        }
    }

    public static void insertRestaurantToDatabase(String restaurantData, DatabaseManager dbManager) {
        try {
            ArrayList<String> tokens = tokenize(restaurantData, ',');

            String id = withQuotesRemoved(tokens.get(0));
            String name = withQuotesRemoved(tokens.get(1));
            String address = withQuotesRemoved(tokens.get(2));
            String city = withQuotesRemoved(tokens.get(3));
            double latitude = Double.parseDouble(tokens.get(5));
            double longitude = Double.parseDouble(tokens.get(6));

            dbManager.insertToRestaurants(id, name, address, city, latitude, longitude);
        } catch (Exception e) {
            String errorMessage = String.format("Illegal string of restaurant data [%s]", restaurantData);
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    public static int insertInspectionToDatabase(
            String inspectionData,
            DatabaseManager dbManager,
            int inspectionId,
            int violationId
    ) {
        ArrayList<String> tokens = tokenize(inspectionData, ',');

        // The violation lump and hazard rating are not always in the same column
        // Check which column is the rating and assign them accordingly
        String rating;
        String violationLump;
        if (tokens.get(5).toLowerCase().matches("\"?(low|moderate|high)\"?")) {
            rating = tokens.get(5);
            violationLump = tokens.get(6);
        } else {
            violationLump = tokens.get(5);
            rating = tokens.get(6);
        }


        String restaurantId = withQuotesRemoved(tokens.get(0));
        String inspectionType = withQuotesRemoved(tokens.get(2));
        String hazardRating = withQuotesRemoved(rating);
        int date = Integer.parseInt(withQuotesRemoved(tokens.get(1)));
        int numCritical = Integer.parseInt(withQuotesRemoved(tokens.get(3)));
        int numNonCritical = Integer.parseInt(withQuotesRemoved(tokens.get(4)));
        Inspection.getInspectionTypeEnum(inspectionType);
        Inspection.getHazardRatingEnum(hazardRating);

        violationId = parseViolationLump(violationLump, dbManager, inspectionId, violationId);

        dbManager.insertToInspections(
                inspectionId,
                restaurantId,
                date,
                inspectionType,
                hazardRating,
                numCritical,
                numNonCritical
        );

        return violationId;
    }

    private static int parseViolationLump(
            String violationLump,
            DatabaseManager dbManager,
            int inspectionId,
            int violationId
    ) {
        if (violationLump.length() == 0) {
            return violationId;
        }

        violationLump = withQuotesRemoved(violationLump);
        ArrayList<String> violationStrings = tokenize(violationLump, '|');

        for (String violationData : violationStrings) {
            insertViolationToDatabase(violationData, dbManager, inspectionId, violationId);
            violationId++;
        }

        return violationId;
    }

    public static void insertViolationToDatabase(
            String violationData,
            DatabaseManager dbManager,
            int inspectionId,
            int violationId
    ) {
        try {
            ArrayList<String> tokens = tokenize(violationData, ',');

            int code = Integer.parseInt(tokens.get(0));
            String description = withQuotesRemoved(tokens.get(2));
            String criticalString = withQuotesRemoved(tokens.get(1));
            String repeatString = withQuotesRemoved(tokens.get(3));
            int isCritical = criticalString.toLowerCase().equals("critical") ? 1 : 0;
            int isRepeat = repeatString.toLowerCase().equals("repeat") ? 1 : 0;

            dbManager.insertToViolations(
                    violationId,
                    inspectionId,
                    code,
                    description,
                    isCritical,
                    isRepeat
            );

        } catch (Exception e) {
            String errorMessage = String.format("Illegal string of violation data [%s]", violationData);
            throw new IllegalArgumentException(errorMessage, e);
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
        if (s.length() == 0) {
            return s;
        }
        if (s.charAt(0) == '"') {
            s = s.substring(1, s.length());
        }
        if (s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private static Restaurant binarySearch(ArrayList<Restaurant> restaurants, String trackingNumber) {
        int max = restaurants.size() - 1;
        int min = 0;
        while (max >= min) {
            int index = (max + min) / 2;
            Restaurant restaurant = restaurants.get(index);
            int compare = trackingNumber.compareTo(restaurant.getId());
            if (compare > 0) {
                min = index + 1;
            } else if (compare < 0) {
                max = index - 1;
            } else {
                return restaurant;
            }
        }

        return null;
    }
}
