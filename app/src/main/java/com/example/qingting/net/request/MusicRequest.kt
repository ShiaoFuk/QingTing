package com.example.qingting.net.request;

import static com.example.qingting.net.ReponseUtils.doWithResponse;

import android.util.Log;

import com.example.qingting.net.ServerConf;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicRequest {
    private final static String TAG = MusicRequest.class.getName();

    // 模板，只需要修改:
    // 1. 传入参数
    // 2. 第一行处理后的转换参数
    // 3. 调用的请求函数
    public static void getMusic(RequestListener listener, String search) {
        String object = (String) listener.onPrepare(search);
        listener.onRequest();
        Thread thread = new Thread(()->{
            try {
                JsonElement element = getMusicRequest(search);
                listener.onReceive();
                listener.onSuccess(element);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onError(e);
            } finally {
                listener.onFinish();
            }
        });
        thread.start();

    }


    private static JsonElement getMusicRequest(String search) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        // 使用 HttpUrl.Builder 来构建带有查询参数的 URL
        HttpUrl url = HttpUrl.parse(ServerConf.ADDRESS + "/music")
                .newBuilder()
                .addQueryParameter("name", search) // 添加第一个参数
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        return doWithResponse(response);
    }


}
