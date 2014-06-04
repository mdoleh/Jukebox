package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;

public class ConnectFragment extends Fragment
{
    private View view;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.connect, container, false);
        mainActivity = (MainActivity)getActivity();
        setupButtonEventListener();
        return view;
    }

    private void setupButtonEventListener()
    {
        final Button connectButton = (Button)view.findViewById(R.id.connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectDevices();
            }});
    }

    private void connectDevices()
    {
        final EditText ipAddress = (EditText)view.findViewById(R.id.ipAddress);
        mainActivity.ip = ipAddress.getText().toString();
        mainActivity.startNetworkThread();
    }

    public void showSongSearch()
    {
        FragmentHelper.showFragment("connect", this, "song_search", new SongSearchFragment(), getFragmentManager());
    }
}
