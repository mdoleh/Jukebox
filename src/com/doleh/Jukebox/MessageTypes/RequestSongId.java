package com.doleh.Jukebox.MessageTypes;

import android.media.MediaPlayer;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Song;

import java.io.Serializable;
import java.util.List;

public class RequestSongId extends ClientMessage implements Serializable
{
    public Long id;

    public RequestSongId(long _id)
    {
        id = _id;
    }

    @Override
    public List<Song> Execute(MainActivity mainActivity, MediaPlayer mediaPlayer)
    {
        final MainActivity activity = mainActivity;
        MediaLibraryHelper.playSong(id, mainActivity.getApplicationContext(), mediaPlayer);
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                activity.getFragmentManager().findFragmentByTag("control_center").getView().findViewById(R.id.pauseButton).setEnabled(true);
            }
        });
        return null;
    }
}
