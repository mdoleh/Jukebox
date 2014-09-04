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

public class ControlCenterFragment extends Fragment
{
    private Activity mainActivity;
    private View view;
    private Server server;
    private PlayerFragment playerFragment;
    private RequestListFragment requestListFragment;
    private boolean listenImageToggle = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.control_center, container, false);
        FragmentHelper.loadBannerAds(view);
        mainActivity = getActivity();
        server = new Server(mainActivity);
        playerFragment = new PlayerFragment(server);
        requestListFragment = new RequestListFragment();
        FragmentHelper.addFragment(FragmentHelper.MUSIC_PLAYER, playerFragment, getFragmentManager());
        FragmentHelper.addFragment(FragmentHelper.REQUEST_LIST, requestListFragment, getFragmentManager());

        setupButtonEventListener();

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        server.closePort();
        server.clearMessageCounts();
        playerFragment.onDestroy();
        requestListFragment.onDestroy();
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
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        if (!hidden)
        {
            hideFragment(playerFragment, getFragmentManager(), R.animator.slide_out_left);
            hideFragment(requestListFragment, getFragmentManager(), R.animator.slide_out_right);
        }
    }

    private void hideFragment(Fragment fragment, FragmentManager fragmentManager, int exit)
    {
        if (!fragment.isHidden())
        {
            AnimationSetting animationSetting = new AnimationSetting(0, exit, 0, 0);
            FragmentHelper.hideFragment(null, fragment, fragmentManager, animationSetting);
        }
    }

    private void showMusicPlayer()
    {
        AnimationSetting controlCenterAnimation = new AnimationSetting(0, R.animator.slide_out_right, R.animator.slide_in_left, 0);
        FragmentHelper.hideFragment("control_center", this, getFragmentManager(), controlCenterAnimation);

        AnimationSetting playerAnimation = new AnimationSetting(R.animator.slide_in_right, 0, 0, 0);
        FragmentHelper.unHideFragment(playerFragment, getFragmentManager(), playerAnimation);
    }

    private void showRequestList()
    {
        AnimationSetting controlCenterAnimation = new AnimationSetting(0, R.animator.slide_out_left, R.animator.slide_in_right, 0);
        FragmentHelper.hideFragment("control_center", this, getFragmentManager(), controlCenterAnimation);

        AnimationSetting requestListAnimation = new AnimationSetting(R.animator.slide_in_left, 0, 0, 0);
        FragmentHelper.unHideFragment(requestListFragment, getFragmentManager(), requestListAnimation);
    }

    private void toggleListener()
    {
        if (Utils.isOnWifi(mainActivity))
        {
            server.toggleListener();
            final TextView ipAddress = (TextView)view.findViewById(R.id.deviceAddress);
            ipAddress.setText(Utils.getIPAddress(true));

            swapImages();
        }
        else
        {
            MessageDialog.showMessageBox(mainActivity, getString(R.string.notOnWifi), getString(R.string.notOnWifiMsgListen));
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

    public class handleListenerToggleTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            toggleListener();
        }
    }

    public class handlePlayerTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            showMusicPlayer();
        }
    }

    public class handleRequestsTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
           showRequestList();
        }
    }
}
