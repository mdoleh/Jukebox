package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Client;
import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class ConnectionClosed extends ServerMessage implements Serializable
{
    @Override
    public void Execute()
    {
        Client.mainActivity.showMessageBox(Client.mainActivity.getString(R.string.connectionLost), Client.mainActivity.getString(R.string.connectionLostMsg));
        FragmentHelper.goBackToBeginning(Client.mainActivity.getFragmentManager());
    }
}
