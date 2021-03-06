package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.doleh.Jukebox.Interfaces.IFunction;
import com.doleh.Jukebox.Interfaces.IStartupView;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Static.Factories.ConnectViewFactory;
import com.doleh.Jukebox.Static.Factories.ControlCenterViewFactory;
import com.doleh.Jukebox.Static.Globals;

public class StartupFragment extends Fragment implements IStartupView
{
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (Globals.IS_LANDSCAPE)
        {
            view = inflater.inflate(R.layout.startup_land, container, false);
        }
        else
        {
            view = inflater.inflate(R.layout.startup, container, false);
        }
        FragmentHelper.loadBannerAds(view);
        setupButtonEventListener();
        hideActionBar();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        Globals.IS_LANDSCAPE = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        FragmentHelper.removeFragment(this, getFragmentManager());
        FragmentHelper.showFragment(FragmentHelper.STARTUP, new StartupFragment(), getFragmentManager());
    }

    private void setupButtonEventListener()
    {
        final ImageView listenButton = ((ImageView)view.findViewById(R.id.receive_requests));
        listenButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, listenButton, new handleListenTouch());
                return true;
            }
        });

        final ImageView sendRequestButton = (ImageView)view.findViewById(R.id.send_a_request);
        sendRequestButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, sendRequestButton, new handleSendTouch());
                return true;
            }
        });
    }

    private void hideActionBar()
    {
        getActivity().getActionBar().hide();
    }

    private class handleListenTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            showControlCenter();
        }
    }

    private void showControlCenter()
    {
        FragmentHelper.showFragment(FragmentHelper.STARTUP, this, FragmentHelper.CONTROL_CENTER, (Fragment)ControlCenterViewFactory.createControlCenterView(), getFragmentManager());
    }

    private class handleSendTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            showConnectDevices();
        }
    }

    private void showConnectDevices()
    {
        FragmentHelper.showFragment(FragmentHelper.STARTUP, this, FragmentHelper.CONNECT, (Fragment)ConnectViewFactory.createConnectView(), getFragmentManager());
    }
}
