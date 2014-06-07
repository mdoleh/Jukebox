package com.doleh.Jukebox;

import android.os.AsyncTask;
import com.doleh.Jukebox.Fragments.ControlCenterFragment;
import com.doleh.Jukebox.Fragments.PlayerFragment;
import com.doleh.Jukebox.MessageTypes.*;
import com.jackieloven.thebasics.CloseConnectionMsg;
import com.jackieloven.thebasics.NetComm;
import com.jackieloven.thebasics.Networked;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server implements Networked
{
    private static final int MAX_MESSAGE_COUNT = 2;

    private boolean listeningForRequests = false;
    private Map<NetComm, Integer> messageCount = new HashMap<NetComm, Integer>();
    public MainActivity mainActivity;
    public PlayerFragment playerFragment;
    public ControlCenterFragment controlCenterFragment;

    // Network globals
    /** networking port that server listens on */
    public static final int PORT = 35768;
    /** server socket used to set up connections with clients */
    private ServerSocket serverSocket;
    /** ArrayList of client connections */
    private ArrayList<NetComm> netComms = new ArrayList<NetComm>();
    private boolean running = false;

    public Server(MainActivity _mainActivity, ControlCenterFragment _controlCenterFragment, PlayerFragment _playerFragment)
    {
        mainActivity = _mainActivity;
        playerFragment = _playerFragment;
        controlCenterFragment = _controlCenterFragment;
    }

    public void toggleListener()
    {
        listeningForRequests = !listeningForRequests;
        if (listeningForRequests)
        {
            if (!running)
            {
                running = true;
                // initialize server socket
                try {
                    serverSocket = new ServerSocket(PORT);
                }
                catch (IOException ex) {
                    mainActivity.finish();
                }
                // wait for clients to connect
                new Thread(new AcceptClientsThread()).start();
            }
            mainActivity.showMessageBox(mainActivity.getString(R.string.toggleListener), mainActivity.getString(R.string.toggleListenerMessageOn));
        }
        else
        {
            mainActivity.showMessageBox(mainActivity.getString(R.string.toggleListener), mainActivity.getString(R.string.toggleListenerMessageOff));
        }
    }

    @Override
    public void msgReceived(Object msgObj, NetComm sender)
    {
        int senderIndex = findSender(msgObj, sender);
        if (msgObj instanceof CloseConnectionMsg)
        {
            netComms.remove(senderIndex);
            updateRequesterCount(netComms.size());
        }
        else
        {
            if (listeningForRequests)
            {
                if (checkMessageCount(sender, msgObj))
                {
                    SongList listMessage = new SongList(((ClientMessage)msgObj).Execute(this));
                    if (listMessage.songs != null)
                    {
                        sender.write(listMessage);
                    }
                } else { sender.write(new LimitRejection()); }
            } else { sender.write(new Rejection()); }
        }
    }

    private int findSender(Object msgObj, NetComm sender)
    {
        int senderIndex;
        // find who sent the message
        for (senderIndex = 0; senderIndex < netComms.size(); senderIndex++) {
            if (sender == netComms.get(senderIndex)) break;
        }
        if (senderIndex == netComms.size()) {
            mainActivity.showMessageBox(mainActivity.getString(R.string.warning), mainActivity.getString(R.string.unknownRequester) + msgObj);
        }
        return senderIndex;
    }

    /** thread to accept new clients */
    private class AcceptClientsThread implements Runnable
    {
        /** wait for clients to connect */
        public void run() {
            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    if (listeningForRequests)
                    {
                        NetComm requester = new NetComm(socket, Server.this);
                        netComms.add(requester);
                        requester.write(new Accepted());
                        updateRequesterCount(netComms.size());
                    }
                    else
                    {
                        new NetComm(socket, Server.this).write(new Rejection(false));
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    break;
                }
            }
        }
    }

    public void closePort()
    {
        new closePort().execute();
    }
    private class closePort extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object... params)
        {
            try {
                for (NetComm netcomm: netComms)
                {
                    netcomm.write(new ConnectionClosed());
                }
                if (serverSocket != null) { serverSocket.close(); }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    private boolean checkMessageCount(NetComm sender, Object msgObj)
    {
        if (msgObj instanceof RequestSongId)
        {
            Integer count = messageCount.get(sender);
            if (count == null) { messageCount.put(sender, 0); return true; }
            else
            {
                ++count;
                if (count >= MAX_MESSAGE_COUNT)
                {
                    return false;
                }
                else
                {
                    messageCount.put(sender, count);
                    return true;
                }
            }
        }
        else { return true; }
    }

    public void clearMessageCounts()
    {
        messageCount = new HashMap<NetComm, Integer>();
    }

    private void updateRequesterCount(int newValue)
    {
        controlCenterFragment.updateRequesterCount(newValue);
    }
}
