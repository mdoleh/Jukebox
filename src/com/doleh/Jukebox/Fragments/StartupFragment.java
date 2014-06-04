package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.doleh.Jukebox.R;

public class StartupFragment extends Fragment
{
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.startup, container, false);
        setupButtonEventListener();
        hideActionBar();
        return view;
    }

    private void setupButtonEventListener()
    {
        final Button listenButton = ((Button)view.findViewById(R.id.receive_requests));
        listenButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                showControlCenter();
            }
        });

        final Button sendRequestButton = (Button)view.findViewById(R.id.send_a_request);
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showConnectDevices();
            }});
    }

    private void hideActionBar()
    {
        getActivity().getActionBar().hide();
    }

    private void showControlCenter()
    {
        FragmentHelper.showFragment("startup", this, "control_center", new ControlCenterFragment(), getFragmentManager());
    }

    private void showConnectDevices()
    {
        FragmentHelper.showFragment("startup", this, "connect", new ConnectFragment(), getFragmentManager());
    }
}
