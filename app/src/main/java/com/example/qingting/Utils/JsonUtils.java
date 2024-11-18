package com.example.qingting.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class JsonUtils {
    private JsonUtils() {}
    private static Gson gson = null;
    public static Gson getJsonParser() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
        }
        return gson;
    }

    public static String toJson(Object obj) {
        String json = getJsonParser().toJson(obj);
        return json;
    }

}

