package com.doleh.Jukebox;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter
{

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // WiFi Direct fragment activity
                return new WiFiDirectFragment();
            case 1:
                // Song Request fragment activity
                return new SongRequestFragment();
            case 2:
                // Music Control Center fragment activity
                return new ControlCenterFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
