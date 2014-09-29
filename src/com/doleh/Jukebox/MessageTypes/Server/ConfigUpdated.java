package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Static.MessageDialog;
import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class ConfigUpdated extends ServerMessage implements Serializable
{
    private int _requestsRemaining;

    public ConfigUpdated(int requestsRemaining)
    {
        _requestsRemaining = requestsRemaining;
    }

    @Override
    public void Execute()
    {
        MessageDialog.showMessageBox(NetworkClient.mainActivity, NetworkClient.mainActivity.getString(R.string.configUpdated), String.format(NetworkClient.mainActivity.getString(R.string.configUpdatedMsg), _requestsRemaining));
    }
}
