package com.animationtransmog.effect;

import lombok.NonNull;
import okhttp3.*;

import java.io.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.function.Consumer;

public class DatabaseManager {
    OkHttpClient client;

    public DatabaseManager()
    {
        client = new OkHttpClient();
    }

    public void getSettings(String playerName, Consumer<HashMap<String, String>> callback)
    {
        HashMap<String, String> newSettings = new HashMap<>();

        // Generate GET request to get settings from database
        Request request = new Request.Builder()
                .url("https://runelite-animation-transmog-default-rtdb.firebaseio.com/players/" + playerName.replace(" ", "+") + ".json")
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute request on new thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    ResponseBody body = response.body();
                    if (body == null) return;

                    String bodyString = body.string();
                    if (!bodyString.equals("null"))
                    {
                        // Parse JSON response into HashMap
                        String responseString = bodyString.substring(1, bodyString.length() - 1);
                        String[] responseStringList = responseString.split(",");
                        for (String keyVal : responseStringList)
                        {
                            String[] keyValList = keyVal.split(":");
                            String key = keyValList[0].substring(1, keyValList[0].length() - 1);
                            String value = keyValList[1].substring(1, keyValList[1].length() - 1);
                            if (!key.equals("lastUpdated")) newSettings.put(key, value);
                        }
                    }
                    body.close();
                    callback.accept(newSettings);
                }
            }
        });
    }

    public void setSettings(String playerName, HashMap<String, String> newSettings)
    {
        // Convert HashMap to JSON string
        StringBuilder result = new StringBuilder();
        result.append("{");
        for (HashMap.Entry<String, String> entry : newSettings.entrySet()) {
            result.append('"').append(entry.getKey()).append('"')
                    .append(":")
                    .append('"').append(entry.getValue()).append('"')
                    .append(",");
        }
        result.append('"').append("lastUpdated").append('"')
                .append(":")
                .append('"').append(new Timestamp(System.currentTimeMillis()).toInstant()).append('"')
                .append("}");

        // Generate PUT request to update database
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, result.toString());
        Request request = new Request.Builder()
                .url("https://runelite-animation-transmog-default-rtdb.firebaseio.com/players/" + playerName.replace(" ", "+") + ".json?print=silent")
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute request on new thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                response.close();
            }
        });
    }
}