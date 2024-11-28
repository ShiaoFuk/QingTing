package com.example.qingting.HomePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.qingting.Adapter.HotListAdapter;
import com.example.qingting.Bean.Music;
import com.example.qingting.R;
import com.example.qingting.data.DB.MusicDB;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SearchHistoryFragment extends Fragment {
    private static SearchHistoryFragment fragment;
    private SearchHistoryFragment() {

    }

    private EditText search;

    // 要确保这个fragment被添加到fragment上而不是activity上
    static SearchHistoryFragment getInstance(EditText editText) {
        if (fragment == null)
            fragment = new SearchHistoryFragment();
        fragment.search = editText;
        return fragment;
    }

    static SearchHistoryFragment getInstance() {
        if (fragment == null)
            fragment = new SearchHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_history, container, false);
        init();
        return rootView;
    }

    private void init() {
        initHotList();
    }

    private void initHotList() {
        RecyclerView recyclerView = rootView.findViewById(R.id.hot_list);
        // 发起网络请求获取热门歌曲列表后用于初始化contentList
        // TODO:设置为音乐列表，用于测试搜索，暂时还没做音乐推荐模块
        List<Music> musicList = MusicDB.getMusicListDefault(rootView.getContext());
        List<String> strList = musicList.stream().map((music)->music.getName()).collect(Collectors.toList());
        HotListAdapter hostListAdapter = new HotListAdapter(search, strList);
        recyclerView.setAdapter(hostListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    }


}