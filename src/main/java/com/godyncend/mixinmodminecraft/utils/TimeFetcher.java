package com.godyncend.mixinmodminecraft.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class TimeFetcher {
    public static CompletableFuture<HourRequest> fetchTime(){
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("http://100.107.209.52:8888/api/time");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                    // Works on all recent Gson versions
                    JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
                    Integer hour = json.get("hour").getAsInt();
                    Integer minute = json.get("minute").getAsInt();
                    return new HourRequest(hour, minute);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new HourRequest(0,0);
            }
        });
    }
}
