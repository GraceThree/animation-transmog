package com.animationtransmog.effect;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class DatabaseManager {
    public HashMap<String, String> getSettings(String playerName)
    {
        HashMap<String, String> newSettings = new HashMap<>();
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            // Make request for player's settings
            HttpGet request = new HttpGet("https://runelite-animation-transmog-default-rtdb.firebaseio.com/players/" + playerName.replace(" ", "+") + ".json");
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            HttpResponse response = httpClient.execute(request);
            HttpEntity responseData = response.getEntity();
            InputStream inData = responseData.getContent();

            // Read in request response
            int bufferSize = 1024;
            char[] buffer = new char[bufferSize];
            StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inData, StandardCharsets.UTF_8);
            for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
                out.append(buffer, 0, numRead);
            }

            // Parse JSON response into HashMap
            String responseString =  out.substring(1, out.toString().length() - 1);
            String[] responseStringList = responseString.split(",");
            for (String keyVal : responseStringList)
            {
                String[] keyValList = keyVal.split(":");
                String key = keyValList[0].substring(1, keyValList[0].length() - 1);
                String value = keyValList[1].substring(1, keyValList[1].length() - 1);
                newSettings.put(key, value);
            }
        } catch (Exception ignored) {
        }

        return newSettings;
    }

    public void setSettings(String playerName, HashMap<String, String> newSettings)
    {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            // Make request to update player's settings
            HttpPut request = new HttpPut("https://runelite-animation-transmog-default-rtdb.firebaseio.com/players/" + playerName.replace(" ", "+") + ".json");

            // Convert HashMap to JSON string
            StringBuilder result = new StringBuilder();
            result.append("{");
            boolean firstElement = true;
            for (HashMap.Entry<String, String> entry : newSettings.entrySet()) {
                if (!firstElement) result.append(",");
                else firstElement = false;

                result.append('"').append(entry.getKey()).append('"').append(":").append('"').append(entry.getValue()).append('"');
            }
            result.append("}");
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(new StringEntity(result.toString()));
            httpClient.execute(request);
        } catch (Exception ignored) {
        }
    }
}