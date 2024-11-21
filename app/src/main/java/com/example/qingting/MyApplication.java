package com.example.qingting;

import android.app.Application;

import com.example.qingting.Bean.Music;
import com.example.qingting.Utils.Play.AudioPlayUtils;
import com.example.qingting.Utils.Play.OnAudioPlayerListener;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

/**
 * App对象，维护一个播放队列
 */
public class MyApplication extends Application {
    static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

    }



    public static MyApplication getInstance() {
        return application;
    }







}
