package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.Fragments.ControlCenterFragment;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.Song;

import java.io.Serializable;
import java.util.List;

public class RequestSongList extends ClientMessage implements Serializable
{
    public String title;
    public String artist;

    public RequestSongList(String _title, String _artist)
    {
        title = _title;
        artist = _artist;
    }

    @Override
    public List<Song> Execute(ControlCenterFragment controlCenterFragment)
    {
        return MediaLibraryHelper.getSongList(controlCenterFragment.getActivity().getContentResolver(), title, artist);
    }
}
