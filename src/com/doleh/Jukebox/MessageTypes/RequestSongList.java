package com.doleh.Jukebox.MessageTypes;

import android.media.MediaPlayer;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.Song;

import java.io.Serializable;
import java.util.List;

public class RequestSongList extends Message implements Serializable
{
    public String title;
    public String artist;

    public RequestSongList(String _title, String _artist)
    {
        title = _title;
        artist = _artist;
    }

    @Override
    public List<Song> Execute(MainActivity mainActivity, MediaPlayer mediaPlayer)
    {
        return MediaLibraryHelper.getSongList(mainActivity.getContentResolver(), title, artist);
    }
}
