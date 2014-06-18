package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class LimitRejection extends ServerMessage implements Serializable
{
    @Override
    public void Execute(MainActivity mainActivity)
    {
        mainActivity.showMessageBox(mainActivity.getString(R.string.connectionFailed), mainActivity.getString(R.string.limitReachedMsg));
    }
}
