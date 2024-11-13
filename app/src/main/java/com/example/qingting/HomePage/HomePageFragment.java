package com.example.qingting.HomePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.qingting.R;
import com.example.qingting.SearchPage.SearchHistoryFragment;
import com.example.qingting.Utils.FragmentUtils;

public class HomePageFragment extends Fragment {
    private static HomePageFragment fragment;
    View rootView;
    FrameLayout frameLayout;
    SearchHistoryFragment searchHistoryFragment;
    EditText search;
    private HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment getInstance() {
        if (fragment == null)
            fragment = new HomePageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        frameLayout = rootView.findViewById(R.id.page_frame);
        searchHistoryFragment = SearchHistoryFragment.getInstance();
        init();
        return rootView;
    }

    private void init() {
        initSearch();  // 初始化搜索框
    }

    private void initSearch() {
        View topBar = rootView.findViewById(R.id.top_bar);
        search = rootView.findViewById(R.id.search);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    topBar.setVisibility(View.INVISIBLE);
                    if (searchHistoryFragment.isAdded())
                        FragmentUtils.removeFragmentFromFragment(fragment, frameLayout, searchHistoryFragment);
                    return;
                }
                // focus在搜索框的时候要替换另一个fragment
                topBar.setVisibility(View.GONE);
                if (!searchHistoryFragment.isAdded()) {
                    boolean isAddToBackStack = FragmentUtils.addFragmentToBackStackToFragment(fragment, frameLayout, searchHistoryFragment);
                }
            }
        });
    }

    public void clearSearchFocus() {
        search.clearFocus();
    }
}