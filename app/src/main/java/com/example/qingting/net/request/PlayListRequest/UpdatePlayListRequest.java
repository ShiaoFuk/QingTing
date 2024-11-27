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

public class UpdatePlayListRequest {
    final static String TAG = DeletePlayListRequest.class.getName();
    public static void deletePlayList(RequestListener listener, String token, Integer playListId, String name) {
        listener.onRequest();
        Thread thread = new Thread(()->{
            try {
                JsonElement element = deletePlayListRequest(token, playListId, name);
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

    private static JsonElement deletePlayListRequest(String token, Integer playListId, String name) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://localhost/playList/update?token=" + token + "&playListId=" + playListId + "&name=" + name)
                .method("PUT", body)
                .build();
        Response response = client.newCall(request).execute();
        return doWithResponse(response);
    }
}
