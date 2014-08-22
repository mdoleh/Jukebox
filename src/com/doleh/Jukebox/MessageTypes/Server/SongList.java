package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.MessageDialog;
import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.Fragments.SongSearchFragment;
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
    public void Execute()
    {
        NetworkClient.receivedSongs = songs;
        if (songs.size() > 0)
        {
            ((SongSearchFragment) NetworkClient.mainActivity.getFragmentManager().findFragmentByTag("song_search")).showSongRequest();
        }
        else
        {
            MessageDialog.showMessageBox(NetworkClient.mainActivity, NetworkClient.mainActivity.getString(R.string.noSongsFound), NetworkClient.mainActivity.getString(R.string.noSongsFoundMsg));
        }
        NetworkClient.songSearchFragment.unBlockUI();
    }
}
