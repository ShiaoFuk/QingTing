package com.example.qingting.Utils;

import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {

    /**
     * add fragment to framelayout, if fragment is null, would do nothing
     * @param fragmentActivity activity where framelayout belong
     * @param frameLayout framelayout where fragment will be added
     * @param fragment will be added to framelayout(ignore if will be added to backstack)ss
     */
    public static void addFragment(FragmentActivity fragmentActivity, FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return;
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameLayout.getId(), fragment);
        transaction.commit();
    }

    /**
     * add fragment to framelayout as well as backstack, if fragment is null, would do nothing
     * @param fragmentActivity activity where framelayout belong
     * @param frameLayout framelayout where fragment will be added
     * @param fragment will be added to backstack
     * @return if successfully add fragment to backstack return true, else false
     */
    public static boolean addFragmentToBackStack(FragmentActivity fragmentActivity, FrameLayout frameLayout, Fragment fragment) {
        if (fragment == null) return false;
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
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
     * @param fragmentActivity activity where fragment belong
     * @param fragment will be removed from framelayout
     */
    public static void removeFragment(FragmentActivity fragmentActivity, Fragment fragment) {
        if (fragment == null) return;
//        FragmentActivity activity = frameLayout.getContext();
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    /**
     * remove fragment in fragmentList from fragmentActivity, if fragment or  fragmentList is null, would do nothing
     * @param fragmentActivity activity where fragments belong
     * @param fragmentList fragments will be removed from framelayout
     */
    public static void removeFragments(FragmentActivity fragmentActivity, Fragment[] fragmentList) {
        if (fragmentList == null) return;
        for (int i = 0; i < fragmentList.length; ++i) {
            removeFragment(fragmentActivity, fragmentList[i]);
        }
    }
}
