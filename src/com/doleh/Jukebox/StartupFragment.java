package com.doleh.Jukebox;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
                showSongSearch();
            }});
    }

    private void hideActionBar()
    {
        getActivity().getActionBar().hide();
    }

    private void showControlCenter()
    {
        Fragment fragment = new ControlCenterFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.add(R.id.main, fragment, "control_center");
        transaction.addToBackStack("startup");
        transaction.commit();
    }

    private void showSongSearch()
    {
        Fragment fragment = new SongSearchFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.add(R.id.main, fragment, "song_search");
        transaction.addToBackStack("startup");
        transaction.commit();
    }
}
