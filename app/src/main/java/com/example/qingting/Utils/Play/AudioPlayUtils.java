package com.example.qingting.Utils.Play;

import android.media.MediaPlayer;
import android.util.Log;

import com.example.qingting.Bean.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Lombok;

public class AudioPlayUtils {
    private static final String TAG = AudioPlayUtils.class.getName();
    private static MediaPlayer mediaPlayer;

    private static boolean hasDataSource;
    // 下面这两个List关系     playedMusicList  musicList
    //               last3 last2 last1 currentMusic next1 next2 next3
    final private static LinkedList<Music> nextMusicList;  // 未被播放的音乐
    final private static LinkedList<Music> playedMusicList;  // 被播放过的音乐
    @Getter
    private static Music currentMusic;

    final private static List<OnAudioPlayerListener> onAudioPlayerListenerList;

    static {
        nextMusicList = new LinkedList<>();
        playedMusicList = new LinkedList<>();
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
            mediaPlayer.setOnErrorListener(AudioPlayUtils::doWithPlayError);
        } catch (IOException e) {
            doWithFetchResourceError(e);
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
            mediaPlayer.setOnErrorListener(AudioPlayUtils::doWithPlayError);
        } catch (IOException e) {
            doWithFetchResourceError(e);
        }
    }

    private static void doWithFetchResourceError(IOException e) {
        Log.e(TAG, "Error setting data source for file", e);
        for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
            onAudioPlayerListener.onPaused();
            onAudioPlayerListener.onError("无法加载音频");
        }
    }

    private static boolean doWithPlayError(MediaPlayer mediaPlayerOnError, int what, int extra) {
        Log.e(TAG, "Error occurred: " + what);
        for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
            onAudioPlayerListener.onPaused();
            onAudioPlayerListener.onError("播放出错");
        }
        return true;
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


    public static void stopAndRelease() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                onAudioPlayerListener.onPaused();
                onAudioPlayerListener.onStopped();
            }
            hasDataSource = false;
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
        nextMusicList.addLast(music);
    }


    /**
     * 添加到下一首播放
     * @param music 要播放的音乐
     * @param onAudioPlayerListener
     */
    public static void addMusicToNext(Music music, OnAudioPlayerListener onAudioPlayerListener) {
        if (currentMusic == null) {
            playMusic(music, onAudioPlayerListener);
            return;
        }
        if (onAudioPlayerListener != null) {
            AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
        }
        nextMusicList.addFirst(music);
    }


    /**
     * 清空列表，并且马上开始播放新的列表
     * @param musicList2add 要播放的列表，注意size为0会直接返回
     */
    public static void playMusicList(List<Music> musicList2add) {
        if (musicList2add.size() == 0) {
            return;
        }
        // 清空音乐
        nextMusicList.clear();
        playedMusicList.clear();
        // 添加到下一首
        nextMusicList.addAll(musicList2add);
        playMusic(nextMusicList.pollFirst(), null);
    }


    // 马上播放音乐
    public static void playMusic(Music music, OnAudioPlayerListener onAudioPlayerListener) {
        if (onAudioPlayerListener != null) {
            AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
        }
        currentMusic = music;
        AudioPlayUtils.playFromUrl(music.getPath());
    }


    // 播放下一首音乐
    public static boolean playNextMusic() {
        if (nextMusicList.isEmpty()) {
            return false;
        }
        if (currentMusic != null) {
            playedMusicList.addLast(currentMusic);
        }
        Music music = nextMusicList.pollFirst();
        playMusic(music, null);
        return true;
    }

    public static boolean playLastMusic() {
        if (playedMusicList.isEmpty()) {
            return false;
        }
        if (currentMusic != null) {
            nextMusicList.addFirst(currentMusic);
        }
        Music music = playedMusicList.pollLast();
        playMusic(music, null);
        return true;
    }

    @Getter
    static int cacheProcess;  // 播放器缓存的进度

    private static MediaPlayer getMediaPlayer() {
        MediaPlayer mediaPlayer1 = new MediaPlayer();
        mediaPlayer1.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                cacheProcess = percent;
            }
        });
        mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
                    onAudioPlayerListener.onPaused();
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
                if (currentMusic != null) {
                    playedMusicList.addLast(currentMusic);
                    if (!playNextMusic()) {
                        currentMusic = null;
                    }
                }
            }

        });
        return mediaPlayer1;
    }



    public static boolean hasNext() {
        if (nextMusicList.isEmpty()) {
            return false;
        }
        return true;
    }

}
