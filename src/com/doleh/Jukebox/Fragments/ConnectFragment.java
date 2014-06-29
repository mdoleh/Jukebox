package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.doleh.Jukebox.Client;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;

import java.io.IOException;

public class ConnectFragment extends Fragment
{
    private View view;
    private MainActivity mainActivity;
    private Client client = new Client();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.connect, container, false);
        mainActivity = (MainActivity)getActivity();
        Client.connectFragment = this;
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
            if (Client.socket != null && !Client.socket.isClosed())
            {
                Client.socket.close();
            }
        }
        catch (IOException ex)
        {
            MainActivity.sendErrorReport(ex);
        }
    }

    private void setupButtonEventListener()
    {
        final Button connectButton = (Button)view.findViewById(R.id.connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectDevices();
                blockUI(connectButton);
            }});
    }

    private void connectDevices()
    {
        final EditText ipAddress = (EditText)view.findViewById(R.id.ipAddress);
        Client.ip = ipAddress.getText().toString();
        Client.mainActivity = mainActivity;
        client.startNetworkThread();
    }

    public void showSongSearch()
    {
        FragmentHelper.showFragment("connect", this, "song_search", new SongSearchFragment(), getFragmentManager());
    }

    private void blockUI(final Button connectButton)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final ImageView blocker = (ImageView)view.findViewById(R.id.loadingImage);
                FragmentHelper.blockUI(connectButton, blocker);
            }
        });
    }

    public void unBlockUI()
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final Button connectButton = (Button)view.findViewById(R.id.connect);
                final ImageView blocker = (ImageView)view.findViewById(R.id.loadingImage);
                FragmentHelper.unBlockUI(connectButton, blocker);
            }
        });
    }
}
