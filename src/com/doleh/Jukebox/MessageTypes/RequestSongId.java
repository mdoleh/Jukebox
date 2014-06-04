package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.Fragments.ControlCenterFragment;
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
    public List<Song> Execute(ControlCenterFragment controlCenterFragment)
    {
        final ControlCenterFragment fragment = controlCenterFragment;
        MainActivity mainActivity = (MainActivity)controlCenterFragment.getActivity();
        MediaLibraryHelper.playRequest(id, mainActivity.getApplicationContext(), controlCenterFragment.mediaPlayer);
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
