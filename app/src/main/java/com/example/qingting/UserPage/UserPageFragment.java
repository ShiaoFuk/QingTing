package com.example.qingting.UserPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qingting.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPageFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPageFragment extends Fragment {


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_page, container, false);
    }
}