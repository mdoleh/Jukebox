package com.doleh.Jukebox;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter
{
//    private List<Fragment> pageReference = new ArrayList<Fragment>();

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
//                if (isAlreadyInList(WiFiDirectFragment.class.toString()))
//                {
//                    pageReference.remove(index);
//                }
                // WiFi Direct fragment activity
                WiFiDirectFragment wiFiDirectFragment = new WiFiDirectFragment();
//                pageReference.add(index, wiFiDirectFragment);
                return wiFiDirectFragment;
            case 1:
//                if (isAlreadyInList(SongRequestFragment.class.toString()))
//                {
//                    pageReference.remove(index);
//                }
                // Song Request fragment activity
                SongRequestFragment songRequestFragment = new SongRequestFragment();
//                pageReference.add(index, songRequestFragment);
                return songRequestFragment;
            case 2:
//                if (isAlreadyInList(ControlCenterFragment.class.toString()))
//                {
//                    pageReference.remove(index);
//                }
                // Music Control Center fragment activity
                ControlCenterFragment controlCenterFragment = new ControlCenterFragment();
//                pageReference.add(2, controlCenterFragment);
                return controlCenterFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

//    private boolean isAlreadyInList(String type)
//    {
//        for (Fragment fragment : pageReference)
//        {
//            if (fragment.getClass().toString().equals(type))
//            {
//                return true;
//            }
//        }
//        return false;
//    }
}
