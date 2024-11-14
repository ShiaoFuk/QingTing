package com.example.qingting.HomePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.qingting.R;


public class HomePageContentFragment extends Fragment {
    static HomePageContentFragment fragment;
    private View rootView;
    private HomePageContentFragment() {

    }

    static HomePageContentFragment getInstance() {
        if (fragment == null)
            fragment = new HomePageContentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_page_content, container, false);
        rootView.setAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.slide_in_bottom));
        return rootView;
    }
}