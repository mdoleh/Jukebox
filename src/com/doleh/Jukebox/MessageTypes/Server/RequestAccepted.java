package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class RequestAccepted extends ServerMessage implements Serializable
{
    @Override
    public void Execute(MainActivity mainActivity)
    {
        mainActivity.showMessageBox(mainActivity.getString(R.string.requestAccepted), mainActivity.getString(R.string.requestAcceptedMsg));
    }
}
