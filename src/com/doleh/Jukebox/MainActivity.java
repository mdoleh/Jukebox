package com.doleh.Jukebox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.*;
import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity
{
    private static final String SONG_REQUEST_TYPE = "songRequest";
    private static final String SONG_LIST_TYPE = "songList";

    private MediaLibraryHelper mediaLibraryHelper;
    private ChordManager mChordManager;
    private List<Integer> interfaceList;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private boolean isRequestListener = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        createTabs();
        setupButtonEventListener();

        // Start a chord
        mChordManager = ChordManager.getInstance(this);
        mChordManager.setTempDirectory(String.valueOf(getCacheDir()));
        mChordManager.setHandleEventLooper(getMainLooper());

        // Prevent LCD screen from turning off
        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "LCD-on");
        wakeLock.acquire();

        // Verify connection
        interfaceList = mChordManager.getAvailableInterfaceTypes();
        if (interfaceList.isEmpty())
        {
            // no connection
            return;
        }
        // Start connection
        mChordManager.start(interfaceList.get(ChordManager.INTERFACE_TYPE_WIFIP2P), new IChordManagerListener()
        {
            @Override
            public void onStarted(String name, int reason)
            {
                if (STARTED_BY_USER == reason)
                {
                    // Chord started successfully
                    IChordChannel channel = null;
                    channel = mChordManager.joinChannel(ChordManager.PUBLIC_CHANNEL, mChannelListener);
                    if (channel == null)
                    {
                        // Failed to join channel
                        return;
                    }
                }
            }

            @Override
            public void onNetworkDisconnected()
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onError(int i)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onStopped(int reason)
            {
                if (STOPPED_BY_USER == reason)
                {
                    mChordManager.close();
                }
            }
        });
    }

    public void OnPause()
    {
        // Allow LCD screen to turn off
        wakeLock.release();
    }

    private IChordChannelListener mChannelListener = new IChordChannelListener()
    {
        @Override
        public void onNodeJoined(String fromNode, String fromChannel)
        {
            if (isRequestListener)
            {
                // TODO: have request listener send message to new node joined to ID the listener
            }
        }

        @Override
        public void onNodeLeft(String fromNode, String fromChannel)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onDataReceived(String fromNode, String fromChannel, String payloadType, byte[][] payload)
        {
            if (payloadType.equals(SONG_REQUEST_TYPE))
            {
                List<Song> songList = checkSongExists(new String(payload[0]), new String(payload[1]));

                // Check if list is empty
                if (!songList.isEmpty())
                {
                    // Send list to requester
                    sendPossibleMatches(fromNode, songList);
                }
                else
                {
                    // No possible matches found
                    // TODO: display message to the user indicating no matches found
                }
            }
            else if (payloadType.equals(SONG_LIST_TYPE))
            {
                List<Song> songList = new ArrayList<Song>();
                try
                {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(payload[0]));
                    songList = (List<Song>)ois.readObject();
                    ois.close();
                }
                catch (ClassNotFoundException e)
                {
                    messageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
                }
                catch (OptionalDataException e)
                {
                    messageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
                }
                catch (StreamCorruptedException e)
                {
                    messageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
                }
                catch (IOException e)
                {
                    messageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
                }

                //Create a list of songs for displaying to the user
                List<String> viewableList = createSongListForSpinner(songList);

                // Display list on UI
                ArrayAdapter<String> songArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, viewableList);
                songArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((Spinner)findViewById(R.id.songListSpinner)).setAdapter(songArrayAdapter);
            }
        }

        @Override
        public void onFileWillReceive(String s, String s2, String s3, String s4, String s5, String s6, long l)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onFileChunkReceived(String s, String s2, String s3, String s4, String s5, String s6, long l, long l2)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onFileReceived(String s, String s2, String s3, String s4, String s5, String s6, long l, String s7)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onFileChunkSent(String s, String s2, String s3, String s4, String s5, String s6, long l, long l2, long l3)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onFileSent(String s, String s2, String s3, String s4, String s5, String s6)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onFileFailed(String s, String s2, String s3, String s4, String s5, int i)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onMultiFilesWillReceive(String s, String s2, String s3, String s4, int i, String s5, long l)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onMultiFilesChunkReceived(String s, String s2, String s3, String s4, int i, String s5, long l, long l2)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onMultiFilesReceived(String s, String s2, String s3, String s4, int i, String s5, long l, String s6)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onMultiFilesChunkSent(String s, String s2, String s3, String s4, int i, String s5, long l, long l2, long l3)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onMultiFilesSent(String s, String s2, String s3, String s4, int i, String s5)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onMultiFilesFailed(String s, String s2, String s3, String s4, int i, int i2)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onMultiFilesFinished(String s, String s2, String s3, int i)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    private void createTabs()
    {
        final ActionBar actionBar = getActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                if (tab.getText() == getString(R.string.requestSong))
                {
                    hideListenUI();
                    showRequestUI();
                }
                else
                {
                    hideRequestUI();
                    showListenUI();
                }
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 2 tabs, specifying the tab's text and TabListener
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.requestSong)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.musicControlCenter)).setTabListener(tabListener));
    }

    private void showRequestUI()
    {
        // UI Elements
        final TextView label = (TextView)findViewById(R.id.textView);
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);
        final Button requestButton = ((Button)findViewById(R.id.requestButton));
        final Spinner songListSpinner = (Spinner)findViewById(R.id.songListSpinner);

        songTitle.setVisibility(View.VISIBLE);
        songArtist.setVisibility(View.VISIBLE);
        requestButton.setVisibility(View.VISIBLE);
        songListSpinner.setVisibility(View.VISIBLE);
        label.setText(getString(R.string.requestSong));
    }

    private void showListenUI()
    {
        // UI Elements
        final TextView label = (TextView)findViewById(R.id.textView);
        final Button listenerButton = ((Button)findViewById(R.id.listenerToggle));
        final Button stopButton = (Button)findViewById(R.id.stopButton);

        listenerButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        label.setText(getString(R.string.musicControlCenter));
    }

    private void hideRequestUI()
    {
        // UI Elements
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);
        final Button requestButton = (Button)findViewById(R.id.requestButton);
        final Spinner songListSpinner = (Spinner)findViewById(R.id.songListSpinner);

        songTitle.setVisibility(View.INVISIBLE);
        songArtist.setVisibility(View.INVISIBLE);
        requestButton.setVisibility((View.INVISIBLE));
        songListSpinner.setVisibility((View.INVISIBLE));
    }

    private void hideListenUI()
    {
        final Button stopButton = (Button)findViewById(R.id.stopButton);
        final Button listenButton = (Button)findViewById(R.id.listenerToggle);

        stopButton.setVisibility(View.INVISIBLE);
        listenButton.setVisibility(View.INVISIBLE);
    }

    private void setupButtonEventListener()
    {
        final Button requestButton = ((Button)findViewById(R.id.requestButton));
        requestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendRequest();
            }
        });

        final Button listenButton = ((Button)findViewById(R.id.listenerToggle));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleListener();
            }
        });

        final Button stopButton = (Button)findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaLibraryHelper.stopSong();
        }});
    }

    private void toggleListener()
    {
        isRequestListener = !isRequestListener;
        if (isRequestListener)
        {
            messageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOn));
        }
        else
        {
            messageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOff));
        }
    }

    private void sendRequest()
    {
        // UI Elements
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);

        // Send song request
        byte[][] request = new byte[2][];
        request[0] = songTitle.getText().toString().getBytes();
        request[1] = songArtist.getText().toString().getBytes();

        IChordChannel channel = mChordManager.getJoinedChannel(ChordManager.PUBLIC_CHANNEL);
