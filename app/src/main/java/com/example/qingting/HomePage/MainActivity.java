package com.example.qingting.HomePage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.qingting.ChatPage.ChatPageFragment;
import com.example.qingting.R;
import com.example.qingting.UserPage.UserPageFragment;
import com.example.qingting.Utils.FragmentUtils;
import com.example.qingting.Utils.TintUtils;

public class MainActivity extends AppCompatActivity {

    Context context;
    LinearLayout navigationView;
    FrameLayout frameLayout;
    View rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context = this;
        navigationView = findViewById(R.id.navigation_bar);
        frameLayout = findViewById(R.id.page_frame);
        rootView = getWindow().getDecorView();
        init();
    }

    private void init() {
        initNavigation();
    }


    /**
     * 初始化底部导航栏
     */
    private void initNavigation() {
        NavigationProvider.initNavigation(this, rootView, frameLayout);
    }



}


class NavigationProvider {

    private static View currentView;
    private static FrameLayout frameLayout;
    private static MainActivity mainActivity;
    private static HomePageFragment homePageFragment;
    private static ChatPageFragment chatPageFragment;
    private static UserPageFragment userPageFragment;
    static void initNavigation(MainActivity mainActivity1, View view, FrameLayout frameLayout1) {
        mainActivity = mainActivity1;
        frameLayout = frameLayout1;
        homePageFragment = HomePageFragment.newInstance();
        chatPageFragment = ChatPageFragment.newInstance();
        userPageFragment = UserPageFragment.newInstance();
        initHomePage(view);
        initChatPage(view);
        initUserPage(view);
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
        im.setImageResource(R.drawable.user_page_icon);
        final Fragment pageFragment;
        final String text;
        if (pageId == R.id.home_page) {
            text = "主页";
            pageFragment = homePageFragment;
        } else if (pageId == R.id.chat_page){
            text = "聊天";
            pageFragment = chatPageFragment;
        } else if (pageId == R.id.user_page) {
            text = "我的";
            pageFragment = userPageFragment;
        } else {
            text = "";
            pageFragment = null;
        }
        tv.setText(text);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragmentClickListener(v, pageFragment);
            }
        });
    }


    private static void initFragmentClickListener(View view, Fragment fragment) {
        setCurrentColor(currentView, view);
        currentView = view;
        if (fragment.isAdded()) return;
        FragmentUtils.replaceFragment(frameLayout, fragment);
    }

    /**
     * 切换的时候要切换导航栏按钮的颜色
     * @param oldView 原来被选中的linearlayout，第一次选中的时候没有oldView，传null
     * @param newView 即将被选中的linearlayout
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
}