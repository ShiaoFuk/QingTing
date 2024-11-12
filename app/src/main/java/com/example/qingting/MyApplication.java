package com.example.qingting;

import android.app.Application;

import com.example.qingting.Bean.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * App对象，维护一个播放队列
 */
public class MyApplication extends Application {
    ArrayList<Song> songList;
    @Override
    public void onCreate() {
        super.onCreate();
        songList = new ArrayList<>();
    }



    List<Song> getSongList() {
        return songList;
    }


}
