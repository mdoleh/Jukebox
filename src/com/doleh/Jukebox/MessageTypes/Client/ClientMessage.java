package com.doleh.Jukebox.MessageTypes.Client;

import com.doleh.Jukebox.Server;
import com.jackieloven.thebasics.NetComm;

public abstract class ClientMessage
{
    public abstract void Execute(Server server, NetComm sender);
}
