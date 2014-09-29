package com.doleh.Jukebox.Static.Factories;

import com.doleh.Jukebox.Fragments.StartupFragment;
import com.doleh.Jukebox.Interfaces.IStartupView;

public class StartupViewFactory
{
    public static IStartupView createStartupView()
    {
        return new StartupFragment();
    }
}
