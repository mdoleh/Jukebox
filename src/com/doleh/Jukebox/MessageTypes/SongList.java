package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.Song;

import java.io.Serializable;
import java.util.List;

public class SongList extends Message implements Serializable
{
    public List<Song> songs;

    public SongList(List<Song> _songs)
    {
        songs = _songs;
    }
}
