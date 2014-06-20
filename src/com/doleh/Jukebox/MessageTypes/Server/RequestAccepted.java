package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Client;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class RequestAccepted extends ServerMessage implements Serializable
{
    @Override
    public void Execute()
    {
        Client.mainActivity.showMessageBox(Client.mainActivity.getString(R.string.requestAccepted), Client.mainActivity.getString(R.string.requestAcceptedMsg));
        Client.songRequestFragment.unBlockUI();
    }
}