//        channel.sendData(newNode, SONG_REQUEST_TYPE, request);
        // TODO: send data only to the listener node and add if statement as done in sendPossibleMatches
        channel.sendDataToAll(SONG_REQUEST_TYPE, request);
    }

    private void sendPossibleMatches(String fromNode, List<Song> songList)
    {
        byte[][] possibleMatches = new byte[1][];
        try
        {
            // Convert songList into a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(songList);
            possibleMatches[0] = bos.toByteArray();
        }
        catch (IOException e)
        {
            messageBox(getString(R.string.errorTitle), getString(R.string.listToBytesError) + Arrays.toString(e.getStackTrace()));
        }
        // Send byte array to requester
        IChordChannel channel = mChordManager.getJoinedChannel(ChordManager.PUBLIC_CHANNEL);
        if (!channel.sendData(fromNode, SONG_LIST_TYPE, possibleMatches))
//            if (!channel.sendDataToAll(SONG_LIST_TYPE, possibleMatches))
        {
            // Failed to send data
            messageBox(getString(R.string.sendFailureTitle), getString(R.string.sendMatchesFailureMessage));
        }
    }

    private List<Song> checkSongExists(String songTitle, String songArtist)
    {
        // Check if requested song exists
        mediaLibraryHelper = new MediaLibraryHelper();
        return mediaLibraryHelper.getSongList(getContentResolver(), songTitle, songArtist);
    }

    private void messageBox(String title, String message)
    {
        AlertDialog.Builder alert  = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setTitle(title);
        alert.setPositiveButton("OK", null);
        alert.setCancelable(true);
        alert.create().show();
    }

    private List<String> createSongListForSpinner(List<Song> songList)
    {
        List<String> temp = new ArrayList<String>();
        for (Song aSongList : songList)
        {
            temp.add(aSongList.id + aSongList.title + aSongList.artist);
        }
        return temp;
    }
}