package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.MessageDialog;
import com.doleh.Jukebox.NetworkClient;
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
        MessageDialog.showMessageBox(NetworkClient.mainActivity, NetworkClient.mainActivity.getString(R.string.connectionFailed), NetworkClient.mainActivity.getString(R.string.connectionFailedMsg));
        NetworkClient.connectFragment.unBlockUI();
        FragmentHelper.goBackToBeginning(NetworkClient.mainActivity.getFragmentManager());
        if (closeNetComm) { NetworkClient.netComm.close(); }
    }
}
