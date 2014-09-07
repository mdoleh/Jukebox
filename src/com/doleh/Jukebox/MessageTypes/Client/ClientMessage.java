package com.doleh.Jukebox.MessageTypes.Client;

import com.doleh.Jukebox.NetworkServer;
import com.jackieloven.thebasics.NetComm;

public abstract class ClientMessage
{
    public abstract void Execute(NetworkServer networkServer, NetComm sender);
}
