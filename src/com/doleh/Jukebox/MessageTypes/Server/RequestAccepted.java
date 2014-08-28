package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.MessageDialog;
import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class RequestAccepted extends ServerMessage implements Serializable
{
    private int _requestsRemaining;

    public RequestAccepted(int requestsRemaining)
    {
        _requestsRemaining = requestsRemaining;
    }

    @Override
    public void Execute()
    {
        MessageDialog.showMessageBox(NetworkClient.mainActivity, NetworkClient.mainActivity.getString(R.string.requestAccepted), String.format(NetworkClient.mainActivity.getString(R.string.requestAcceptedMsg), _requestsRemaining));
        NetworkClient.songRequestFragment.unBlockUI();
    }
}
