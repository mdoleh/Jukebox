package com.doleh.Jukebox.Static.Factories;

import com.doleh.Jukebox.Fragments.SongSearchFragment;
import com.doleh.Jukebox.Interfaces.ISongSearchView;

public class SongSearchViewFactory
{
    public static ISongSearchView createSongSearchView()
    {
        return new SongSearchFragment();
    }
}
