package com.doleh.Jukebox;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class ControlCenterFragment extends Fragment
{
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MainActivity mainActivity;
    private boolean isRequestListener = false;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.control_center, container, false);
        mainActivity = (MainActivity)getActivity();
        setupButtonEventListener();
        setupOnCompletionListener();
        return view;
    }

    private void setupButtonEventListener()
    {
        final Button listenButton = ((Button)view.findViewById(R.id.listenerToggle));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleListener();
            }
        });

        final Button stopButton = (Button)view.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MediaLibraryHelper.stopSong(mediaPlayer);
            }});
    }

    private void setupOnCompletionListener()
    {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                MediaLibraryHelper.stopSong(mp);
                // If songs are in the queue play them next
                if (!MediaLibraryHelper.songQueueIsEmpty())
                {
                    MediaLibraryHelper.playSong(MediaLibraryHelper.getSongId(0), mainActivity.getApplicationContext(), mp);
                    MediaLibraryHelper.removeSong(0);
                }
            }
        });
    }

    private void toggleListener()
    {
        isRequestListener = !isRequestListener;
        if (isRequestListener)
        {
            mainActivity.showMessageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOn));
        }
        else
        {
            mainActivity.showMessageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOff));
        }
    }

    private List<Song> checkSongExists(String songTitle, String songArtist)
    {
        // Check if requested song exists
        return MediaLibraryHelper.getSongList(mainActivity.getContentResolver(), songTitle, songArtist);
    }
}
