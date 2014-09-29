package com.doleh.Jukebox.Static.Factories;

import com.doleh.Jukebox.Fragments.SongRequestFragment;
import com.doleh.Jukebox.Interfaces.ISongRequestView;

public class SongRequestViewFactory
{
    public static ISongRequestView createSongRequestView()
    {
        return new SongRequestFragment();
    }
}
