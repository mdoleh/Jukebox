package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Client;
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
        Client.mainActivity.showMessageBox(Client.mainActivity.getString(R.string.requestAccepted), String.format(Client.mainActivity.getString(R.string.requestAcceptedMsg), _requestsRemaining));
        Client.songRequestFragment.unBlockUI();
    }
}
