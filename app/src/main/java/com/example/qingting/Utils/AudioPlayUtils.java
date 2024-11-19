package com.example.qingting.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import lombok.Setter;

public class AudioPlayUtils {
    private static final String TAG = AudioPlayUtils.class.getName();
    private MediaPlayer mediaPlayer;
    @Setter
    private OnAudioPlayerListener onAudioPlayerListener;

    public AudioPlayUtils(OnAudioPlayerListener listener) {
        this.onAudioPlayerListener = listener;
    }

    // 播放网络音频
    public void playFromUrl(String url) {
        if (mediaPlayer != null) {
            stop();
        }
        mediaPlayer = getMediaPlayer();
        try {
            // 设置音频来源为网络 URL
            mediaPlayer.setDataSource(url);
            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);

            // 准备异步播放
            mediaPlayer.prepareAsync();

            // 播放准备完成后
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                if (onAudioPlayerListener != null) {
                    onAudioPlayerListener.onStarted();
                }
            });

            // 处理播放错误
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "Error occurred: " + what);
                if (onAudioPlayerListener != null) {
                    onAudioPlayerListener.onError("播放出错");
                }
                return true;
            });

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error setting data source for URL", e);
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onError("无法加载音频");
            }
        }
    }

    // 播放本地音频文件
    public void playFromFile(String filePath) {
        if (mediaPlayer != null) {
            stop();
        }

        mediaPlayer = getMediaPlayer();

        try {
            // 设置音频来源为本地文件路径
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);

            // 准备异步播放
            mediaPlayer.prepareAsync();

            // 播放准备完成后
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                if (onAudioPlayerListener != null) {
                    onAudioPlayerListener.onStarted();
                }
            });

            // 处理播放错误
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "Error occurred: " + what);
                if (onAudioPlayerListener != null) {
                    onAudioPlayerListener.onError("播放出错");
                }
                return true;
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error setting data source for file", e);
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onError("无法加载音频");
            }
        }
    }

    private MediaPlayer getMediaPlayer() {
        MediaPlayer mediaPlayer1 = new MediaPlayer();
        mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onAudioPlayerListener.onComplete();
            }
        });
        return mediaPlayer1;
    }

    // 停止播放
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onStopped();
            }
        }
    }


    // 暂停播放
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onPaused();
            }
        }
    }

    // 恢复播放
    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onStarted();
            }
        }
    }

    // 获取当前播放位置
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    // 获取音频的总时长
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    // 释放资源
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
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
