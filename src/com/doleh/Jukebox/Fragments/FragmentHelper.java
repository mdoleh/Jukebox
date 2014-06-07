package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import com.doleh.Jukebox.R;

public class FragmentHelper
{
    public static void showFragment(String currentFragmentTag, Fragment currentFragment, String newFragmentTag, Fragment newFragment, FragmentManager fragmentManager)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (currentFragment != null) { transaction.remove(currentFragment); }
        transaction.add(R.id.main, newFragment, newFragmentTag);
        if (currentFragmentTag != null) { transaction.addToBackStack(currentFragmentTag); }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    public static void addFragment(String fragmentTag, Fragment fragment, FragmentManager fragmentManager)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main, fragment, fragmentTag);
        transaction.hide(fragment);
        transaction.commit();
    }

    public static void unHideFragment(Fragment fragment, FragmentManager fragmentManager, int enter, int exit, int popEnter, int popExit)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
        transaction.show(fragment);
        transaction.commit();
    }

    public static void hideFragment(String fragmentTag, Fragment fragment, FragmentManager fragmentManager, int enter, int exit, int popEnter, int popExit)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentTag != null) { transaction.addToBackStack(fragmentTag); }
        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
        transaction.hide(fragment);
        transaction.commit();
    }

    public static void goBackToBeginning(FragmentManager fragmentManager)
    {
        int size = fragmentManager.getBackStackEntryCount();
        for (int ii = 0; ii < size - 1; ++ii)
        {
            fragmentManager.popBackStack();
        }
    }
}
