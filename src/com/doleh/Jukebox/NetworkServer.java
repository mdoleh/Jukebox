package com.doleh.Jukebox;

import android.app.Activity;
import android.os.AsyncTask;
import com.doleh.Jukebox.Fragments.ControlCenterFragment;
import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.MessageTypes.Client.ClientMessage;
import com.doleh.Jukebox.MessageTypes.Server.ConnectionAccepted;
import com.doleh.Jukebox.MessageTypes.Server.ConnectionClosed;
import com.doleh.Jukebox.MessageTypes.Server.Rejection;
import com.jackieloven.thebasics.CloseConnectionMsg;
import com.jackieloven.thebasics.NetComm;
import com.jackieloven.thebasics.Networked;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkServer implements Networked
{
    private static final int MAX_MESSAGE_COUNT = 3;

    private boolean listeningForRequests = false;
    private Map<String, Integer> messageCount = new HashMap<String, Integer>();
    public Activity mainActivity;
    private ControlCenterFragment controlCenterFragment;

    // Network globals
    /** networking port that server listens on */
    public static final int PORT = 35768;
    /** server socket used to set up connections with clients */
    private ServerSocket serverSocket;
    /** ArrayList of client connections */
    private ArrayList<NetComm> netComms = new ArrayList<NetComm>();
    private boolean running = false;

    public NetworkServer(Activity _mainActivity)
    {
        mainActivity = _mainActivity;
        controlCenterFragment = (ControlCenterFragment)_mainActivity.getFragmentManager().findFragmentByTag(FragmentHelper.CONTROL_CENTER);
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
                    Email.sendErrorReport(ex);
                    mainActivity.finish();
                }
                // wait for clients to connect
                new Thread(new AcceptClientsThread()).start();
            }
            MessageDialog.showMessageBox(mainActivity, mainActivity.getString(R.string.toggleListener), mainActivity.getString(R.string.toggleListenerMessageOn));
        }
        else
        {
            MessageDialog.showMessageBox(mainActivity, mainActivity.getString(R.string.toggleListener), mainActivity.getString(R.string.toggleListenerMessageOff));
        }
    }

    @Override
    public void msgReceived(Object msgObj, NetComm sender)
    {
        Integer senderIndex = findSender(sender);
        if (msgObj instanceof CloseConnectionMsg)
        {
            if (senderIndex != null)
            {
                netComms.remove(senderIndex.intValue());
                updateRequesterCount(netComms.size());
            }
        }
        else
        {
            if (listeningForRequests)
            {
                ((ClientMessage)msgObj).Execute(this, sender);
            } else { sender.write(new Rejection()); }
        }
    }

    private Integer findSender(NetComm sender)
    {
        int senderIndex;
        // find who sent the message
        for (senderIndex = 0; senderIndex < netComms.size(); ++senderIndex) {
            if (sender == netComms.get(senderIndex)) { return senderIndex; }
        }
        return null;
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
                        NetComm requester = new NetComm(socket, NetworkServer.this);
                        netComms.add(requester);
                        requester.write(new ConnectionAccepted());
                        updateRequesterCount(netComms.size());
                    }
                    else
                    {
                        new NetComm(socket, NetworkServer.this).write(new Rejection(false));
                    }
                }
                catch (SocketException ex)
                {
                    // ignore exceptions
                }
                catch (Exception ex) {
                    Email.sendErrorReport(ex);
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
            catch (IOException ex)
            {
                Email.sendErrorReport(ex);
            }
            return null;
        }
    }

    public boolean checkMessageCount(String ipAddress)
    {
        Integer count = messageCount.get(ipAddress);
        if (count == null) { messageCount.put(ipAddress, 1); return true; }
        else
        {
            ++count;
            if (count > MAX_MESSAGE_COUNT)
            {
                return false;
            }
            else
            {
                messageCount.put(ipAddress, count);
                return true;
            }
        }
    }

    public void clearMessageCounts()
    {
        messageCount = new HashMap<String, Integer>();
    }

    private void updateRequesterCount(int newValue)
    {
        controlCenterFragment.updateRequesterCount(newValue);
    }

    public int getRemainingRequests(String ipAddress)
    {
        Integer count = messageCount.get(ipAddress);
        if (count != null)
        {
            return MAX_MESSAGE_COUNT - count;
        }
        return MAX_MESSAGE_COUNT;
    }
}
