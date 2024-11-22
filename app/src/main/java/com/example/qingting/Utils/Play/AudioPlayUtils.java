package com.example.qingting.Utils.Play;

import android.media.MediaPlayer;
import android.util.Log;

import com.example.qingting.Bean.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public class AudioPlayUtils {
    private static final String TAG = AudioPlayUtils.class.getName();
    private static MediaPlayer mediaPlayer;

    private static boolean hasDataSource;
    final private static LinkedList<Music> musicList;
    @Getter
    private static Music currentMusic;

    final private static List<OnAudioPlayerListener> onAudioPlayerListenerList;

    static {
        musicList = new LinkedList<>();
        onAudioPlayerListenerList = new ArrayList<>();
        mediaPlayer = getMediaPlayer();
    }


    // 播放网络音频
    public static void playFromUrl(String url) {
        try {
            // 设置音频来源为网络 URL
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            hasDataSource = true;
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
        try {
            // 设置音频来源为本地文件路径
            mediaPlayer.reset();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            hasDataSource = true;
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


    public static void seekTo(int millSecs) {
        if (hasDataSource) {
            mediaPlayer.seekTo(millSecs);
        }
    }

    // 暂停播放
    public static void pause() {
        if (hasDataSource && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                onAudioPlayerListener.onPaused();
            }
        }
    }

    // 恢复播放
    public static void resume() {
        if (hasDataSource && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                onAudioPlayerListener.onStarted();
            }
        }
    }

    // 获取当前播放位置
    public static int getCurrentPosition() {
        if (hasDataSource) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    // 获取音频的总时长
    public static int getDuration() {
        if (hasDataSource) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public static void stopAndRelease() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                onAudioPlayerListener.onStopped();
            }
            hasDataSource = false;
        }
    }


    public static boolean isPlaying() {
        if (hasDataSource) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }


    public static void addOnAudioPlayerListener(OnAudioPlayerListener onAudioPlayerListener) {
        onAudioPlayerListenerList.add(onAudioPlayerListener);
    }

    public static void removeOnAudioPlayerListener(OnAudioPlayerListener onAudioPlayerListener) {
        if (onAudioPlayerListenerList.contains(onAudioPlayerListener)) {
            onAudioPlayerListenerList.remove(onAudioPlayerListener);
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

    // 列表最后加上音乐
    public static void addMusicToEnd(Music music, OnAudioPlayerListener onAudioPlayerListener) {
        if (onAudioPlayerListener != null) {
            AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
        }
        musicList.addLast(music);
    }


    // 加入下一首
    public static void addMusicToNext(Music music, OnAudioPlayerListener onAudioPlayerListener) {
        if (currentMusic == null) {
            playMusic(music, onAudioPlayerListener);
            return;
        }
        if (onAudioPlayerListener != null) {
            AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
        }
        musicList.addFirst(music);
    }


    // 马上播放音乐
    public static void playMusic(Music music, OnAudioPlayerListener onAudioPlayerListener) {
        if (onAudioPlayerListener != null) {
            AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
        }
        currentMusic = music;
        AudioPlayUtils.playFromUrl(music.getPath());
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
        AudioPlayUtils.addOnAudioPlayerListener(new OnAudioPlayerListener() {
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
                    currentMusic = music;
                    playFromUrl(music.getPath());
                } else {
                    currentMusic = null;
                }
            }

        });
        return mediaPlayer1;
    }

//    public static List<Music> getMusicList() {
//        return musicList;
//    }

    public static boolean hasNext() {
        if (musicList.isEmpty()) {
            return false;
        }
        return true;
    }

}
