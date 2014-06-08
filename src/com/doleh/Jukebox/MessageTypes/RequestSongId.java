package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.Fragments.PlayerFragment;
import com.doleh.Jukebox.*;

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
    public List<Song> Execute(Server server)
    {
        final PlayerFragment fragment = server.playerFragment;
        MainActivity mainActivity = server.mainActivity;
        MediaLibraryHelper.playRequest(id, mainActivity.getApplicationContext(), fragment.mediaPlayer, mainActivity.getContentResolver());
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                fragment.enableElement(R.id.pauseButton);
            }
        });
        return null;
    }
}
