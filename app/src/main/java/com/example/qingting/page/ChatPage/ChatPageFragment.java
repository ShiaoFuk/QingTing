package com.example.qingting.page.ChatPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qingting.R;


public class ChatPageFragment extends Fragment {
    private static ChatPageFragment fragment;
    private ChatPageFragment() {
        // Required empty public constructor
    }

    public static ChatPageFragment getInstance() {
        if (fragment == null)
            fragment = new ChatPageFragment();
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
        return inflater.inflate(R.layout.fragment_chat_page, container, false);
    }
}