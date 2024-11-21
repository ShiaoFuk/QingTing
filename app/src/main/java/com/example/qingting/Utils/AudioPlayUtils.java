package com.example.qingting.Utils;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class AudioPlayUtils {
    private static final String TAG = AudioPlayUtils.class.getName();
    @Getter
    private static MediaPlayer mediaPlayer = getMediaPlayer();

    private static List<OnAudioPlayerListener> onAudioPlayerListenerList = new ArrayList<>();

    // 播放网络音频
    public static void playFromUrl(String url) {
        if (mediaPlayer != null) {
            pause();
        }
        try {
            // 设置音频来源为网络 URL
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);

            // 准备异步播放
            mediaPlayer.prepareAsync();

            // 播放准备完成后
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                    onAudioPlayerListener.onStarted();
                }
            });

            // 处理播放错误
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "Error occurred: " + what);
                for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                    onAudioPlayerListener.onError("播放出错");
                }
                return true;
            });

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error setting data source for URL", e);
            for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                onAudioPlayerListener.onError("无法加载音频");
            }
        }
    }

    // 播放本地音频文件
    public static void playFromFile(String filePath) {
        if (mediaPlayer != null) {
            pause();
        }
        try {
            // 设置音频来源为本地文件路径
            mediaPlayer.reset();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);

            // 准备异步播放
            mediaPlayer.prepareAsync();

            // 播放准备完成后
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                    onAudioPlayerListener.onStarted();
                }
            });

            // 处理播放错误
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "Error occurred: " + what);
                for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                    onAudioPlayerListener.onError("播放出错");
                }
                return true;
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error setting data source for file", e);
            for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                onAudioPlayerListener.onError("无法加载音频");
            }

        }
    }

    private static MediaPlayer getMediaPlayer() {
        MediaPlayer mediaPlayer1 = new MediaPlayer();
        mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                    onAudioPlayerListener.onComplete();
                }
            }
        });
        return mediaPlayer1;
    }

    // 停止播放
    public static void stopAndRelease() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                onAudioPlayerListener.onStopped();
            }
        }
    }


    // 暂停播放
    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                onAudioPlayerListener.onPaused();
            }
        }
    }

    // 恢复播放
    public static void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                onAudioPlayerListener.onStarted();
            }
        }
    }

    // 获取当前播放位置
    public static int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    // 获取音频的总时长
    public static int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    // 释放资源
    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }


    public static void addOnAudioPlayerListener(OnAudioPlayerListener onAudioPlayerListener) {
        onAudioPlayerListenerList.add(onAudioPlayerListener);
    }

    public static void removeOnAudioPlayerListener(OnAudioPlayerListener onAudioPlayerListener) {
        OnAudioPlayerListener onAudioPlayerListenerFirst = onAudioPlayerListenerList.get(0);
        for (OnAudioPlayerListener onAudioPlayerListener1: onAudioPlayerListenerList) {
            if (onAudioPlayerListener1.equals(onAudioPlayerListener) && !onAudioPlayerListenerFirst.equals(onAudioPlayerListener)) {
                onAudioPlayerListenerList.remove(onAudioPlayerListener);
            }
        }
    }


    public static void removeOnAudioPlayerListener(int index) {
        if (index < onAudioPlayerListenerList.size() && index > 0) {
            onAudioPlayerListenerList.remove(index);
        }
    }

    public static void removeAllOnAudioPlayerListener(int index) {
        for (int i = onAudioPlayerListenerList.size() - 1; i > 0; --i) {
            onAudioPlayerListenerList.remove(i);
        }
    }

    public static void removeLastOnAudioPlayerListener() {
        if (onAudioPlayerListenerList.size() > 0) {
            onAudioPlayerListenerList.remove(onAudioPlayerListenerList.size() - 1);
        }
    }


    // 播放状态回调接口
    public interface OnAudioPlayerListener {
        void onStarted();
        void onPaused();
        void onStopped();
        void onError(String errorMessage);
        void onComplete();
    }

}
