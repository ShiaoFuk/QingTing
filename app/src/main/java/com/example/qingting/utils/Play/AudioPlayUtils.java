package com.example.qingting.utils.Play;

import android.media.MediaPlayer;
import android.util.Log;

import com.android.tools.r8.internal.Mu;
import com.example.qingting.bean.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public class AudioPlayUtils {
    private static final String TAG = AudioPlayUtils.class.getName();
    private static MediaPlayer mediaPlayer;

    private static boolean hasDataSource;
    // 下面这两个List关系     playedMusicList  musicList
    //               last3 last2 last1 currentMusic next1 next2 next3
    // 顺序播放
    private static LinkedList<Music> nextMusicList;  // 未被播放的音乐
    private static LinkedList<Music> playedMusicList;  // 被播放过的音乐
    // 随机播放
    private static LinkedList<Music> randomNextMusicList;  // 随机播放 未播放的列表
    @Getter
    private static Music currentMusic;

    private static List<OnAudioPlayerListener> onAudioPlayerListenerList;

    public static void init() {
        nextMusicList = new LinkedList<>();
        playedMusicList = new LinkedList<>();
        randomNextMusicList = new LinkedList<>();
        onAudioPlayerListenerList = new ArrayList<>();
        mediaPlayer = getMediaPlayer();
    }

    private static Object getPlayListLock = new Object();
    private static int mode = 0;  // 默认=0——0:顺序播放，1:随机播放，2:列表循环，3:单曲循环

    public static List<Music> getPlayList() {
        if (isRandomMode()) {
            synchronized (getPlayListLock) {
                return randomNextMusicList;
            }
        } else if (isSingleLoopMode()) {
            LinkedList<Music> res = new LinkedList();
            res.addFirst(currentMusic);
            return res;
        } else {
            // 循环列表和顺序播放都直接返回
            return nextMusicList;
        }
    }

    private static boolean isRandomMode() {
        return mode == 1;
    }

    private static boolean isListLoopMode() {
        return mode == 2;
    }

    private static boolean isSingleLoopMode() {
        return mode == 3;
    }


    // 把nextMusicList打乱
    private static void shuffle() {
        Music[] array = nextMusicList.toArray(new Music[]{});
        Collections.shuffle(Arrays.asList(array));
        synchronized (getPlayListLock) {
            randomNextMusicList.clear();
            Collections.addAll(randomNextMusicList, array);
        }
    }

    /**
     * 切换播放模式，0:顺序播放，1:随机播放，2:列表循环，3:单曲循环
     * 本质上是维护多个列表
     */
    public static void changePlayListMode(int inMode) {
        mode = inMode;
        if (isRandomMode()) {
            shuffle();
        }
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
                for (OnAudioPlayerListener onAudioPlayerListener : onAudioPlayerListenerList) {
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
                for (OnAudioPlayerListener onAudioPlayerListener : onAudioPlayerListenerList) {
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
        for (OnAudioPlayerListener onAudioPlayerListener : onAudioPlayerListenerList) {
            onAudioPlayerListener.onPaused();
            onAudioPlayerListener.onError("无法加载音频");
        }
    }

    private static boolean doWithPlayError(MediaPlayer mediaPlayerOnError, int what, int extra) {
        Log.e(TAG, "Error occurred: " + what);
        for (OnAudioPlayerListener onAudioPlayerListener : onAudioPlayerListenerList) {
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
            for (OnAudioPlayerListener onAudioPlayerListener : onAudioPlayerListenerList) {
                onAudioPlayerListener.onPaused();
            }
        }
    }

    // 恢复播放
    public static void resume() {
        if (hasDataSource && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            for (OnAudioPlayerListener onAudioPlayerListener : onAudioPlayerListenerList) {
                onAudioPlayerListener.onStarted();
            }
        }
    }


    public static void stopAndRelease() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            for (OnAudioPlayerListener onAudioPlayerListener : onAudioPlayerListenerList) {
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



    /**
     * 添加到下一首播放
     *
     * @param music                 要播放的音乐
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
        if (isRandomMode()) {
            randomNextMusicList.addFirst(music);
            nextMusicList.addFirst(music);
        } else {
            nextMusicList.addFirst(music);
        }
    }


    /**
     * 清空列表，并且马上开始播放新的列表
     *
     * @param musicList2add 要播放的列表，注意size为0会直接返回
     */
    public static void playMusicList(List<Music> musicList2add) {
        if (musicList2add.size() == 0) {
            return;
        }
        // 清空音乐
        nextMusicList.clear();
        // 添加到下一首
        nextMusicList.addAll(musicList2add);
        if (isRandomMode()) {
            shuffle();  // 初始化随机
            playMusic(randomNextMusicList.pollFirst(), null);
        } else {
            playMusic(nextMusicList.pollFirst(), null);
        }
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
        if (isRandomMode()) {
            return !randomNextMusicList.isEmpty();
        } else if (nextMusicList.isEmpty()) {
            return false;
        }
        if (currentMusic != null) {
            playedMusicList.addLast(currentMusic);
        }
        Music music;
        if (isRandomMode()) {
            music = randomNextMusicList.pollFirst();
        } else if (isListLoopMode()){
            music = nextMusicList.pollFirst();
            nextMusicList.addLast(music);
        } else if (isSingleLoopMode()) {
            // 不动
            music = currentMusic;
        } else {
            music = nextMusicList.pollFirst();
        }
        playMusic(music, null);
        return true;
    }

    public static boolean playLastMusic() {
        if (playedMusicList.isEmpty()) {
            return false;
        }
        if (currentMusic != null) {
            if (isRandomMode()) {
                randomNextMusicList.addFirst(currentMusic);
            } else {
                nextMusicList.addFirst(currentMusic);
            }
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
                for (OnAudioPlayerListener onAudioPlayerListener : onAudioPlayerListenerList) {
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
