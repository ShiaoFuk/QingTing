package com.example.qingting.UserPage;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qingting.Adapter.PlayListViewPagerAdapter;
import com.example.qingting.Bean.PlayList;
import com.example.qingting.Login.LoginActivity;
import com.example.qingting.MyApplication;
import com.example.qingting.R;
import com.example.qingting.Setting.SettingActivity;
import com.example.qingting.Utils.DialogUtils;
import com.example.qingting.Utils.JwtUtil;
import com.example.qingting.Utils.ToastUtils;
import com.example.qingting.data.DB.PlayListDB;
import com.example.qingting.data.SP.LoginSP;
import com.example.qingting.net.request.PlayListRequest.InsertPlayListRequest;
import com.example.qingting.net.request.RequestListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;

import io.jsonwebtoken.JwtException;


public class UserPageFragment extends Fragment {
    View rootView;
    Date loginExpireTime;  // 登录过期时间,如果已经过期会被置为null,没有登录也是null
    final static String TAG = UserPageFragment.class.getName();
    final Handler handler = new Handler(Looper.getMainLooper());
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

    boolean firstIn = true;
    @Override
    public void onResume() {
        super.onResume();
        if (firstIn) {
            firstIn = false;
        } else {
            init();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_page, container, false);
        viewPager2 = rootView.findViewById(R.id.play_list_viewpager);
        tabLayout = rootView.findViewById(R.id.play_list_tabs);
        init();
        return rootView;
    }

    private void init() {
        initLoginInfo();
        initUserInfo();
        initPlayList();
        initAddPlayListBtn();
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
        } catch (JwtException e) {
            Log.e(TAG, e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, rootView.getResources().getString(R.string.nullptr_token));
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
        initPlayListViewPager();
        initPlayListTabs();
    }


    private void initAddPlayListBtn() {
        ImageView btn = rootView.findViewById(R.id.add_playlist_icon);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showInputDialog(rootView.getContext(),
                        rootView.getResources().getString(R.string.add_playlist_title),
                        rootView.getResources().getString(R.string.add_playlist_message),
                        rootView.getResources().getString(R.string.sure_message),
                        new DialogUtils.DialogCallback() {
                            @Override
                            public void doSth(View view, String input) {
                                // 添加歌单
                                InsertPlayListRequest.insertPlayList(new RequestListener() {
                                    @Override
                                    public Object onPrepare(Object object) {
                                        return null;
                                    }

                                    @Override
                                    public void onRequest() {

                                    }

                                    @Override
                                    public void onReceive() {

                                    }

                                    @Override
                                    public void onSuccess(JsonElement element) throws Exception {
                                        JsonObject jsonObject = element.getAsJsonObject();
                                        if (jsonObject.get("code") != null && jsonObject.get("code").getAsInt() == 200) {
                                            // 插入成功，更新全局list
                                            MyApplication.getPlayListFromNet(rootView.getContext());
                                            // 构建一个playlist,直接添加,然后notified data set changed
                                            PlayList playList = new PlayList();
                                            playList.setName(input);
                                            playList.setId(jsonObject.get("data").getAsInt());
                                            PlayListViewPagerAdapter adapter = (PlayListViewPagerAdapter) viewPager2.getAdapter();
                                            handler.post(()->{
                                                if (UserPageFragment.getInstance().isAdded()) {
                                                    adapter.addPlayList(playList);
                                                    Log.e(TAG, "update adapter");
                                                }
                                            });
                                            ToastUtils.makeShortText(rootView.getContext(), rootView.getResources().getString(R.string.insert_playlist_success));
                                        } else {
                                            Log.e(TAG, jsonObject.get("message").toString());
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        ToastUtils.makeShortText(rootView.getContext(), rootView.getResources().getString(R.string.insert_playlist_fail));
                                        Log.e(TAG, e.getMessage());
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                }, LoginSP.getToken(rootView.getContext()), input);
                            }
                        }, null);
            }
        });
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

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    /**
     * 将tablayout和viewpager2关联起来
     */
    private void initPlayListTabs() {
        final String[] titleList = new String[]{
                rootView.getResources().getString(R.string.tab_name_default),
                rootView.getResources().getString(R.string.tab_name_order_by_name),
        };
        new TabLayoutMediator(tabLayout, viewPager2, true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titleList[position]);
            }
        }).attach();
    }

    private void initPlayListViewPager() {
        if (loginExpireTime != null) {
//            getPlayList();
        }
        // 添加adapter
        List<List<PlayList>> playListListList = MyApplication.getPlayListListList();;
        if (MyApplication.isPlayListModified()) {
            // 如果被修改过，异步加载
            new Thread(()->{
                final List<List<PlayList>> subPlayListListList = PlayListDB.getPlayListListList(rootView.getContext());
                MyApplication.setPlayListListList(subPlayListListList);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (UserPageFragment.getInstance().isAdded()) {
                            PlayListViewPagerAdapter adapter = (PlayListViewPagerAdapter) UserPageFragment.getInstance().viewPager2.getAdapter();
                            adapter.updateData(subPlayListListList);
                            Log.e(TAG, "update adapter");
                        }
                    }
                });
                MyApplication.setPlayListNoModified();
            }).start();
        }
        viewPager2.setOffscreenPageLimit(2);  // 预加载2个页面，使得切换流畅
        viewPager2.setAdapter(new PlayListViewPagerAdapter(playListListList));
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