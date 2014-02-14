package com.doleh.Jukebox;

import android.app.Fragment;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DeviceConnectFragment extends Fragment
{
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.device_connect, container, false);
        showActionBar();
        addIntentsToFilter();
        setupConnectionVariables();
        return view;
    }

    private void showActionBar()
    {
        getActivity().getActionBar().show();
    }

    private void addIntentsToFilter()
    {
        ((MainActivity)getActivity()).addIntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        ((MainActivity)getActivity()).addIntentFilter(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        ((MainActivity)getActivity()).addIntentFilter(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        ((MainActivity)getActivity()).addIntentFilter(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
    }

    private void setupConnectionVariables()
    {
        ((MainActivity)getActivity()).setupConnectionVariables();
    }
}
