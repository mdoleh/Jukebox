package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.Fragments.SongSearchFragment;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.Song;

import java.io.Serializable;
import java.util.List;

public class SongList extends ServerMessage implements Serializable
{
    public List<Song> songs;

    public SongList(List<Song> _songs)
    {
        songs = _songs;
    }

    @Override
    public void Execute(MainActivity mainActivity)
    {
        mainActivity.receivedSongs = songs;
        ((SongSearchFragment)mainActivity.getFragmentManager().findFragmentByTag("song_search")).showSongRequest();
    }
}
