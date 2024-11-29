package com.example.qingting;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.qingting.Bean.PlayList;
import com.example.qingting.Utils.JsonUtils;
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
import java.util.List;

/**
 * App对象，维护一个播放队列
 */
public class MyApplication extends Application {
    final static String TAG = MyApplication.class.getName();
    static MyApplication application;
    boolean isPlayListModified = true;  // 第一次设为true
    List<List<PlayList>> playListListList;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MusicDBHelper.getInstance(getApplicationContext());
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


    /**
     * 用于让ui读取数据
     * @return
     */
    public static List<List<PlayList>> getPlayListListList() {
        return application.playListListList;
    }


    /**
     * 获取全部歌单信息后用这个设置歌单，仅限于写入数据库后读取到内存
     * @param list
     */
    public static void setPlayListListList(List<List<PlayList>> list) {
        application.playListListList = list;
        application.isPlayListModified = true;
        Log.e(TAG, "set Play list");
    }

    /**
     * 网络读取歌单到本地数据库，同时设置全局列表
     * 进入App的时候更新一次歌单，登录成功也要更新一次，避免之前进入app没登陆导致没有歌单
     */
    public static void getPlayListFromNet(Context context) {
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
                    PlayListDB.insertList(context, playListList);
                    List<PlayList> playListListDefault = PlayListDB.selectAll(context);
                    List<PlayList> playListListOrderByName = PlayListDB.selectAllOrderByName(context);
                    List<List<PlayList>> playListListList = new ArrayList<>();
                    playListListList.add(playListListDefault);
                    playListListList.add(playListListOrderByName);
                    setPlayListListList(playListListList);
                    setPlayListNoModified();
                    // 刷新视图
                    ToastUtils.makeShortText(context, context.getResources().getString(R.string.play_list_request_success));
                    return;
                }
                ToastUtils.makeShortText(context, context.getResources().getString(R.string.play_list_request_fail));
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onFinish() {

            }

        }, LoginSP.getToken(context));
    }


    /**
     * 从当前维护的列表删除对应id的playList
     * @param playList 要删除的playlist
     * @return true 删除成功； false删除失败
     */
    public static boolean deletePlayList(PlayList playList) {
        // 删除
        if (playList == null || playList.getId() == null) {
            return false;
        }
        boolean res = true;
        for (int i = 0; i < getPlayListListList().size(); ++i) {
            res &= getPlayListListList().get(i).removeIf(tempPlayList -> tempPlayList.getId() == playList.getId());
        }
        Log.e("fjkdklafjaksla", "删除成功" + res);
        return res;
    }

}
