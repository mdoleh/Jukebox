package com.doleh.Jukebox.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.doleh.Jukebox.*;
import com.doleh.Jukebox.Interfaces.*;
import com.doleh.Jukebox.Static.Config;
import com.doleh.Jukebox.Static.Factories.ConfigViewFactory;
import com.doleh.Jukebox.Static.Factories.NetworkServerFactory;
import com.doleh.Jukebox.Static.Factories.PlayerViewFactory;
import com.doleh.Jukebox.Static.Factories.RequestListViewFactory;
import com.doleh.Jukebox.Static.MessageDialog;
import com.doleh.Jukebox.Static.Utils;

public class ControlCenterFragment extends Fragment implements IControlCenterView
{
    private Activity mainActivity;
    private View view;
    private INetworkServer networkServer;
    private IPlayerView playerFragment;
    private IRequestListView requestListFragment;
    private IConfigView configFragment;
    private boolean listenImageToggle = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.control_center, container, false);
        FragmentHelper.loadBannerAds(view);
        mainActivity = getActivity();
        networkServer = NetworkServerFactory.createNetworkServer(mainActivity);
        loadFragments();

        setupButtonEventListener();

        return view;
    }

    private void loadFragments()
    {
        playerFragment = PlayerViewFactory.createPlayerView(networkServer);
        requestListFragment = RequestListViewFactory.createRequestListView();
        FragmentHelper.addFragment(FragmentHelper.MUSIC_PLAYER, (Fragment)playerFragment, getFragmentManager());
        FragmentHelper.addFragment(FragmentHelper.REQUEST_LIST, (Fragment)requestListFragment, getFragmentManager());
        loadConfigurationFragment();
    }

    private void loadConfigurationFragment()
    {
        // this fragment does not need to loaded immediately, delayed until needed
        if (Config.APP_PAID)
        {
            configFragment = ConfigViewFactory.createConfigView(networkServer);
            FragmentHelper.addFragment(FragmentHelper.CONFIG, (Fragment)configFragment, getFragmentManager());
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        networkServer.closePort();
        networkServer.clearMessageCounts();
        ((Fragment)playerFragment).onDestroy();
        ((Fragment)requestListFragment).onDestroy();
        FragmentHelper.goBackToBeginning(getFragmentManager());
        MediaLibraryHelper.clearSongQueue();
    }

    private void setupButtonEventListener()
    {
        final ImageView listenButton = ((ImageView)view.findViewById(R.id.listenerToggle));
        listenButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, listenButton, new handleListenerToggleTouch());
                return true;
            }
        });

        final ImageView playerButton = ((ImageView)view.findViewById(R.id.viewPlayerButton));
        playerButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, playerButton, new handlePlayerTouch());
                return true;
            }
        });

        final ImageView requestsButton = ((ImageView)view.findViewById(R.id.viewRequestListButton));
        requestsButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, requestsButton, new handleRequestsTouch());
                return true;
            }
        });

        final ImageView configButton = ((ImageView)view.findViewById(R.id.config_icon));
        configButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, configButton, new handleConfigTouch());
                return true;
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        if (!hidden)
        {
            hideFragment((Fragment)playerFragment, getFragmentManager(), R.animator.slide_out_left);
            hideFragment((Fragment)requestListFragment, getFragmentManager(), R.animator.slide_out_right);
            hideFragment((Fragment)configFragment, getFragmentManager(), R.animator.slide_out_down);
        }
    }

    private void hideFragment(Fragment fragment, FragmentManager fragmentManager, int exit)
    {
        if (fragment != null && !fragment.isHidden())
        {
            AnimationSetting animationSetting = new AnimationSetting(0, exit, 0, 0);
            FragmentHelper.hideFragment(null, fragment, fragmentManager, animationSetting);
            removeFragment(fragment, fragmentManager, animationSetting);
        }
    }

    private void removeFragment(Fragment fragment, FragmentManager fragmentManager, AnimationSetting animationSetting)
    {
        // ConfigFragment can be reloaded every time it is needed, removed when leaving screen
        if (fragment.getClass() == ConfigFragment.class)
        {
            FragmentHelper.removeFragment(fragment, fragmentManager, animationSetting);
        }
    }

    private void showMusicPlayer()
    {
        AnimationSetting controlCenterAnimation = new AnimationSetting(0, R.animator.slide_out_right, R.animator.slide_in_left, 0);
        FragmentHelper.hideFragment(FragmentHelper.CONTROL_CENTER, this, getFragmentManager(), controlCenterAnimation);

        AnimationSetting playerAnimation = new AnimationSetting(R.animator.slide_in_right, 0, 0, 0);
        FragmentHelper.unHideFragment((Fragment)playerFragment, getFragmentManager(), playerAnimation);
    }

    private void showRequestList()
    {
        AnimationSetting controlCenterAnimation = new AnimationSetting(0, R.animator.slide_out_left, R.animator.slide_in_right, 0);
        FragmentHelper.hideFragment(FragmentHelper.CONTROL_CENTER, this, getFragmentManager(), controlCenterAnimation);

        AnimationSetting requestListAnimation = new AnimationSetting(R.animator.slide_in_left, 0, 0, 0);
        FragmentHelper.unHideFragment((Fragment)requestListFragment, getFragmentManager(), requestListAnimation);
    }

    private void showConfigurations()
    {
        if (Config.APP_PAID)
        {
            AnimationSetting animationSetting = new AnimationSetting(0, R.animator.slide_out_up, R.animator.slide_in_down, 0);
            FragmentHelper.hideFragment(FragmentHelper.CONTROL_CENTER, this, getFragmentManager(), animationSetting);

            loadConfigurationFragment();
            AnimationSetting configAnimation = new AnimationSetting(R.animator.slide_in_up, 0, 0, 0);
            FragmentHelper.unHideFragment((Fragment)configFragment, getFragmentManager(), configAnimation);
        }
        else
        {
            MessageDialog.showMessageBox(mainActivity, getString(R.string.notPaidApp), getString(R.string.notPaidAppMsg), null);
        }
    }

    private void toggleListener()
    {
        if (Utils.isOnWifi(mainActivity))
        {
            networkServer.toggleListener();
            final TextView ipAddress = (TextView)view.findViewById(R.id.deviceAddress);
            ipAddress.setText(Utils.getIPAddress(true));

            swapImages();
        }
        else
        {
            MessageDialog.showMessageBox(mainActivity, getString(R.string.notOnWifi), getString(R.string.notOnWifiMsgListen), null);
        }
    }

    private void swapImages()
    {
        ImageView button = (ImageView)view.findViewById(R.id.listenerToggle);
        if (listenImageToggle)
        {
            button.setImageResource(R.drawable.stop_listening_icon);
            listenImageToggle = false;
        }
        else
        {
            button.setImageResource(R.drawable.listen_icon);
            listenImageToggle = true;
        }
    }

    public void updateRequesterCount(final int newValue)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final TextView requesterCount = (TextView) view.findViewById(R.id.requesterCount);
                requesterCount.setText(Integer.toString(newValue));
            }
        });
    }

    private class handleListenerToggleTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            toggleListener();
        }
    }

    private class handlePlayerTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            showMusicPlayer();
        }
    }

    private class handleRequestsTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
           showRequestList();
        }
    }

    private class handleConfigTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            showConfigurations();
        }
    }
}
