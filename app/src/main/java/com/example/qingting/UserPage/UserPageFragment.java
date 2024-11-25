package com.example.qingting.UserPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qingting.Login.LoginActivity;
import com.example.qingting.R;
import com.example.qingting.Setting.SettingActivity;
import com.example.qingting.Utils.JwtUtil;
import com.example.qingting.Utils.ToastUtils;
import com.example.qingting.data.SP.LoginSP;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Date;

import io.jsonwebtoken.JwtException;


public class UserPageFragment extends Fragment {
    View rootView;
    Date loginExpireTime;  // 登录过期时间,如果已经过期会被置为null,没有登录也是null
    final static String TAG = UserPageFragment.class.getName();

    private static UserPageFragment fragment;
    private UserPageFragment() {
        // Required empty public constructor
    }


    public static UserPageFragment getInstance() {
        if (fragment == null) fragment = new UserPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_page, container, false);
        init();
        return rootView;
    }

    private void init() {
        initLoginInfo();
        initUserInfo();
        initPlayList();
    }


    @Override
    public void onDestroyView() {
        try {
            if (checkLoginThread != null)
                checkLoginThread.join(1000);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
        super.onDestroyView();
    }

    Thread checkLoginThread;
    private void initLoginInfo() {
        // 初始化过期时间
        String token = LoginSP.getToken(rootView.getContext());
        try {
            loginExpireTime = JwtUtil.getExpireTime(token);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            loginExpireTime = null;
        }
        checkLoginThread = new Thread(new Runnable() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void run() {
                while (true) {
                    if (JwtUtil.checkIfTokenExpire(loginExpireTime)) {
                        handler.post(()-> {
                            ToastUtils.makeShortText(rootView.getContext(), rootView.getResources().getString(R.string.login_expire_message));
                            TextView textView = rootView.findViewById(R.id.user_name);
                            setLoginExpire(textView);
                        });
                        break;
                    }
                    // 一秒检测一次登录状态
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    private void initUserInfo() {
        initUserAvatar();
        initUserName();
        initSetting();
    }


    private void initPlayList() {
        initPlayListTabs();
        initPlayListViewPager();
    }

    private void initUserAvatar() {
        ShapeableImageView imageView = rootView.findViewById(R.id.avatar);
        imageView.setOnClickListener(this::loginListenEvent);
        // 发起请求获取用户头像
    }

    private void initUserName() {
        TextView textView = rootView.findViewById(R.id.user_name);
        if (loginExpireTime != null) {
            textView.setText(rootView.getResources().getString(R.string.default_username));
        }
        textView.setOnClickListener(this::loginListenEvent);
        // 发起请求获取用户名
    }

    private void setLoginExpire(TextView textView) {
        textView.setText(textView.getResources().getString(R.string.no_login_username));
    }

    private void initSetting() {
        ImageView imageView = rootView.findViewById(R.id.setting);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initPlayListTabs() {
        // TODO:initTabs
    }

    private void initPlayListViewPager() {
        // TODO:initViewPager
    }


    private void loginListenEvent(View v) {
        if (loginExpireTime != null && !JwtUtil.checkIfTokenExpire(loginExpireTime)) {
            return;
        }
        // 跳转登录
        Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
        startActivity(intent);
    }



}