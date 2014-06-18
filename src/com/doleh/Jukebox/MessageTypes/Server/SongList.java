package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Client;
import com.doleh.Jukebox.Fragments.SongSearchFragment;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;
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
        Client.receivedSongs = songs;
        if (songs.size() > 0)
        {
            ((SongSearchFragment)mainActivity.getFragmentManager().findFragmentByTag("song_search")).showSongRequest();
        }
        else
        {
            mainActivity.showMessageBox(mainActivity.getString(R.string.noSongsFound), mainActivity.getString(R.string.noSongsFoundMsg));
        }
    }
}
