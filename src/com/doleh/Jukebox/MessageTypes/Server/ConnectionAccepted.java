package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Client;
import com.doleh.Jukebox.Fragments.ConnectFragment;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class ConnectionAccepted extends ServerMessage implements Serializable
{
    @Override
    public void Execute()
    {
        Client.mainActivity.showMessageBox(Client.mainActivity.getString(R.string.connectionSuccess), Client.mainActivity.getString(R.string.connectionSuccessMsg));
        ((ConnectFragment)Client.mainActivity.getFragmentManager().findFragmentByTag("connect")).showSongSearch();
        Client.connectFragment.unBlockUI();
    }
}
