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

import com.example.qingting.R;

public class MainActivity extends AppCompatActivity {

    Context context;
    LinearLayout navigationView;
    FrameLayout frameLayout;
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
        init();
    }

    private void init() {
        initNavigation();
    }


    /**
     * 初始化底部导航栏
     */
    private void initNavigation() {
        initHomePage();
        initChatPage();
        initUserPage();
    }


    private void initHomePage() {
        View view = findViewById(R.id.home_page);
        ImageView im = view.findViewById(R.id.navigation_icon);
        TextView tv = view.findViewById(R.id.navigation_text);
        im.setImageResource(R.drawable.home_page_icon);
        tv.setText("首页");
    }

    private void initChatPage() {
        View view = findViewById(R.id.chat_page);
        ImageView im = view.findViewById(R.id.navigation_icon);
        TextView tv = view.findViewById(R.id.navigation_text);
        im.setImageResource(R.drawable.chat_page_icon);
        tv.setText("聊天");
    }
    private void initUserPage() {
        View view = findViewById(R.id.user_page);
        ImageView im = view.findViewById(R.id.navigation_icon);
        TextView tv = view.findViewById(R.id.navigation_text);
        im.setImageResource(R.drawable.user_page_icon);
        tv.setText("我的");
    }
}