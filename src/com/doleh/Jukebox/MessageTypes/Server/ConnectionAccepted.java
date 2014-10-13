package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.Interfaces.IConnectView;
import com.doleh.Jukebox.Static.MessageDialog;
import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.Fragments.ConnectFragment;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class ConnectionAccepted extends ServerMessage implements Serializable
{
    @Override
    public void Execute()
    {
        MessageDialog.showMessageBox(NetworkClient.mainActivity, NetworkClient.mainActivity.getString(R.string.connectionSuccess), NetworkClient.mainActivity.getString(R.string.connectionSuccessMsg), null);
        IConnectView connectFragment = FragmentHelper.getFragment(ConnectFragment.class, NetworkClient.mainActivity.getFragmentManager(), FragmentHelper.CONNECT);
        connectFragment.showSongSearch();
        connectFragment.unBlockUI();
    }
}
