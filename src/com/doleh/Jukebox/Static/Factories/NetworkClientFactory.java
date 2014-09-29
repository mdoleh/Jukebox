package com.doleh.Jukebox.Static.Factories;

import com.doleh.Jukebox.Interfaces.INetworkClient;
import com.doleh.Jukebox.NetworkClient;

public class NetworkClientFactory
{
    public static INetworkClient createNetworkClient()
    {
        return new NetworkClient();
    }
}
