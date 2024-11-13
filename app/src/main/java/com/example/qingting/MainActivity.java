package com.example.qingting;

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
import com.example.qingting.HomePage.HomePageFragment;
import com.example.qingting.SearchPage.SearchHistoryFragment;
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
        NavigationProvider.initNavigation(rootView, frameLayout);
    }



    @Override
    public void onBackPressed() {
        if (SearchHistoryFragment.getInstance().isAdded()) {
            HomePageFragment homePageFragment = HomePageFragment.getInstance();
            FragmentUtils.removeFragmentFromFragment(homePageFragment, (FrameLayout) homePageFragment.getView().findViewById(R.id.page_frame), SearchHistoryFragment.getInstance());
            return;
        }
        super.onBackPressed();
    }

}


class NavigationProvider {

    private static View currentView;
    private static FrameLayout frameLayout;
    private static HomePageFragment homePageFragment;
    private static ChatPageFragment chatPageFragment;
    private static UserPageFragment userPageFragment;
    static void initNavigation(View view, FrameLayout frameLayout1) {
        frameLayout = frameLayout1;
        homePageFragment = HomePageFragment.getInstance();
        chatPageFragment = ChatPageFragment.getInstance();
        userPageFragment = UserPageFragment.getInstance();

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
        final int drawableResouceId;
        if (pageId == R.id.home_page) {
            text = rootView.getResources().getString(R.string.home_page_name);
            pageFragment = homePageFragment;
            drawableResouceId = R.drawable.home_page_icon;
        } else if (pageId == R.id.chat_page){
            text = rootView.getResources().getString(R.string.chat_page_name);
            pageFragment = chatPageFragment;
            drawableResouceId = R.drawable.chat_page_icon;
        } else if (pageId == R.id.user_page) {
            text = rootView.getResources().getString(R.string.user_page_name);
            pageFragment = userPageFragment;
            drawableResouceId = R.drawable.user_page_icon;
        } else {
            text = "";
            pageFragment = null;
            drawableResouceId = -1;
        }
        im.setImageResource(drawableResouceId);
        tv.setText(text);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNavigationClickEvent(v, pageFragment);
            }
        });
    }


    /**
     * tiggerd when the navigation icon group is clicked
     * @param view the navigation icon group
     * @param fragment fragment would be put on the framelayout
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