package com.example.qingting.PlayPage;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.qingting.Bean.Music;
import com.example.qingting.MyApplication;
import com.example.qingting.R;
import com.example.qingting.Utils.Play.AudioPlayUtils;
import com.example.qingting.Utils.FragmentUtils;
import com.example.qingting.Utils.Play.OnAudioPlayerListener;
import com.example.qingting.Utils.TimeUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class PlayFragment extends BottomSheetDialogFragment {

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
    ImageView forwardBtn;
    ImageView rewindBtn;
    TextView titleTextView;
    TextView genreTextView;
    List<OnAudioPlayerListener> onAudioPlayerListenerList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_play, container, false);
        rootView.setAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.slide_in_bottom));
        seekBar = rootView.findViewById(R.id.seekbar);
        songLengthTextView = rootView.findViewById(R.id.song_length);
        album = rootView.findViewById(R.id.album);
        playBtn = rootView.findViewById(R.id.play_btn);
        forwardBtn = rootView.findViewById(R.id.fast_forward);
        rewindBtn = rootView.findViewById(R.id.rewind);
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
                rotationAnimator.start();
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


    void initSeekbar() {

    }


    @Override
    public void onDestroyView() {
        for (OnAudioPlayerListener onAudioPlayerListener: onAudioPlayerListenerList) {
            if (onAudioPlayerListener != null) {
                AudioPlayUtils.removeOnAudioPlayerListener(onAudioPlayerListener);
            }
        }
        super.onDestroyView();
    }

    private void initCurrentMusic() {
        Music music = AudioPlayUtils.getCurrentMusic();
        titleTextView.setText(music.getName());
        genreTextView.setText(music.getGenre());
        songLengthTextView.setText(TimeUtils.milliSecs2MMss(AudioPlayUtils.getCurrentPosition()));
        songLengthTextView.setText(TimeUtils.milliSecs2MMss(AudioPlayUtils.getDuration()));
        OnAudioPlayerListener onAudioPlayerListener = new OnAudioPlayerListener() {
            @Override
            public void onStarted() {
                Music music = AudioPlayUtils.getCurrentMusic();
                titleTextView.setText(music.getName());
                genreTextView.setText(music.getGenre());
                songLengthTextView.setText(TimeUtils.milliSecs2MMss(AudioPlayUtils.getCurrentPosition()));
                songLengthTextView.setText(TimeUtils.milliSecs2MMss(AudioPlayUtils.getDuration()));
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

}