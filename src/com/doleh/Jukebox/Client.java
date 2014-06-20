package com.doleh.Jukebox;

import com.doleh.Jukebox.Fragments.ConnectFragment;
import com.doleh.Jukebox.Fragments.SongRequestFragment;
import com.doleh.Jukebox.Fragments.SongSearchFragment;
import com.doleh.Jukebox.MessageTypes.Server.ServerMessage;
import com.jackieloven.thebasics.CloseConnectionMsg;
import com.jackieloven.thebasics.NetComm;
import com.jackieloven.thebasics.Networked;

import java.net.Socket;
import java.util.List;

public class Client implements Networked
{
    public static List<Song> receivedSongs;
    public static String ip;
    public static NetComm netComm;
    public static MainActivity mainActivity;
    public static SongRequestFragment songRequestFragment;
    public static SongSearchFragment songSearchFragment;
    public static ConnectFragment connectFragment;

    @Override
    public void msgReceived(Object msgObj, NetComm sender)
    {
        if (!(msgObj instanceof CloseConnectionMsg))
        {
            ((ServerMessage)msgObj).Execute();
        }
    }

    public void startNetworkThread()
    {
        new Thread(new NetworkThread()).start();
    }
    private class NetworkThread implements Runnable
    {
        public void run() {
            try {
                netComm = new NetComm(new Socket(ip, Server.PORT), Client.this);
            } catch (Exception ex) {
                netComm = null;
                mainActivity.showMessageBox(mainActivity.getString(R.string.connectionFailed), mainActivity.getString(R.string.connectionFailedMsg2));
                connectFragment.unBlockUI();
            }
        }
    }
}
