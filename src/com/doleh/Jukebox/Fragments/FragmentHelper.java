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
        transaction.remove(currentFragment);
        transaction.add(R.id.main, newFragment, newFragmentTag);
        transaction.addToBackStack(currentFragmentTag);
        transaction.commit();
    }
}
