package com.example.cmpt276project.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;

/**
 * Class which handles the data from URL being stored in device storage and making sure initialized
 * data is handled correctly
 */
public class DataUpdater implements Callable<Boolean> {
    public static String RESTAURANTS_FILE = "restaurant_data.csv";
    public static String INSPECTION_FILE = "inspection_data.csv";
    public static String TEMP_RESTAURANT_FILE = "temp_rest_data.csv";
    public static String TEMP_INSPECTION_FILE = "temp_insp_data.csv";
    public static String FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;

    private SharedPreferences csvDataPrefs;

    public DataUpdater(Context context) {
        this.csvDataPrefs = context.getSharedPreferences("CSVData", Context.MODE_PRIVATE);
    }

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
            deleteTempFile(tempRestaurantFile);
            deleteTempFile(tempInspectionFile);
            updateModificationDate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void deleteTempData() {
        String tempRestaurantFile = FILE_PATH + TEMP_RESTAURANT_FILE;
        String tempInspectionFile = FILE_PATH + TEMP_INSPECTION_FILE;
        deleteTempFile(tempRestaurantFile);
        deleteTempFile(tempInspectionFile);
        if (isFreshInstall()) {
            String restaurantFile = FILE_PATH + RESTAURANTS_FILE;
            String inspectionFile = FILE_PATH + INSPECTION_FILE;
            deleteTempFile(restaurantFile);
            deleteTempFile(inspectionFile);
        }
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

    private void deleteTempFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    private void updateModificationDate() {
        try {
            JSONObject response = HttpRequestHandler.get(DataDownloader.RESTAURANTS_URL);

            String currentTime = DateHelper.getTimeString(new GregorianCalendar());
            String modifyTime = (String) response.getJSONObject("result").get("metadata_modified");
            modifyTime = modifyTime.replaceAll("[^0-9]", "");
            modifyTime = modifyTime.substring(0, 14);

            String previousModifyTime = csvDataPrefs.getString("localModifyTime", DateHelper.DEFAULT_TIME);
            storeModDateSharedPrefs("updatedOn", currentTime);
            storeModDateSharedPrefs("localModifyTime", modifyTime);
            storeModDateSharedPrefs("previousModifyTime", previousModifyTime.substring(0, 8));
        } catch (Exception e) {
            // Do nothing
        }
    }

    private boolean isFreshInstall() {
        String lastUpdateTime = csvDataPrefs.getString("updatedOn", DateHelper.DEFAULT_TIME);
        if (lastUpdateTime == DateHelper.DEFAULT_TIME) {
            return true;
        }
        return false;
    }

    private void storeModDateSharedPrefs(String key, String date) {
        SharedPreferences.Editor editor = csvDataPrefs.edit();
        editor.putString(key,date);
        editor.apply();
    }

    public void clearSharedPrefs(){
        SharedPreferences.Editor editor = csvDataPrefs.edit();
        editor.clear();
        editor.apply();
    }
}
