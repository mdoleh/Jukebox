package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.MessageDialog;
import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class LimitRejection extends ServerMessage implements Serializable
{
    @Override
    public void Execute()
    {
        MessageDialog.showMessageBox(NetworkClient.mainActivity, NetworkClient.mainActivity.getString(R.string.connectionFailed), NetworkClient.mainActivity.getString(R.string.limitReachedMsg));
        NetworkClient.songRequestFragment.unBlockUI();
    }
}
