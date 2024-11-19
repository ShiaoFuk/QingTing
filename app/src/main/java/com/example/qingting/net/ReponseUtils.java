package com.example.qingting.net;

import com.example.qingting.Utils.JsonUtils;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.Response;

public class ReponseUtils {
    public static JsonElement doWithResponse(Response response) throws IOException {
        String res = response.body().string();
        return JsonUtils.getJsonParser().fromJson(res, JsonElement.class);
    }
}
