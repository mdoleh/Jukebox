package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class Rejection extends ServerMessage implements Serializable
{
    private boolean closeNetComm;

    public Rejection()
    {
        closeNetComm = true;
    }

    public Rejection(boolean _closeNetComm)
    {
        closeNetComm = _closeNetComm;
    }

    @Override
    public void Execute(MainActivity mainActivity)
    {
        mainActivity.showMessageBox(mainActivity.getString(R.string.connectionFailed), mainActivity.getString(R.string.connectionFailedMsg));
        FragmentHelper.goBackToBeginning(mainActivity.getFragmentManager());
        if (closeNetComm) { mainActivity.netComm.close(); }
    }
}
