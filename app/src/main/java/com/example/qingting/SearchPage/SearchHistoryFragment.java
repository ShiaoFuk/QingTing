package com.example.qingting.SearchPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qingting.HomePage.HomePageFragment;
import com.example.qingting.R;


public class SearchHistoryFragment extends Fragment {
    private static SearchHistoryFragment fragment;
    private SearchHistoryFragment() {
    }
    public static SearchHistoryFragment getInstance() {
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
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        // 要确保这个fragment被添加到fragment上而不是activity上
        HomePageFragment homePageFragment = (HomePageFragment) getParentFragment();
        homePageFragment.clearSearchFocus();
    }

}