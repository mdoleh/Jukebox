package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class ConnectionClosed extends ServerMessage implements Serializable
{
    @Override
    public void Execute()
    {
        NetworkClient.mainActivity.showMessageBox(NetworkClient.mainActivity.getString(R.string.connectionLost), NetworkClient.mainActivity.getString(R.string.connectionLostMsg));
        FragmentHelper.goBackToBeginning(NetworkClient.mainActivity.getFragmentManager());
    }
}
