package com.doleh.Jukebox.MessageTypes.Client;

import android.app.Activity;
import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.Fragments.PlayerFragment;
import com.doleh.Jukebox.Fragments.RequestListFragment;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.MessageTypes.Server.LimitRejection;
import com.doleh.Jukebox.MessageTypes.Server.RequestAccepted;
import com.doleh.Jukebox.NetworkServer;
import com.doleh.Jukebox.Song;
import com.jackieloven.thebasics.NetComm;

import java.io.Serializable;

public class RequestSongId extends ClientMessage implements Serializable
{
    public Song _requestedSong;

    public RequestSongId(Song requestedSong)
    {
        _requestedSong = requestedSong;
    }

    @Override
    public void Execute(NetworkServer networkServer, NetComm sender)
    {
        if (networkServer.checkMessageCount(sender.ipAddress))
        {
            final PlayerFragment playerFragment = FragmentHelper.getFragment(PlayerFragment.class, networkServer.mainActivity.getFragmentManager(), FragmentHelper.MUSIC_PLAYER);
            final RequestListFragment requestListFragment = FragmentHelper.getFragment(RequestListFragment.class, networkServer.mainActivity.getFragmentManager(), FragmentHelper.REQUEST_LIST);
            Activity mainActivity = networkServer.mainActivity;

            MediaLibraryHelper.playRequest(_requestedSong, mainActivity.getApplicationContext(), playerFragment.mediaPlayer, mainActivity.getContentResolver(), playerFragment, requestListFragment);
            mainActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    playerFragment.enableAllElements();
                }
            });
            sender.write(new RequestAccepted(networkServer.getRemainingRequests(sender.ipAddress)));
            requestListFragment.updateUI();
        }
        else { sender.write(new LimitRejection()); }
    }
}
