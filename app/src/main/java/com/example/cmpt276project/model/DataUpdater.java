package com.example.cmpt276project.model;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Callable;

public class DataUpdater implements Callable<Boolean> {
    public static String RESTAURANTS_FILE = "restaurant_data.csv";
    public static String INSPECTION_FILE = "inspection_data.csv";
    public static String TEMP_RESTAURANT_FILE = "temp_restaurant_data.csv";
    public static String TEMP_INSPECTION_FILE = "temp_inspection_data.csv";
    public static String FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;

    public Boolean call() {
        return tryUpdateData();
    }

    public boolean tryUpdateData() {
        String restaurantFile = FILE_PATH + RESTAURANTS_FILE;
        String inspectionFile = FILE_PATH + INSPECTION_FILE;
        String tempRestaurantFile = FILE_PATH + TEMP_RESTAURANT_FILE;
        String tempInspectionFile = FILE_PATH + TEMP_INSPECTION_FILE;

        try {
            copy(tempRestaurantFile, restaurantFile);
            copy(tempInspectionFile, inspectionFile);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void copy(String fileSource, String fileDestination) throws Exception {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(fileSource);
            output = new FileOutputStream(fileDestination);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

            char[] buffer = new char[1024];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, read);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }
}
