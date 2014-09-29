package com.doleh.Jukebox.MessageTypes.Server;

import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.Fragments.SongRequestFragment;
import com.doleh.Jukebox.Static.MessageDialog;
import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.R;

import java.io.Serializable;

public class LimitRejection extends ServerMessage implements Serializable
{
    @Override
    public void Execute()
    {
        MessageDialog.showMessageBox(NetworkClient.mainActivity, NetworkClient.mainActivity.getString(R.string.connectionFailed), NetworkClient.mainActivity.getString(R.string.limitReachedMsg));
        FragmentHelper.getFragment(SongRequestFragment.class, NetworkClient.mainActivity.getFragmentManager(), FragmentHelper.SONG_REQUEST).unBlockUI();
    }
}
