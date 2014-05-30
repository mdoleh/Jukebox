package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class Rejection extends ServerMessage implements Serializable
{
    @Override
    public void Execute(MainActivity mainActivity)
    {
        mainActivity.showMessageBox(mainActivity.getString(R.string.connectionFailed), mainActivity.getString(R.string.connectionFailedMsg));
        mainActivity.goBackToBeginning();
        mainActivity.netComm.close();
    }
}
