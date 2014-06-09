package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.R;

public class PlayerFragment extends Fragment
{
    private View view;
    public MediaPlayer mediaPlayer = new MediaPlayer();
    private MainActivity mainActivity;
    private boolean mediaPlayerReady = false;
    private ControlCenterFragment controlCenterFragment;

    public PlayerFragment(ControlCenterFragment _controlCenterFragment)
    {
        controlCenterFragment = _controlCenterFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.player, container, false);
        mainActivity = (MainActivity)getActivity();
        setupButtonEventListeners();
        setupMediaPlayerListeners();

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mediaPlayer.release();
    }

    private void setupButtonEventListeners()
    {
        final Button pauseButton = (Button)view.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = MediaLibraryHelper.togglePlay(mediaPlayer, getActivity());
                if (text != null) { pauseButton.setText(text); }
        }});
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
                MediaLibraryHelper.playNextSongInQueue(mp, mainActivity.getApplicationContext(), controlCenterFragment.server);
                if (!mediaPlayerReady) { disableElement(R.id.pauseButton); }
                controlCenterFragment.server.clearMessageCounts();
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
}
