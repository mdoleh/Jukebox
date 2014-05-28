package com.doleh.Jukebox.MessageTypes;

import com.doleh.Jukebox.Fragments.ConnectFragment;
import com.doleh.Jukebox.MainActivity;

import java.io.Serializable;

public class Accepted extends ServerMessage implements Serializable
{
    @Override
    public void Execute(MainActivity mainActivity)
    {
        mainActivity.showMessageBox("Connection Success", "Control Center is now accepting requests.");
        ((ConnectFragment)mainActivity.getFragmentManager().findFragmentByTag("connect")).showSongSearch();
    }
}
