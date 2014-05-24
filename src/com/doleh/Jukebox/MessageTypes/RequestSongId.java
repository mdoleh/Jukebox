package com.doleh.Jukebox.MessageTypes;

import android.media.MediaPlayer;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.Song;

import java.io.Serializable;
import java.util.List;

public class RequestSongId extends Message implements Serializable
{
    public Long id;

    public RequestSongId(long _id)
    {
        id = _id;
    }

    @Override
    public List<Song> Execute(MainActivity mainActivity, MediaPlayer mediaPlayer)
    {
        MediaLibraryHelper.playSong(id, mainActivity.getApplicationContext(), mediaPlayer);
        return null;
    }
}
