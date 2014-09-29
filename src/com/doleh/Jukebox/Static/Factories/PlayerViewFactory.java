package com.doleh.Jukebox.Static.Factories;

import com.doleh.Jukebox.Fragments.PlayerFragment;
import com.doleh.Jukebox.Interfaces.INetworkServer;
import com.doleh.Jukebox.Interfaces.IPlayerView;

public class PlayerViewFactory
{
    public static IPlayerView createPlayerView(INetworkServer networkServer)
    {
        return new PlayerFragment(networkServer);
    }
}
