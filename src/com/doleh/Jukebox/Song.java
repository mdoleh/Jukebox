package com.doleh.Jukebox;

import java.io.Serializable;

public class Song implements Serializable, Comparable<Song>
{
    public Long id;
    public String title;
    public String artist;

    public Song() {}

    public Song(Long ID, String Title, String Artist)
    {
        id = ID;
        title = Title;
        artist = Artist;
    }

    @Override
    public int compareTo(Song another)
    {
        return title.compareToIgnoreCase(another.title);
    }
}
