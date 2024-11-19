package com.example.qingting.HomePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qingting.Adapter.HotListAdapter;
import com.example.qingting.R;

import java.util.ArrayList;
import java.util.List;


public class SearchHistoryFragment extends Fragment {
    private static SearchHistoryFragment fragment;
    private SearchHistoryFragment() {

    }

    // 要确保这个fragment被添加到fragment上而不是activity上
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
        List<String> arrayList = new ArrayList<>();
        arrayList.add("test");
        arrayList.add("test1");
        arrayList.add("test2");
        arrayList.add("test3");
        arrayList.add("test4");
        // 发起网络请求获取热门歌曲列表后用于初始化contentList
        HotListAdapter hostListAdapter = new HotListAdapter(arrayList);
        recyclerView.setAdapter(hostListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    }


}