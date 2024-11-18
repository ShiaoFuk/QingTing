package com.example.qingting.net.request;

import android.util.Log;

import com.example.qingting.Utils.JsonUtils;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MusicRequest {
    private final static String TAG = MusicRequest.class.getName();

    // 模板，只需要修改:
    // 1. 传入参数
    // 2. 第一行处理后的转换参数
    // 3. 调用的请求函数
    public static void getMusic1(RequestListener listener, String search) {
        String object = (String) listener.onPrepare(search);
        listener.onRequest();
        try {
            JsonElement element = getMusicRequest(listener, search);
            listener.onReceive();
            listener.onSuccess(element);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            listener.onError();
        }
        listener.onFinish();
    }


    private static JsonElement getMusicRequest(RequestListener listener, String search) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://suansun.top/music")
                .method("GET", body)
                .build();
        Response response = client.newCall(request).execute();
        return doWithResponse(response);
    }

    private static JsonElement doWithResponse(Response response) throws IOException {
        String res = response.body().string();
        return JsonUtils.getJsonParser().fromJson(res, JsonElement.class);
    }
}
