package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class ConnectionClosed extends ServerMessage implements Serializable
{
    @Override
    public void Execute(MainActivity mainActivity)
    {
        mainActivity.showMessageBox(mainActivity.getString(R.string.connectionLost), mainActivity.getString(R.string.connectionLostMsg));
        int size = mainActivity.getFragmentManager().getBackStackEntryCount();
        for(int ii = 0; ii < size; ++ii)
        {
            mainActivity.getFragmentManager().popBackStack();
        }
    }
}
