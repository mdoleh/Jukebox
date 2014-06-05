package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.Server;
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
    public List<Song> Execute(Server server)
    {
        return MediaLibraryHelper.getSongList(server.mainActivity.getContentResolver(), title, artist);
    }
}
