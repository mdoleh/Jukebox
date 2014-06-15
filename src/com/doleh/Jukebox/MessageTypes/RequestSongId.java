package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.Fragments.PlayerFragment;
import com.doleh.Jukebox.*;

import java.io.Serializable;
import java.util.List;

public class RequestSongId extends ClientMessage implements Serializable
{
    public Song requestedSong;

    public RequestSongId(Song _requestedSong)
    {
        requestedSong = _requestedSong;
    }

    @Override
    public List<Song> Execute(Server server)
    {
        final PlayerFragment playerFragment = server.playerFragment;
        MainActivity mainActivity = server.mainActivity;
        MediaLibraryHelper.playRequest(requestedSong, mainActivity.getApplicationContext(), playerFragment.mediaPlayer, mainActivity.getContentResolver(), server);
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                playerFragment.enableAllElements();
            }
        });
        return null;
    }
}
