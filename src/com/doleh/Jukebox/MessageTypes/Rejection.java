package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.MainActivity;

import java.io.Serializable;

public class Rejection extends ServerMessage implements Serializable
{
    @Override
    public void Execute(MainActivity mainActivity)
    {
        mainActivity.showMessageBox("Connection Failed", "Control Center is not accepting requests at this time.");
        mainActivity.netComm.close();
    }
}
