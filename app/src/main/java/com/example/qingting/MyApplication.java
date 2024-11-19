package com.example.qingting;

import android.app.Application;

import com.example.qingting.Bean.Music;
import com.example.qingting.Bean.Song;
import com.example.qingting.Utils.AudioPlayUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * App对象，维护一个播放队列
 */
public class MyApplication extends Application {
    LinkedList<Music> musicList;
    AudioPlayUtils audioPlayUtils;
    static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        musicList = new LinkedList<>();
        audioPlayUtils = new AudioPlayUtils(new AudioPlayUtils.OnAudioPlayerListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onComplete() {
                if (!musicList.isEmpty()) {
                    Music music = musicList.pop();
                    audioPlayUtils.playFromUrl(music.getPath());
                }
            }

        });
    }


    public static MyApplication getInstance() {
        return application;
    }

    public static List<Music> getMusicList() {
        return application.musicList;
    }


    // 列表最后加上音乐
    public static void addMusicToEnd(Music music, AudioPlayUtils.OnAudioPlayerListener onAudioPlayerListener) {
        if (onAudioPlayerListener != null) {
            application.audioPlayUtils.setOnAudioPlayerListener(onAudioPlayerListener);
        }
        application.musicList.addLast(music);
    }


    // 加入下一首
    public static void addMusicToNext(Music music, AudioPlayUtils.OnAudioPlayerListener onAudioPlayerListener) {
        if (onAudioPlayerListener != null) {
            application.audioPlayUtils.setOnAudioPlayerListener(onAudioPlayerListener);
        }
        application.musicList.addFirst(music);
    }


    // 马上播放音乐
    public static void playMusic(Music music, AudioPlayUtils.OnAudioPlayerListener onAudioPlayerListener) {
        if (onAudioPlayerListener != null) {
            application.audioPlayUtils.setOnAudioPlayerListener(onAudioPlayerListener);
        }
        application.audioPlayUtils.playFromUrl(music.getPath());
    }

}
