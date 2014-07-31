package com.doleh.Jukebox.MessageTypes.Client;

import com.doleh.Jukebox.Fragments.PlayerFragment;
import com.doleh.Jukebox.*;
import com.doleh.Jukebox.MessageTypes.Server.LimitRejection;
import com.doleh.Jukebox.MessageTypes.Server.RequestAccepted;
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
    public void Execute(Server server, NetComm sender)
    {
        if (server.checkMessageCount(sender.ipAddress))
        {
            final PlayerFragment playerFragment = server.controlCenterFragment.playerFragment;
            MainActivity mainActivity = server.mainActivity;
            MediaLibraryHelper.playRequest(_requestedSong, mainActivity.getApplicationContext(), playerFragment.mediaPlayer, mainActivity.getContentResolver(), server.controlCenterFragment);
            mainActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    playerFragment.enableAllElements();
                }
            });
            sender.write(new RequestAccepted(server.getRemainingRequests(sender.ipAddress)));
            server.controlCenterFragment.requestListFragment.updateUI();
        }
        else { sender.write(new LimitRejection()); }
    }
}
