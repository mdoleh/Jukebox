package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.doleh.Jukebox.*;

public class ControlCenterFragment extends Fragment
{
    public MediaPlayer mediaPlayer = new MediaPlayer();
    private MainActivity mainActivity;
    private boolean mediaPlayerReady = false;
    private View view;
    private Server server;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.control_center, container, false);
        mainActivity = (MainActivity)getActivity();
        server = new Server(mainActivity, this);
        setupButtonEventListener();
        setupMediaPlayerListeners();

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        server.closePort();
        mediaPlayer.stop();
        mediaPlayer.release();
        server.clearMessageCounts();
        MediaLibraryHelper.clearSongQueue();
    }

    private void setupButtonEventListener()
    {
        final Button pauseButton = (Button)view.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = MediaLibraryHelper.togglePlay(mediaPlayer, getActivity());
                if (text != null) { pauseButton.setText(text); }
            }});

        final Button listenButton = ((Button)view.findViewById(R.id.listenerToggle));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleListener();
            }
        });
    }

    public void disableElement(int elementId)
    {
        final View element = view.findViewById(elementId);
        element.setEnabled(false);
    }

    public void enableElement(int elementId)
    {
        final View element = view.findViewById(elementId);
        element.setEnabled(true);
    }

    private void setupMediaPlayerListeners()
    {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                mediaPlayerReady = false;
                mp.stop();
                mp.reset();
                // If songs are in the queue play them next
                MediaLibraryHelper.playNextSongInQueue(mp, mainActivity.getApplicationContext());
                if (!mediaPlayerReady) { disableElement(R.id.pauseButton); }
                server.clearMessageCounts();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mediaPlayerReady = true;
                enableElement(R.id.pauseButton);
            }
        });
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
