package com.doleh.Jukebox.Static.Factories;

import com.doleh.Jukebox.Fragments.ConnectFragment;
import com.doleh.Jukebox.Interfaces.IConnectView;

public class ConnectViewFactory
{
    public static IConnectView createConnectView()
    {
        return new ConnectFragment();
    }
}
