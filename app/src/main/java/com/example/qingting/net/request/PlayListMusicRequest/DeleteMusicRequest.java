package com.example.qingting.net.request.PlayListMusicRequest;

import static com.example.qingting.net.ReponseUtils.doWithResponse;

import android.util.Log;

import com.example.qingting.Bean.Music;
import com.example.qingting.net.request.RequestListener;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteMusicRequest {
    final static String TAG = DeleteMusicRequest.class.getName();
    // 模板，只需要修改:
    // 1. 传入参数
    // 2. 第一行处理后的转换参数
    // 3. 调用的请求函数


    /**
     * 发起请求，从歌单删除多首歌，不需要写onPrepare
     * @param listener
     * @param token
     * @param playListId 歌单id
     * @param musicList 要删除的音乐列表
     */
    public static void deleteMusicFromPlayList(RequestListener listener, String token, Integer playListId, List<Music> musicList) {
        listener.onRequest();
        Thread thread = new Thread(()->{
            try {
                JsonElement element = deleteMusicFromPlayListRequest(token, playListId, musicList);
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


    private static JsonElement deleteMusicFromPlayListRequest(String token, Integer playListId, List<Music> musicList) throws IOException {
        // 提取 musicList 中每个 Music 对象的 id
        List<String> musicIdList = musicList.stream()
                .map(music -> music.getId().toString())  // 提取 Music 的 id
                .collect(Collectors.toList());
        String musicIds = String.join(",", musicIdList);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        HttpUrl url = HttpUrl.parse("http://suansun.top/playList/song/deleteMany")
                .newBuilder()
                .addQueryParameter("token", token) // 添加查询参数 q
                .addQueryParameter("playListId", playListId.toString()) // 添加查询参数 limit
                .addQueryParameter("musicIdList", musicIds)
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(url)
                .method("PUT", body)
                .build();
        Response response = client.newCall(request).execute();
        return doWithResponse(response);
    }
}
