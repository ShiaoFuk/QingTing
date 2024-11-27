package com.example.qingting;

import android.app.Application;

import com.example.qingting.Adapter.PlayListViewPagerAdapter;
import com.example.qingting.Bean.Music;
import com.example.qingting.Bean.PlayList;
import com.example.qingting.UserPage.UserPageFragment;
import com.example.qingting.Utils.JsonUtils;
import com.example.qingting.Utils.Play.AudioPlayUtils;
import com.example.qingting.Utils.Play.OnAudioPlayerListener;
import com.example.qingting.Utils.ToastUtils;
import com.example.qingting.data.DB.MusicDBHelper;
import com.example.qingting.data.DB.PlayListDB;
import com.example.qingting.data.SP.LoginSP;
import com.example.qingting.net.request.PlayListRequest.GetAllPlayListRequest;
import com.example.qingting.net.request.RequestListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * App对象，维护一个播放队列
 */
public class MyApplication extends Application {
    static MyApplication application;
    boolean isPlayListModified = true;  // 第一次设为true
    List<List<PlayList>> playListListList;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MusicDBHelper.getInstance(getApplicationContext());
        getPlayList();
        playListListList = PlayListDB.getPlayListListList(getApplicationContext());
    }


    public static MyApplication getInstance() {
        return application;
    }

    public static boolean isPlayListModified() {
        return application.isPlayListModified;
    }

    public static void setPlayListNoModified() {
        application.isPlayListModified = false;
    }

    public static List<List<PlayList>> getPlayListListList() {
        return application.playListListList;
    }

    public static void setPlayListListList(List<List<PlayList>> list) {
        application.playListListList = list;
        application.isPlayListModified = true;
    }

    /**
     * 网络读取歌单
     */
    public static void getPlayList() {
        GetAllPlayListRequest.getAllPlayList(new RequestListener() {
            @Override
            public Object onPrepare(Object object) {
                return null;
            }

            @Override
            public void onRequest() {

            }

            @Override
            public void onReceive() {

            }

            @Override
            public void onSuccess(JsonElement element) throws Exception {
                JsonObject jsonObject = element.getAsJsonObject();
                JsonElement element1 = jsonObject.get("code");
                if (element1 != null && element1.getAsInt() == 200) {
                    JsonElement list = jsonObject.get("data");
                    List<PlayList> playListList = JsonUtils.getJsonParser().fromJson(list, new TypeToken<List<PlayList>>() {}.getType());
                    // 存入数据库，后刷新
                    PlayListDB.insertList(application.getApplicationContext(), playListList);
                    List<PlayList> playListListDefault = PlayListDB.selectAll(application.getApplicationContext());
                    List<PlayList> playListListOrderByName = PlayListDB.selectAllOrderByName(application.getApplicationContext());
                    List<List<PlayList>> playListListList = new ArrayList<>();
                    playListListList.add(playListListDefault);
                    playListListList.add(playListListOrderByName);
                    setPlayListListList(playListListList);
                    // 刷新视图
                    ToastUtils.makeShortText(application.getApplicationContext(), application.getResources().getString(R.string.play_list_request_success));
                    return;
                }
                ToastUtils.makeShortText(application.getApplicationContext(), application.getResources().getString(R.string.play_list_request_fail));
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onFinish() {

            }

        }, LoginSP.getToken(application.getApplicationContext()));
    }

}
