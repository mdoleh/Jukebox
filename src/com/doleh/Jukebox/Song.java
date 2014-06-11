package com.doleh.Jukebox;

import java.io.Serializable;

public class Song implements Serializable
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
}
