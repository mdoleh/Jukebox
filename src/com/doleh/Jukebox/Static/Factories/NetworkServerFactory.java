package com.doleh.Jukebox.Static.Factories;

import android.app.Activity;
import com.doleh.Jukebox.Interfaces.INetworkServer;
import com.doleh.Jukebox.NetworkServer;

public class NetworkServerFactory
{
    public static INetworkServer createNetworkServer(Activity activity)
    {
        return new NetworkServer(activity);
    }
}
