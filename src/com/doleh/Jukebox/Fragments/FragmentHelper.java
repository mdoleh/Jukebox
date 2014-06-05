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

    public static void goBackToBeginning(FragmentManager fragmentManager)
    {
        int size = fragmentManager.getBackStackEntryCount();
        for (int ii = 0; ii < size - 1; ++ii)
        {
            fragmentManager.popBackStack();
        }
    }
}
