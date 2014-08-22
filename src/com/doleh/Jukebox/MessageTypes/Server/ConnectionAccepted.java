package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.MessageDialog;
import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.Fragments.ConnectFragment;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class ConnectionAccepted extends ServerMessage implements Serializable
{
    @Override
    public void Execute()
    {
        MessageDialog.showMessageBox(NetworkClient.mainActivity, NetworkClient.mainActivity.getString(R.string.connectionSuccess), NetworkClient.mainActivity.getString(R.string.connectionSuccessMsg));
        ((ConnectFragment) NetworkClient.mainActivity.getFragmentManager().findFragmentByTag("connect")).showSongSearch();
        NetworkClient.connectFragment.unBlockUI();
    }
}
