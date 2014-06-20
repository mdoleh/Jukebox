package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Client;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class LimitRejection extends ServerMessage implements Serializable
{
    @Override
    public void Execute()
    {
        Client.mainActivity.showMessageBox(Client.mainActivity.getString(R.string.connectionFailed), Client.mainActivity.getString(R.string.limitReachedMsg));
        Client.songRequestFragment.unBlockUI();
    }
}
