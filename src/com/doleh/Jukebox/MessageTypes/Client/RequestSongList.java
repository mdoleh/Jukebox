package com.doleh.Jukebox.MessageTypes.Client;

import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.MessageTypes.Server.SongList;
import com.doleh.Jukebox.Server;
import com.jackieloven.thebasics.NetComm;

import java.io.Serializable;

public class RequestSongList extends ClientMessage implements Serializable
{
    public String _title;
    public String _artist;

    public RequestSongList(String title, String artist)
    {
        _title = title;
        _artist = artist;
    }

    @Override
    public void Execute(Server server, NetComm sender)
    {
        SongList listMessage = new SongList(MediaLibraryHelper.getSongList(server.mainActivity.getContentResolver(), _title, _artist));
        if (listMessage.songs != null)
        {
            sender.write(listMessage);
        }
    }
}
