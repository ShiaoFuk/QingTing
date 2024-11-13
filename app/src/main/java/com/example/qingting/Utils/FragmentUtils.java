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
     * @param fromFragment is framelayout from fragment
     */
    public static void addFragment(FrameLayout frameLayout, Fragment fragment, boolean fromFragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManager(frameLayout, fromFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameLayout.getId(), fragment);
        transaction.commit();
    }

    /**
     * add fragment to frameLayout as well as backstack, if fragment is null, would do nothing
     * @param frameLayout frameLayout where fragment will be added
     * @param fragment will be added to backstack
     * @param fromFragment is framelayout from fragment
     * @return if successfully add fragment to backstack return true, else false
     */
    public static boolean addFragmentToBackStack(FrameLayout frameLayout, Fragment fragment, boolean fromFragment) {
        if (fragment == null) return false;
        FragmentManager fragmentManager = getFragmentManager(frameLayout, fromFragment);
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
     * @param fromFragment is framelayout from fragment
     */
    public static void removeFragment(FrameLayout frameLayout, Fragment fragment, boolean fromFragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManager(frameLayout, fromFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    /**
     * remove fragment in fragmentList from fragmentActivity, if fragment or  fragmentList is null, would do nothing
     * @param frameLayout frameLayout where fragments belong
     * @param fragmentList fragments will be removed from frameLayout
     * @param fromFragment is framelayout from fragment
     */
    public static void removeFragments(FrameLayout frameLayout, Fragment[] fragmentList, boolean fromFragment) {
        if (fragmentList == null) return;
        for (Fragment fragment: fragmentList) {
            removeFragment(frameLayout, fragment, fromFragment);
        }
    }

    /**
     * replace fragment into frameLayout, if fragment is null, would do nothing
     * @param frameLayout frameLayout where fragment will be added
     * @param fragment will be added to frameLayout(ignore if will be added to backstack)
     * @param fromFragment is framelayout from fragment
     */
    public static void replaceFragment(FrameLayout frameLayout, Fragment fragment, boolean fromFragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = getFragmentManager(frameLayout, fromFragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }

    /**
     * get fragment manager of frameLayout, for the frameLayout may belong to a activity or a fragment, then the parent of it should be judge before the child fragment would be added
     * @param frameLayout frameLayout where the fragment  will be added
     * @param fromFragment is framelayout from fragment
     * @return the parent fragmentManager of frameLayout
     */
    private static FragmentManager getFragmentManager(FrameLayout frameLayout, boolean fromFragment) {
        if (fromFragment) {
            // 获取父 Fragment 的 ChildFragmentManager
            Fragment fragment = (Fragment) frameLayout.getParent();
            return fragment.getChildFragmentManager(); // 返回 Fragment 的 ChildFragmentManager
        }
        // 如果父视图是 Activity 的容器视图
        Context context = frameLayout.getContext();
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        return fragmentActivity.getSupportFragmentManager(); // 返回 Activity 的 FragmentManager
    }


}
