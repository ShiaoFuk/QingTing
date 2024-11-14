package com.example.qingting.HomePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qingting.R;


public class SearchResultFragment extends Fragment {
    static SearchResultFragment fragment;
    View rootView;
    private SearchResultFragment() {
    }


    static SearchResultFragment getInstance() {
        if (fragment == null)
            fragment = new SearchResultFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
        return rootView;
    }
}