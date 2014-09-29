package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.Static.MessageDialog;
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
        SongSearchFragment songSearchFragment = FragmentHelper.getFragment(SongSearchFragment.class, NetworkClient.mainActivity.getFragmentManager(), FragmentHelper.SONG_SEARCH);
        if (songs.size() > 0)
        {
            songSearchFragment.showSongRequest();
        }
        else
        {
            MessageDialog.showMessageBox(NetworkClient.mainActivity, NetworkClient.mainActivity.getString(R.string.noSongsFound), NetworkClient.mainActivity.getString(R.string.noSongsFoundMsg));
        }
        songSearchFragment.unBlockUI();
    }
}
