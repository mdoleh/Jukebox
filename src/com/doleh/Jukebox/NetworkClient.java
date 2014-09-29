package com.doleh.Jukebox;

import android.app.Activity;
import com.doleh.Jukebox.Fragments.ConnectFragment;
import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.Interfaces.INetworkClient;
import com.doleh.Jukebox.MessageTypes.Server.ServerMessage;
import com.doleh.Jukebox.Static.Config;
import com.doleh.Jukebox.Static.MessageDialog;
import com.jackieloven.thebasics.CloseConnectionMsg;
import com.jackieloven.thebasics.NetComm;
import com.jackieloven.thebasics.Networked;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class NetworkClient implements Networked, INetworkClient
{
    public static List<Song> receivedSongs;
    public static String ip;
    public static NetComm netComm;
    public static Socket socket;
    public static Activity mainActivity;

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
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, Config.PORT), 30 * 1000);
                netComm = new NetComm(socket, NetworkClient.this);
            }
            catch (Exception ex) {
                cancelConnect();
            }
        }
    }

    private void cancelConnect()
    {
        netComm = null;
        MessageDialog.showMessageBox(mainActivity, mainActivity.getString(R.string.connectionFailed), mainActivity.getString(R.string.connectionFailedMsg2));
        FragmentHelper.getFragment(ConnectFragment.class, mainActivity.getFragmentManager(), FragmentHelper.CONNECT).unBlockUI();
    }
}
