package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.Fragments.ConnectFragment;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class Accepted extends ServerMessage implements Serializable
{
    @Override
    public void Execute(MainActivity mainActivity)
    {
        mainActivity.showMessageBox(mainActivity.getString(R.string.connectionSuccess), mainActivity.getString(R.string.connectionSuccessMsg));
        ((ConnectFragment)mainActivity.getFragmentManager().findFragmentByTag("connect")).showSongSearch();
    }
}
