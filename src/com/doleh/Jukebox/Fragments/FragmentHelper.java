package com.doleh.Jukebox.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.doleh.Jukebox.AnimationSetting;
import com.doleh.Jukebox.IFunction;
import com.doleh.Jukebox.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FragmentHelper
{
    public static boolean shouldShowAds = true;

    public static void loadBannerAds(View view)
    {
        if (shouldShowAds) {
            // Look up the AdView as a resource and load a request.
            AdView adView = (AdView)view.findViewById(R.id.bottomBanner);
            AdRequest adRequest = new AdRequest.Builder()
    //                .addTestDevice("05EB5BB05700B41A96F9FAA7933F2832")
    //                .addTestDevice("A6A88EF525F683BB326EECB11E4504D6")
                    .build();
            adView.loadAd(adRequest);
        }
    }

    public static void handleTouchEvents(MotionEvent event, ImageView button, IFunction touchHandler)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:{
                button.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                touchHandler.execute(button);
                button.clearColorFilter();
                break;
            }
        }
    }

    public static void showFragment(String currentFragmentTag, Fragment currentFragment, String newFragmentTag, Fragment newFragment, FragmentManager fragmentManager)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_up, R.animator.slide_in_down, R.animator.slide_out_down);
        transaction.remove(currentFragment);
        transaction.add(R.id.main, newFragment, newFragmentTag);
        transaction.addToBackStack(currentFragmentTag);
        transaction.commit();
    }

    public static void showFragment(String newFragmentTag, Fragment newFragment, FragmentManager fragmentManager)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(R.id.main, newFragment, newFragmentTag);
        transaction.commit();
    }

    public static void addFragment(String fragmentTag, Fragment fragment, FragmentManager fragmentManager)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main, fragment, fragmentTag);
        transaction.hide(fragment);
        transaction.commit();
    }

    public static void unHideFragment(Fragment fragment, FragmentManager fragmentManager, AnimationSetting animationSetting)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(animationSetting.enter, animationSetting.exit, animationSetting.popEnter, animationSetting.popExit);
        transaction.show(fragment);
        transaction.commit();
    }

    public static void hideFragment(String fragmentTag, Fragment fragment, FragmentManager fragmentManager, AnimationSetting animationSetting)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentTag != null) { transaction.addToBackStack(fragmentTag); }
        transaction.setCustomAnimations(animationSetting.enter, animationSetting.exit, animationSetting.popEnter, animationSetting.popExit);
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

    public static void blockUI(Activity mainActivity, final Button button, final View view)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final ImageView blocker = (ImageView)view.findViewById(R.id.loadingImage);
                button.setEnabled(false);
                showLoadingImage(blocker);
            }
        });
    }

    private static void showLoadingImage(ImageView blocker)
    {
        blocker.setBackgroundResource(R.drawable.loading_animation);

        AnimationDrawable frameAnimation = (AnimationDrawable) blocker.getBackground();
        blocker.setVisibility(View.VISIBLE);
        frameAnimation.start();
    }

    public static void unBlockUI(Activity mainActivity, final Button button, final View view)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                button.setEnabled(true);
                final ImageView blocker = (ImageView)view.findViewById(R.id.loadingImage);
                hideLoadingImage(blocker);
            }
        });
    }

    private static void hideLoadingImage(ImageView blocker)
    {
        blocker.setVisibility(View.INVISIBLE);
        AnimationDrawable frameAnimation = (AnimationDrawable) blocker.getBackground();
        frameAnimation.stop();
    }
}
