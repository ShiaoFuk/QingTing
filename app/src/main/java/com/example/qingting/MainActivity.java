package com.example.qingting;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.qingting.Bean.Music;
import com.example.qingting.ChatPage.ChatPageFragment;
import com.example.qingting.HomePage.HomePageFragment;
import com.example.qingting.Utils.Play.AudioPlayUtils;
import com.example.qingting.PlayPage.PlayFragment;
import com.example.qingting.UserPage.UserPageFragment;
import com.example.qingting.Utils.FragmentUtils;
import com.example.qingting.Utils.Play.OnAudioPlayerListener;
import com.example.qingting.Utils.TimeUtils;
import com.example.qingting.Utils.TintUtils;
import com.king.view.circleprogressview.CircleProgressView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getName();
    Context context;
    LinearLayout navigationView;
    FrameLayout frameLayout;
    View rootView;
    List<OnAudioPlayerListener> onAudioPlayerListenerList = new ArrayList<>();
    Thread seekBarThread;
    boolean isEnd;
    CircleProgressView circleProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        context = this;
        navigationView = findViewById(R.id.navigation_bar);
        frameLayout = findViewById(R.id.page_frame);
        rootView = getWindow().getDecorView();
        circleProgressView = findViewById(R.id.progress_view);
        init();
    }


    private void init() {
        initPlayBar();
        initNavigation();
        initPlayBtn();
        initPlayBarTitle();
        initSeekBar();
    }


    private void initPlayBar() {
        View playBarIcon = findViewById(R.id.play_bar_icon);
        View playBar = findViewById(R.id.play_bar);
        playBarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayBarClickEvent(v);
            }
        });
        playBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayBarClickEvent(v);
            }
        });
    }

    private void initPlayBarTitle() {
        TextView titleView = findViewById(R.id.play_bar_title);
        TextView genreView = findViewById(R.id.play_bar_genre);
        OnAudioPlayerListener onAudioPlayerListener = new OnAudioPlayerListener() {
            @Override
            public void onStarted() {
                Music music = AudioPlayUtils.getCurrentMusic();
                if (music != null) {
                    titleView.setText(music.getName());
                    genreView.setText(music.getGenre());
                }
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
                    titleView.setText(rootView.getResources().getString(R.string.no_music_playing));
                    genreView.setText("");
                }
            }
        };
        AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
    }


    private void initPlayBtn() {
        ConstraintLayout playGroup = findViewById(R.id.play_group);
        ImageView imageView = findViewById(R.id.main_play_btn);
        playGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AudioPlayUtils.isPlaying()) {
                    AudioPlayUtils.pause();
                }
                else {
                    AudioPlayUtils.resume();
                }
            }
        });
        OnAudioPlayerListener onAudioPlayerListener = new OnAudioPlayerListener() {
            @Override
            public void onStarted() {
                circleProgressView.setMax(AudioPlayUtils.getDuration());
                circleProgressView.setProgress(AudioPlayUtils.getCurrentPosition());
                imageView.setImageResource(R.drawable.pause_icon);
            }

            @Override
            public void onPaused() {
                imageView.setImageResource(R.drawable.play_icon);
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
                    circleProgressView.setProgress(0);
                    circleProgressView.setMax(0);
                }
            }
        };
        onAudioPlayerListenerList.add(onAudioPlayerListener);
        AudioPlayUtils.addOnAudioPlayerListener(onAudioPlayerListener);
    }


    @Override
    protected void onDestroy() {
        isEnd = true;
        try {
            seekBarThread.join();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        AudioPlayUtils.stopAndRelease();
        super.onDestroy();
    }

    private void setPlayBarClickEvent(View view) {
        // 添加播放页面的fragment
        FrameLayout rootFrame = findViewById(R.id.main);
        if (!PlayFragment.getInstance().isAdded())
            FragmentUtils.addFragmentToBackStackToActivity(rootFrame, PlayFragment.getInstance());
    }

    /**
     * 初始化底部导航栏
     */
    private void initNavigation() {
        NavigationProvider.initNavigation(rootView, frameLayout);
    }


    private void initSeekBar() {
        seekBarThread = new Thread(new Runnable() {
            final Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void run() {
                isEnd = false;
                while (!isEnd) {
                    while (!isEnd && AudioPlayUtils.isPlaying()) {
                        handler.post(() -> {
                            int currentTime = AudioPlayUtils.getCurrentPosition();
                            circleProgressView.setProgress(currentTime);
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
    }



    @Override
    public void onBackPressed() {
        if (PlayFragment.getInstance().isAdded() && PlayFragment.getInstance().isExpandBottomSheet()) {
            PlayFragment.getInstance().collapseBottomSheet();
            return;
        }
        if (HomePageFragment.getInstance().isAdded() && HomePageFragment.getInstance().removeChildFragments()) {
            return;
        }

        super.onBackPressed();
    }


}


class NavigationProvider {

    private static View currentView;
    private static FrameLayout frameLayout;
    static void initNavigation(View view, FrameLayout frameLayout1) {
        frameLayout = frameLayout1;
        // 初始化要切换到homePage页面
        initHomePage(view);
        initChatPage(view);
        initUserPage(view);
        initEnd(view);
    }


    private static void initHomePage(View rootView) {
        initPage(rootView, R.id.home_page);
    }
    private static void initChatPage(View rootView) {
        initPage(rootView, R.id.chat_page);
    }
    private static void initUserPage(View rootView) {
        initPage(rootView, R.id.user_page);
    }


    private static void initPage(View rootView, int pageId) {
        View view = rootView.findViewById(pageId).findViewById(R.id.navigation_layout);
        ImageView im = view.findViewById(R.id.navigation_icon);
        TextView tv = view.findViewById(R.id.navigation_text);
        final Fragment pageFragment;
        final String text;
        final int drawableResourceId;
        if (pageId == R.id.home_page) {
            text = rootView.getResources().getString(R.string.home_page_name);
            pageFragment = HomePageFragment.getInstance();
            drawableResourceId = R.drawable.home_page_icon;
        } else if (pageId == R.id.chat_page){
            text = rootView.getResources().getString(R.string.chat_page_name);
            pageFragment = ChatPageFragment.getInstance();
            drawableResourceId = R.drawable.chat_page_icon;
        } else if (pageId == R.id.user_page) {
            text = rootView.getResources().getString(R.string.user_page_name);
            pageFragment = UserPageFragment.getInstance();
            drawableResourceId = R.drawable.user_page_icon;
        } else {
            text = "";
            pageFragment = null;
            drawableResourceId = -1;
        }
        im.setImageResource(drawableResourceId);
        tv.setText(text);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNavigationClickEvent(v, pageFragment);
            }
        });
    }


    /**
     * triggered when the navigation icon group is clicked
     * @param view the navigation icon group
     * @param fragment fragment would be put on the frameLayout
     */
    private static void initNavigationClickEvent(View view, Fragment fragment) {
        setCurrentColor(currentView, view);
        currentView = view;
        if (fragment.isAdded()) return;
        FragmentUtils.replaceFragmentToActivity(frameLayout, fragment);
    }


    /**
     * switch the navigation color when switch the fragment by touching navigation color
     * @param oldView the previous selected navigation icon group, if no then pass null
     * @param newView the navigation icon group been clicked
     */
    private static void setCurrentColor(View oldView, View newView) {
        if (oldView != null) {
            ImageView oldImageView = oldView.findViewById(R.id.navigation_icon);
            TextView oldTV = oldView.findViewById(R.id.navigation_text);
            TintUtils.setImageViewTint(oldImageView, oldView.getResources().getColor(R.color.gray));
            oldTV.setTextColor(oldView.getResources().getColor(R.color.gray));
        }
        ImageView newImgView = newView.findViewById(R.id.navigation_icon);
        TextView newTV = newView.findViewById(R.id.navigation_text);
        TintUtils.setImageViewTint(newImgView, newView.getResources().getColor(R.color.green));
        newTV.setTextColor(newView.getResources().getColor(R.color.green));
    }

    private static void initEnd(View rootView) {
        View view = rootView.findViewById(R.id.home_page).findViewById(R.id.navigation_layout);
        view.performClick();
    }



}