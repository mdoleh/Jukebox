package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.MessageTypes.*;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Utils;
import com.jackieloven.thebasics.CloseConnectionMsg;
import com.jackieloven.thebasics.NetComm;
import com.jackieloven.thebasics.Networked;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControlCenterFragment extends Fragment implements Networked
{
    private final int MAX_MESSAGE_COUNT = 2;

    public MediaPlayer mediaPlayer = new MediaPlayer();
    private MainActivity mainActivity;
    private boolean listeningForRequests = false;
    private boolean mediaPlayerReady = false;
    private Map<String, Integer> messageCount = new HashMap<String, Integer>();
    private View view;

    // Network globals
    /** networking port that server listens on */
    public static final int PORT = 44247;
    /** server socket used to set up connections with clients */
    private ServerSocket serverSocket;
    /** ArrayList of client connections */
    private ArrayList<NetComm> netComms = new ArrayList<NetComm>();
    private boolean running = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.control_center, container, false);
        mainActivity = (MainActivity)getActivity();
        setupButtonEventListener();
        setupMediaPlayerListeners();

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        new closePort().execute();
        mediaPlayer.stop();
        clearMessageCounts();
        MediaLibraryHelper.clearSongQueue();
    }

    private void setupButtonEventListener()
    {
        final Button pauseButton = (Button)view.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = MediaLibraryHelper.togglePlay(mediaPlayer, getActivity());
                if (text != null) { pauseButton.setText(text); }
            }});

        final Button listenButton = ((Button)view.findViewById(R.id.listenerToggle));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleListener();
            }
        });
    }

    public void disableElement(int elementId)
    {
        final View element = view.findViewById(elementId);
        element.setEnabled(false);
    }

    public void enableElement(int elementId)
    {
        final View element = view.findViewById(elementId);
        element.setEnabled(true);
    }

    private void setupMediaPlayerListeners()
    {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                mediaPlayerReady = false;
                mp.stop();
                mp.reset();
                // If songs are in the queue play them next
                MediaLibraryHelper.playNextSongInQueue(mp, mainActivity.getApplicationContext());
                if (!mediaPlayerReady) { disableElement(R.id.pauseButton); }
                clearMessageCounts();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mediaPlayerReady = true;
                enableElement(R.id.pauseButton);
            }
        });
    }

    private void toggleListener()
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
                final TextView ipAddress = (TextView)view.findViewById(R.id.deviceAddress);
                ipAddress.setText(Utils.getIPAddress(true));
            }
            mainActivity.showMessageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOn));
        }
        else
        {
            mainActivity.showMessageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOff));
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
                if (checkMessageCount(senderIndex, msgObj))
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
            mainActivity.showMessageBox(getString(R.string.warning), getString(R.string.unknownRequester) + msgObj);
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
                        NetComm requester = new NetComm(socket, ControlCenterFragment.this);
                        netComms.add(requester);
                        requester.write(new Accepted());
                        updateRequesterCount(netComms.size());
                    }
                    else
                    {
                        new NetComm(socket, ControlCenterFragment.this).write(new Rejection(false));
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    break;
                }
            }
        }
    }

    private void updateRequesterCount(final int newValue)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final TextView requesterCount = (TextView) view.findViewById(R.id.requesterCount);
                requesterCount.setText(Integer.toString(newValue));
            }
        });
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

    private boolean checkMessageCount(int senderIndex, Object msgObj)
    {
        if (msgObj instanceof RequestSongId)
        {
            String key = netComms.get(senderIndex).ipAddress;
            Integer count = messageCount.get(key);
            if (count == null) { messageCount.put(key, 0); return true; }
            else
            {
                ++count;
                if (count >= MAX_MESSAGE_COUNT)
                {
                    return false;
                }
                else
                {
                    messageCount.put(key, count);
                    return true;
                }
            }
        }
        else { return true; }
    }

    private void clearMessageCounts()
    {
        messageCount = new HashMap<String, Integer>();
    }
}
