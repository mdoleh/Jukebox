package com.doleh.Jukebox.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.doleh.Jukebox.*;

import java.io.IOException;

public class ConnectFragment extends Fragment
{
    private View view;
    private Activity mainActivity;
    private NetworkClient networkClient = new NetworkClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.connect, container, false);
        FragmentHelper.loadBannerAds(view);
        mainActivity = getActivity();
        NetworkClient.connectFragment = this;
        setupButtonEventListener();
        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        closeSocket();
    }

    private void closeSocket()
    {
        try
        {
            if (NetworkClient.socket != null && !NetworkClient.socket.isClosed())
            {
                NetworkClient.socket.close();
            }
        }
        catch (IOException ex)
        {
            Email.sendErrorReport(ex);
        }
    }

    private void setupButtonEventListener()
    {
        final Button connectButton = (Button)view.findViewById(R.id.connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Utils.isOnWifi(mainActivity))
                {
                    connectDevices();
                    blockUI(connectButton);
                }
                else
                {
                    MessageDialog.showMessageBox(mainActivity, getString(R.string.notOnWifi), getString(R.string.notOnWifiMsgConnect));
                }
            }});
    }

    private void connectDevices()
    {
        final EditText ipAddress = (EditText)view.findViewById(R.id.ipAddress);
        NetworkClient.ip = ipAddress.getText().toString();
        NetworkClient.mainActivity = mainActivity;
        networkClient.startNetworkThread();
    }

    public void showSongSearch()
    {
        FragmentHelper.showFragment("connect", this, "song_search", new SongSearchFragment(), getFragmentManager());
    }

    private void blockUI(Button connectButton)
    {
        FragmentHelper.blockUI(mainActivity, connectButton, view);
    }

    public void unBlockUI()
    {
        final Button connectButton = (Button)view.findViewById(R.id.connect);
        FragmentHelper.unBlockUI(mainActivity, connectButton, view);
    }
}
