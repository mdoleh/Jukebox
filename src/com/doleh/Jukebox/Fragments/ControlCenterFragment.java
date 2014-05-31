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

public class ControlCenterFragment extends Fragment implements Networked
{
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MainActivity mainActivity;
    private boolean listeningForRequests = false;
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
        setupOnCompletionListener();

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        new closePort().execute();
    }

    private void setupButtonEventListener()
    {
        final Button pauseButton = (Button)view.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pauseButton.setText(MediaLibraryHelper.togglePlay(mediaPlayer, getActivity()));
            }});

        final Button listenButton = ((Button)view.findViewById(R.id.listenerToggle));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleListener();
            }
        });
    }

    private void setupOnCompletionListener()
    {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                mp.reset();
                // If songs are in the queue play them next
                if (!MediaLibraryHelper.songQueueIsEmpty())
                {
                    MediaLibraryHelper.playSong(MediaLibraryHelper.getSongId(0), mainActivity.getApplicationContext(), mediaPlayer);
                    MediaLibraryHelper.removeSong(0);
                }
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
        if (msgObj instanceof CloseConnectionMsg)
        {
            int senderIndex = findSender(msgObj, sender);
            netComms.remove(senderIndex);
            updateRequesterCount(netComms.size());
        }
        else
        {
            if (listeningForRequests)
            {
                SongList listMessage = new SongList(((ClientMessage)msgObj).Execute(mainActivity, mediaPlayer));
                if (listMessage.songs != null)
                {
                    sender.write(listMessage);
                }
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
                    //new closePort().execute();
                    //mainActivity.finish();
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
}
