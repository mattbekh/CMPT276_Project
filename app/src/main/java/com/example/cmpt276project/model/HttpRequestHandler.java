package com.example.cmpt276project.model;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This class gets a Json object which is the result of a HTTP request
 */
public class HttpRequestHandler {

    public static JSONObject get(String urlString) throws Exception {
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder jsonText = new StringBuilder();
            int read;
            char[] buffer = new char[1024];

            while ((read = reader.read(buffer)) != -1) {
                jsonText.append(buffer, 0, read);
            }

            return new JSONObject(jsonText.toString());
        } catch (Exception e) {
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
