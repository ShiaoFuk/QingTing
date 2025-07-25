package com.example.qingting.page.PlayPage;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.qingting.bean.Music;
import com.example.qingting.R;
import com.example.qingting.utils.Play.AudioPlayUtils;
import com.example.qingting.utils.FragmentUtils;
import com.example.qingting.utils.Play.OnAudioPlayerListener;
import com.example.qingting.utils.TimeUtils;
import com.example.qingting.utils.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class PlayFragment extends BottomSheetDialogFragment {

    final static String TAG = PlayFragment.class.getName();
    static PlayFragment fragment;
    View rootView;
    BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    SeekBar seekBar;
    private PlayFragment() {
    }


    public static PlayFragment getInstance() {
        if (fragment == null) {
            fragment = new PlayFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    TextView songLengthLeftTextView;
    TextView songLengthTextView;
    ImageView album;
    ImageView playBtn;
    ImageView nextSongBtn;
    ImageView lastSongBtn;
    TextView titleTextView;
    TextView genreTextView;
    List<OnAudioPlayerListener> onAudioPlayerListenerList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_play, container, false);
        rootView.setAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.slide_in_bottom));
        seekBar = rootView.findViewById(R.id.seekbar);
        songLengthLeftTextView = rootView.findViewById(R.id.song_length_left);
        songLengthTextView = rootView.findViewById(R.id.song_length);
        album = rootView.findViewById(R.id.album);
        playBtn = rootView.findViewById(R.id.play);
        nextSongBtn = rootView.findViewById(R.id.next_song);
        lastSongBtn = rootView.findViewById(R.id.last_song);
        titleTextView = rootView.findViewById(R.id.title);
        genreTextView = rootView.findViewById(R.id.genre);
        init();
        return rootView;
    }

    private void init() {
        initBottomSheetBehavior();
        initAlbum();
        initCurrentMusic();
        initSeekbar();
        initPlayGroup();
    }


    private void initBottomSheetBehavior() {
        // Set up BottomSheetBehavior
        View bottomSheet = rootView.findViewById(R.id.root_view);
        bottomSheetBehavior = BottomSheetBehavior.from((ConstraintLayout) bottomSheet);
        bottomSheetBehavior.setPeekHeight(0, true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Activity activity = getActivity();
                    FrameLayout mainFrame = activity.findViewById(R.id.main);
                    FragmentUtils.removeFragmentFromActivity(mainFrame, fragment);
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    private void initAlbum() {
        ImageView imageView = rootView.findViewById(R.id.album);
        // 创建旋转动画
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotationAnimator.setDuration(8000); // 设置动画循环时间,每n秒转360度
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE); // 设置动画循环播放
        rotationAnimator.setRepeatMode(ObjectAnimator.RESTART); // 重启模式
        rotationAnimator.setInterpolator(new LinearInterpolator()); // 设置匀速插值器
        if (AudioPlayUtils.isPlaying())
            rotationAnimator.start(); // 启动旋转动画
        // 旋转动画回调
        OnAudioPlayerListener onAudioPlayerListener = new OnAudioPlayerListener() {
            @Override
            public void onStarted() {
                if (rotationAnimator.isPaused()) {
                    rotationAnimator.resume();
                } else {
                    rotationAnimator.start();
                }
            }

            @Override
            public void onPaused() {
                rotationAnimator.pause();
            }

            @Override
            public void onStopped() {
                rotationAnimator.pause();
            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onComplete() {
                rotationAnimator.pause();
            }
        };
        AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
    }

    boolean isEnd;
    Thread seekBarThread;
    void initSeekbar() {
        seekBarThread = new Thread(new Runnable() {
            final Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void run() {
                isEnd = false;
                while (!isEnd) {
                    while (!isEnd && AudioPlayUtils.isPlaying()) {
                        handler.post(() -> {
                            int currentTime = AudioPlayUtils.getCurrentPosition();
                            seekBar.setProgress(currentTime);
                            seekBar.setSecondaryProgress((int)(seekBar.getMax() * (AudioPlayUtils.getCacheProcess() / 100f)));
                            songLengthLeftTextView.setText(TimeUtils.milliSecs2MMss(currentTime));
                        });
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
            }
        });
        seekBarThread.start();
        Music music = AudioPlayUtils.getCurrentMusic();
        if (music != null) {
            doWithSeekBar();
        }
        OnAudioPlayerListener onAudioPlayerListener = new OnAudioPlayerListener() {
            @Override
            public void onStarted() {
                doWithSeekBar();
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
                // 回收seekbar
                if (!AudioPlayUtils.hasNext()) {
                    collectSeekBar();
                }
            }
        };
        AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
        onAudioPlayerListenerList.add(onAudioPlayerListener);
    }


    private void doWithSeekBar() {
        seekBar.setMax(AudioPlayUtils.getDuration());
        seekBar.setProgress(AudioPlayUtils.getCurrentPosition());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    AudioPlayUtils.seekTo(progress);
                    songLengthLeftTextView.setText(TimeUtils.milliSecs2MMss(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void collectSeekBar() {
        seekBar.setProgress(0);
        seekBar.setMax(0);
    }

    private void initCurrentMusic() {
        doWithCurrentText();
        OnAudioPlayerListener onAudioPlayerListener = new OnAudioPlayerListener() {
            @Override
            public void onStarted() {
                doWithCurrentText();
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
                if (!AudioPlayUtils.hasNext()) {
                    collectCurrentText();
                }
            }
        };
        AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
        onAudioPlayerListenerList.add(onAudioPlayerListener);
    }


    private void doWithCurrentText() {
        Music music = AudioPlayUtils.getCurrentMusic();
        if (music != null) {
            titleTextView.setText(music.getName());
            genreTextView.setText(music.getGenre());
            songLengthLeftTextView.setText(TimeUtils.milliSecs2MMss(AudioPlayUtils.getCurrentPosition()));
            songLengthTextView.setText(TimeUtils.milliSecs2MMss(AudioPlayUtils.getDuration()));
        }
    }

    private void collectCurrentText() {
        titleTextView.setText(rootView.getResources().getString(R.string.no_music_playing));
        genreTextView.setText("");
        songLengthLeftTextView.setText(rootView.getResources().getString(R.string.song_initial_length));
        songLengthTextView.setText(rootView.getResources().getString(R.string.song_initial_length));
    }

    private void initPlayGroup() {
        if (AudioPlayUtils.isPlaying()) {
            playBtn.setImageResource(R.drawable.pause_icon);
        } else {
            playBtn.setImageResource(R.drawable.play_icon);
        }
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AudioPlayUtils.isPlaying()) {
                    AudioPlayUtils.pause();
                } else {
                    AudioPlayUtils.resume();
                }
            }
        });
        nextSongBtn.setOnClickListener((v)->{
            if (!AudioPlayUtils.playNextMusic()) {
                ToastUtils.makeShortText(v.getContext(), v.getResources().getString(R.string.no_next_music));
            }
        });
        lastSongBtn.setOnClickListener((v)->{
            if (!AudioPlayUtils.playLastMusic()) {
                ToastUtils.makeShortText(v.getContext(), v.getResources().getString(R.string.no_last_music));
            }
        });
        OnAudioPlayerListener onAudioPlayerListener = new OnAudioPlayerListener() {
            @Override
            public void onStarted() {
                playBtn.setImageResource(R.drawable.pause_icon);
            }

            @Override
            public void onPaused() {
                playBtn.setImageResource(R.drawable.play_icon);
            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onComplete() {

            }
        };
        AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
        onAudioPlayerListenerList.add(onAudioPlayerListener);
    }



    public void expandBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    public void collapseBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public boolean isExpandBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) return true;
        return false;
    }



    @Override
    public void onDestroyView() {
        for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
            if (onAudioPlayerListener != null) {
                AudioPlayUtils.removeOnAudioPlayerListener(onAudioPlayerListener);
            }
        }
        isEnd = true;
        try {
            seekBarThread.join();
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
        super.onDestroyView();
    }

}