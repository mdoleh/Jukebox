package com.doleh.Jukebox.Static.Factories;

import com.doleh.Jukebox.Fragments.ConfigFragment;
import com.doleh.Jukebox.Interfaces.IConfigView;
import com.doleh.Jukebox.Interfaces.INetworkServer;

public class ConfigViewFactory
{
    public static IConfigView createConfigView(INetworkServer networkServer)
    {
        return new ConfigFragment(networkServer);
    }
}
