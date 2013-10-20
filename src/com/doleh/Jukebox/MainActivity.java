package com.doleh.Jukebox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static java.sql.DriverManager.println;

public class MainActivity extends Activity
{
    private MediaLibraryHelper mediaLibraryHelper;
    private ChordManager mChordManager;
    private String sendingNode;
    private String sendingChannel;
    private List<Integer> interfaceList;

    private static final String JUKEBOX_REQUEST_CHANNEL = "jukeboxRequestChannel";

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

        interfaceList = mChordManager.getAvailableInterfaceTypes();
        if (interfaceList.isEmpty())
        {
            // no connection
            return;
        }
        mChordManager.start(interfaceList.get(0), new IChordManagerListener()
        {
            @Override
            public void onStarted(String name, int reason)
            {
                if (STARTED_BY_USER == reason)
                {
                    // Chord started successfully
                    IChordChannel channel = null;
                    channel = mChordManager.joinChannel(JUKEBOX_REQUEST_CHANNEL, mChannelListener);
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

    private IChordChannelListener mChannelListener = new IChordChannelListener()
    {
        @Override
        public void onNodeJoined(String fromNode, String fromChannel)
        {
            sendingNode = fromNode;
            sendingChannel = fromChannel;
        }

        @Override
        public void onNodeLeft(String s, String s2)
        {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onDataReceived(String fromNode, String fromChannel, String payloadType, byte[][] payload)
        {
            try
            {
                println("Data has been received!");
                String songTitle = new String(payload[0], "UTF-8");
                println("songTitle processed!");
                String songArtist = new String(payload[1], "UTF-8");
                println("songArtist processed!");
                checkSongExists(songTitle, songArtist);
            }
            catch (UnsupportedEncodingException e)
            {
                // Failed to convert payload
                e.printStackTrace();
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
        actionBar.addTab(actionBar.newTab().setText("Listen for Requests").setTabListener(tabListener));
    }

    private void showRequestUI()
    {
        // UI Elements
        final TextView label = (TextView)findViewById(R.id.textView);
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);
        final Spinner availableDevices = (Spinner)findViewById(R.id.availableDevices);
        final Button button = ((Button)findViewById(R.id.button));
        final TextView spinnerLabel = (TextView)findViewById(R.id.spinnerLabel);

        songTitle.setVisibility(View.VISIBLE);
        songArtist.setVisibility(View.VISIBLE);
        availableDevices.setVisibility(View.VISIBLE);
        spinnerLabel.setVisibility(View.VISIBLE);
        label.setText("Request a Song");
        button.setText("Send Request");
    }

    private void showListenUI()
    {
        // UI Elements
        final TextView label = (TextView)findViewById(R.id.textView);
        final Button button = ((Button)findViewById(R.id.button));
        final Button stopButton = (Button)findViewById(R.id.stopButton);

        stopButton.setVisibility(View.VISIBLE);
        label.setText("Listen for Requests");
        button.setText("Start Listening");
    }

    private void hideRequestUI()
    {
        // UI Elements
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);
        final Spinner availableDevices = (Spinner)findViewById(R.id.availableDevices);
        final TextView spinnerLabel = (TextView)findViewById(R.id.spinnerLabel);

        songTitle.setVisibility(View.INVISIBLE);
        songArtist.setVisibility(View.INVISIBLE);
        availableDevices.setVisibility(View.INVISIBLE);
        spinnerLabel.setVisibility(View.INVISIBLE);
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
                else
                {
                    startListening();
                }
            }
        });

        final Button stopButton = (Button)findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaLibraryHelper.stopSong();
        }});
    }

    private void sendRequest()
    {
        // UI Elements
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);

        // Send song request
        byte[][] request = new byte[1][];
        request[0] = songTitle.getText().toString().getBytes();
        request[1] = songArtist.getText().toString().getBytes();

        IChordChannel channel = mChordManager.getJoinedChannel(sendingChannel);
        channel.sendData(sendingNode, "songRequest", request);
    }

    private void startListening()
    {
        // Start listening for song requests

    }

    private void checkSongExists(String songTitle, String songArtist)
    {
        // Check if requested song exists
        mediaLibraryHelper = new MediaLibraryHelper();
        List<List> songInfo = mediaLibraryHelper.getSongList(getContentResolver(), songTitle, songArtist);
        List songIds = songInfo.get(0);
        List songTitles = songInfo.get(1);
        if (songIds.size() != 0)
        {
            // Play song / reply with results
            mediaLibraryHelper.playSong((Long) songIds.get(0), getApplicationContext());
        }
        else
        {
            System.out.println(">>>No matching songs found.");
        }
    }
}