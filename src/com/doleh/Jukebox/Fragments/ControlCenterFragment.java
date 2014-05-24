package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.MessageTypes.Message;
import com.doleh.Jukebox.MessageTypes.SongList;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Song;
import com.jackieloven.thebasics.NetComm;
import com.jackieloven.thebasics.Networked;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<NetComm> netComms;
    private boolean running;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.control_center, container, false);
        mainActivity = (MainActivity)getActivity();
        setupButtonEventListener();
        setupOnCompletionListener();

        // initialize server socket
        try {
            serverSocket = new ServerSocket(PORT);
        }
        catch (IOException ex) {
            mainActivity.finish();
        }
        netComms = new ArrayList<NetComm>();
        running = true;

        return view;
    }

    private void setupButtonEventListener()
    {
        final Button listenButton = ((Button)view.findViewById(R.id.listenerToggle));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleListener();
            }
        });

        final Button stopButton = (Button)view.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MediaLibraryHelper.stopSong(mediaPlayer);
            }});
    }

    private void setupOnCompletionListener()
    {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                MediaLibraryHelper.stopSong(mp);
                // If songs are in the queue play them next
                if (!MediaLibraryHelper.songQueueIsEmpty())
                {
                    MediaLibraryHelper.playSong(MediaLibraryHelper.getSongId(0), mainActivity.getApplicationContext(), mp);
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
            mainActivity.showMessageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOn));
            // wait for clients to connect
            new Thread(new AcceptClientsThread()).start();
        }
        else
        {
            mainActivity.showMessageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOff));
        }
    }

    private List<Song> checkSongExists(String songTitle, String songArtist)
    {
        // Check if requested song exists
        return MediaLibraryHelper.getSongList(mainActivity.getContentResolver(), songTitle, songArtist);
    }

    @Override
    public void msgReceived(Object msgObj, NetComm sender)
    {
        //int senderIndex = findSender(msgObj, sender);

        SongList listMessage = new SongList(((Message)msgObj).Execute(mainActivity, mediaPlayer));
        if (listMessage.songs != null)
        {
            sender.write(listMessage);
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
            mainActivity.showMessageBox("Warning", "Received message from unknown requester: " + msgObj);
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
                    netComms.add(new NetComm(socket, ControlCenterFragment.this));
                    mainActivity.showMessageBox("New Requester", "Requester is online.");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    break;
                }
            }
            try {
                serverSocket.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            mainActivity.finish();
        }
    }

//    // TODO: probably belongs in the control center
//    if (payloadType.equals(Constants.SONG_REQUEST_TYPE))
//    {
//        List<Song> songList = checkSongExists(new String(payload[0]), new String(payload[1]));
//
//        // Check if list is empty
//        if (!songList.isEmpty())
//        {
//            // Send list to requester
//            sendPossibleMatches(fromNode, songList);
//        }
//        else
//        {
//            // No possible matches found
//            // TODO: display message to the user indicating no matches found
//        }
//    }

//    // TODO: probably belongs on control center
//    else if (payloadType.equals(Constants.SONG_ID_TYPE))
//    {
//        mediaLibraryHelper.playSong(Long.parseLong(new String(payload[0]), 10), getApplicationContext(), mediaPlayer);
//    }
}
