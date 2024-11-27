package com.example.qingting.net.request.PlayListRequest;

import static com.example.qingting.net.ReponseUtils.doWithResponse;

import android.util.Log;

import com.example.qingting.net.request.RequestListener;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeletePlayListRequest {
    final static String TAG = DeletePlayListRequest.class.getName();
    // 模板，只需要修改:
    // 1. 传入参数
    // 2. 第一行处理后的转换参数
    // 3. 调用的请求函数

    public static void deletePlayList(RequestListener listener, String token, Integer playListId) {
        listener.onRequest();
        Thread thread = new Thread(()->{
            try {
                JsonElement element = deletePlayListRequest(token, playListId);
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

    private static JsonElement deletePlayListRequest(String token, Integer playListId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://localhost/playList/delete?token=" + token + "&playListId=" + playListId)
                .method("DELETE", body)
                .build();
        Response response = client.newCall(request).execute();
        return doWithResponse(response);
    }
}
