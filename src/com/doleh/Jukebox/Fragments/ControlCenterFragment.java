package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.doleh.Jukebox.*;

public class ControlCenterFragment extends Fragment
{
    private MainActivity mainActivity;
    private View view;
    public Server server;
    private PlayerFragment playerFragment;
    private RequestListFragment requestListFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.control_center, container, false);
        mainActivity = (MainActivity)getActivity();
        playerFragment = new PlayerFragment(this);
        requestListFragment = new RequestListFragment();
        FragmentHelper.addFragment("music_player", playerFragment, getFragmentManager());
        FragmentHelper.addFragment("request_list", requestListFragment, getFragmentManager());
        server = new Server(mainActivity, this, playerFragment);

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
        final Button listenButton = ((Button)view.findViewById(R.id.listenerToggle));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleListener();
            }
        });

        final Button playerButton = ((Button)view.findViewById(R.id.viewPlayerButton));
        playerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                showMusicPlayer();
            }
        });

        final Button requestsButton = ((Button)view.findViewById(R.id.viewRequestListButton));
        requestsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                showRequestList();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        if (!hidden)
        {
            hideFragment(playerFragment, getFragmentManager());
            hideFragment(requestListFragment, getFragmentManager());
        }
    }

    private void hideFragment(Fragment fragment, FragmentManager fragmentManager)
    {
        if (!fragment.isHidden())
        {
            FragmentHelper.hideFragment(null, fragment, fragmentManager);
        }
    }

    private void showMusicPlayer()
    {
        FragmentHelper.hideFragment("control_center", this, getFragmentManager());
        FragmentHelper.unHideFragment(playerFragment, getFragmentManager());
    }

    private void showRequestList()
    {
        FragmentHelper.hideFragment("control_center", this, getFragmentManager());
        FragmentHelper.unHideFragment(requestListFragment, getFragmentManager());
    }

    private void toggleListener()
    {
        server.toggleListener();
        final TextView ipAddress = (TextView)view.findViewById(R.id.deviceAddress);
        ipAddress.setText(Utils.getIPAddress(true));
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
}
