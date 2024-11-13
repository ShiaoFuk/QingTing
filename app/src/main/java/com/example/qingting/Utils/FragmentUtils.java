package com.example.qingting.Utils;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {

    /**
     * add fragment to frameLayout, if fragment is null, would do nothing
     * @param frameLayout frameLayout where fragment will be added
     * @param fragment will be added to frameLayout(ignore if will be added to backstack)
     */
    public static void addFragmentToActivity(FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManagerByActivity(frameLayout);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameLayout.getId(), fragment);
        transaction.commit();
    }

    public static void addFragmentToFragment(Fragment parentFragment, FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManagerByFragment(parentFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameLayout.getId(), fragment);
        transaction.commit();
    }

    /**
     * add fragment to frameLayout as well as backstack, if fragment is null, would do nothing
     * @param frameLayout frameLayout where fragment will be added
     * @param fragment will be added to backstack
     * @return if successfully add fragment to backstack return true, else false
     */
    public static boolean addFragmentToBackStackToActivity(FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return false;
        FragmentManager fragmentManager = getFragmentManagerByActivity(frameLayout);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameLayout.getId(), fragment);
        if (!transaction.isAddToBackStackAllowed()) {
            transaction.commit();
            return false;
        }
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }


    public static boolean addFragmentToBackStackToFragment(Fragment parentFragment, FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return false;
        FragmentManager fragmentManager = getFragmentManagerByFragment(parentFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameLayout.getId(), fragment);
        if (!transaction.isAddToBackStackAllowed()) {
            transaction.commit();
            return false;
        }
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }


    /**
     * remove fragment from fragmentActivity, if fragment is null, would do nothing
     * @param frameLayout frameLayout where fragment will be added
     * @param fragment will be removed from frameLayout
     */
    public static void removeFragmentFromActivity(FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManagerByActivity(frameLayout);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public static void removeFragmentFromFragment(Fragment parentFragment, FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManagerByFragment(parentFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    /**
     * remove fragment in fragmentList from fragmentActivity, if fragment or  fragmentList is null, would do nothing
     * @param frameLayout frameLayout where fragments belong
     * @param fragmentList fragments will be removed from frameLayout
     */
    public static void removeFragmentsFromActivity(FrameLayout frameLayout, Fragment[] fragmentList) {
        if (fragmentList == null) return;
        for (Fragment fragment: fragmentList) {
            removeFragmentFromActivity(frameLayout, fragment);
        }
    }

    public static void removeFragmentsFromFragment(Fragment parentFragment, FrameLayout frameLayout, Fragment[] fragmentList) {
        if (fragmentList == null) return;
        for (Fragment fragment: fragmentList) {
            removeFragmentFromFragment(parentFragment, frameLayout, fragment);
        }
    }

    /**
     * replace fragment into frameLayout, if fragment is null, would do nothing
     * @param frameLayout frameLayout where fragment will be added
     * @param fragment will be added to frameLayout(ignore if will be added to backstack)
     */
    public static void replaceFragmentToActivity(FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManagerByActivity(frameLayout);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }


    public static void replaceFragmentToFragment(Fragment parentFragment, FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManagerByFragment(parentFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }





    /**
     * get fragment manager of frameLayout, for the frameLayout may belong to a activity or a fragment, then the parent of it should be judge before the child fragment would be added
     * @param frameLayout frameLayout where the fragment  will be added
     * @return the parent fragmentManager of frameLayout
     */
    private static FragmentManager getFragmentManagerByActivity(FrameLayout frameLayout) {
        // 如果父视图是 Activity 的容器视图
        Context context = frameLayout.getContext();
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        return fragmentActivity.getSupportFragmentManager(); // 返回 Activity 的 FragmentManager
    }

    private static FragmentManager getFragmentManagerByFragment(Fragment parentFragment) {
        return parentFragment.getChildFragmentManager();
    }




}
