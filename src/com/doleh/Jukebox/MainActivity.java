package com.doleh.Jukebox;

import android.app.ActionBar;
import android.app.Activity;
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
import java.util.List;

import static java.sql.DriverManager.println;

public class MainActivity extends Activity
{
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
            println("Data has been received!");

            if (payloadType.equals("songRequest"))
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
            else if (payloadType.equals("songList"))
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
                    e.printStackTrace();
                }
                catch (OptionalDataException e)
                {
                    e.printStackTrace();
                }
                catch (StreamCorruptedException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                // Display list on UI
                ArrayAdapter<Song> songArrayAdapter = new ArrayAdapter<Song>(getApplicationContext(), android.R.layout.simple_spinner_item, songList);
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
                if (tab.getText() == "Request a Song")
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
        actionBar.addTab(actionBar.newTab().setText("Request a Song").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Music Control Center").setTabListener(tabListener));
    }

    private void showRequestUI()
    {
        // UI Elements
        final TextView label = (TextView)findViewById(R.id.textView);
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);
        final Button button = ((Button)findViewById(R.id.button));

        songTitle.setVisibility(View.VISIBLE);
        songArtist.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        button.setText("Send Request");
        label.setText("Request a Song");
    }

    private void showListenUI()
    {
        // UI Elements
        final TextView label = (TextView)findViewById(R.id.textView);
        final Button button = ((Button)findViewById(R.id.button));
        final Button stopButton = (Button)findViewById(R.id.stopButton);

        stopButton.setVisibility(View.VISIBLE);
        button.setText("Become Listener");
        label.setText("Music Control Center");
    }

    private void hideRequestUI()
    {
        // UI Elements
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);
        final Button button = (Button)findViewById(R.id.button);

        songTitle.setVisibility(View.INVISIBLE);
        songArtist.setVisibility(View.INVISIBLE);
        button.setVisibility((View.INVISIBLE));
    }

    private void hideListenUI()
    {
        final Button stopButton = (Button)findViewById(R.id.stopButton);

        stopButton.setVisibility(View.INVISIBLE);
    }

    private void setupButtonEventListener()
    {
        final Button button = ((Button)findViewById(R.id.button));
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (button.getText() == "Send Request")
                {
                    sendRequest();
                }
                else if (button.getText() == "Become Listener")
                {
                    toggleListener();
                }
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
//        channel.sendData(newNode, "songRequest", request);
        // TODO: send data only to the listener node
        channel.sendDataToAll("songRequest", request);
    }

    private void sendPossibleMatches(String fromNode, List<Song> songList)
    {
        try
        {
            // Convert songList into a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(songList);
            byte[][] possibleMatches = new byte[1][];
            possibleMatches[0] = bos.toByteArray();

            // Send byte array to requester
            IChordChannel channel = mChordManager.getJoinedChannel(ChordManager.PUBLIC_CHANNEL);
            if (!channel.sendData(fromNode, "songList", possibleMatches))
            {
                // Failed to send data
                // TODO: display message to user that data failed to send
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private List<Song> checkSongExists(String songTitle, String songArtist)
    {
        // Check if requested song exists
        mediaLibraryHelper = new MediaLibraryHelper();
        return mediaLibraryHelper.getSongList(getContentResolver(), songTitle, songArtist);
    }
}