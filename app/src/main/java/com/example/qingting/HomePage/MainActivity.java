package com.example.qingting.HomePage;

import android.content.Context;
import android.graphics.Color;
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

import com.example.qingting.R;
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
        NavigationProvider.initNavigation(rootView);

    }



}


class NavigationProvider {

    final static int SELECTED_COLOR = Color.rgb(118, 194, 175);
    final static int DEFAULT_COLOR = Color.GRAY;
    private static View currentView = null;
    static void initNavigation(View view) {
        initHomePage(view);
        initChatPage(view);
        initUserPage(view);
    }

    private static void initHomePage(View rootView) {
        View view = rootView.findViewById(R.id.home_page).findViewById(R.id.navigation_layout);
        ImageView im = view.findViewById(R.id.navigation_icon);
        TextView tv = view.findViewById(R.id.navigation_text);
        im.setImageResource(R.drawable.home_page_icon);
        tv.setText("首页");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentColor(currentView, view);
                currentView = view;
            }
        });
    }

    private static void initChatPage(View rootView) {
        View view = rootView.findViewById(R.id.chat_page).findViewById(R.id.navigation_layout);
        ImageView im = view.findViewById(R.id.navigation_icon);
        TextView tv = view.findViewById(R.id.navigation_text);
        im.setImageResource(R.drawable.chat_page_icon);
        tv.setText("聊天");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentColor(currentView, view);
                currentView = view;
            }
        });
    }
    private static void initUserPage(View rootView) {
        View view = rootView.findViewById(R.id.user_page).findViewById(R.id.navigation_layout);
        ImageView im = view.findViewById(R.id.navigation_icon);
        TextView tv = view.findViewById(R.id.navigation_text);
        im.setImageResource(R.drawable.user_page_icon);
        tv.setText("我的");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentColor(currentView, view);
                currentView = view;
            }
        });
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
            TintUtils.setImageViewTint(oldImageView, DEFAULT_COLOR);
            oldTV.setTextColor(DEFAULT_COLOR);
        }
        ImageView newImgView = newView.findViewById(R.id.navigation_icon);
        TextView newTV = newView.findViewById(R.id.navigation_text);
        TintUtils.setImageViewTint(newImgView, SELECTED_COLOR);
        newTV.setTextColor(SELECTED_COLOR);
    }
}