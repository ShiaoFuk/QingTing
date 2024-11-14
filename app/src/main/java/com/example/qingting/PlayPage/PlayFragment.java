package com.example.qingting.PlayPage;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.qingting.R;
import com.example.qingting.Utils.FragmentUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class PlayFragment extends BottomSheetDialogFragment {

    static PlayFragment fragment;
    View rootView;
    BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private PlayFragment() {
    }


    public static PlayFragment getInstance() {
        if (fragment == null) {
            fragment = new PlayFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_play, container, false);
        init();
        return rootView;
    }

    private void init() {
        initBottomSheetBehavior();
    }

    private void initBottomSheetBehavior() {
        // Set up BottomSheetBehavior
        View bottomSheet = rootView.findViewById(R.id.root_view);
        bottomSheetBehavior = BottomSheetBehavior.from((ConstraintLayout) bottomSheet);
        bottomSheetBehavior.setPeekHeight(0, true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Activity activity = getActivity();
                    FrameLayout mainFrame = activity.findViewById(R.id.main);
                    FragmentUtils.removeFragmentFromActivity(mainFrame, fragment);
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void expandBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    public void collapseBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

}