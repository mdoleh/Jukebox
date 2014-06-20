package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Client;
import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class Rejection extends ServerMessage implements Serializable
{
    private boolean closeNetComm;

    public Rejection()
    {
        closeNetComm = true;
    }

    public Rejection(boolean _closeNetComm)
    {
        closeNetComm = _closeNetComm;
    }

    @Override
    public void Execute()
    {
        Client.mainActivity.showMessageBox(Client.mainActivity.getString(R.string.connectionFailed), Client.mainActivity.getString(R.string.connectionFailedMsg));
        FragmentHelper.goBackToBeginning(Client.mainActivity.getFragmentManager());
        if (closeNetComm) { Client.netComm.close(); }
    }
}
