package com.example.qingting.net.request.PlayListMusicRequest;

import static com.example.qingting.net.ReponseUtils.doWithResponse;

import android.util.Log;

import com.example.qingting.net.request.PlayListRequest.GetAllPlayListRequest;
import com.example.qingting.net.request.RequestListener;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetMusicRequest {
    final static String TAG = GetAllPlayListRequest.class.getName();
    // 模板，只需要修改:
    // 1. 传入参数
    // 2. 第一行处理后的转换参数
    // 3. 调用的请求函数


    /**
     * 发送请求获取该歌单所有歌
     * @param listener 不需要写onPrepare，没有数据要准备
     * @param token
     * @param playListId 歌单id
     */
    public static void getAllPlayList(RequestListener listener, String token, Integer playListId) {
        if (token == null) {
            return;
        }
        listener.onRequest();
        Thread thread = new Thread(()->{
            try {
                JsonElement element = getAllFromPlayList(token, playListId);
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


    private static JsonElement getAllFromPlayList(String token, Integer playListId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(String.format("http://suansun.top/playList/song/get?token=%s&playListId=%s", token, playListId.toString()))
                .build();
        Response response = client.newCall(request).execute();
        return doWithResponse(response);
    }
}
