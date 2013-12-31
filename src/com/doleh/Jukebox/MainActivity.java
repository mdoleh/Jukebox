package com.doleh.Jukebox;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
    public static final String TAG = "jukebox";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MediaLibraryHelper mediaLibraryHelper = new MediaLibraryHelper();
    private PowerManager.WakeLock wakeLock;
    private boolean isRequestListener = false;
    private List<String> viewableList = new ArrayList<String>();

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "WiFi Direct", "Song Request", "Control Center" };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        createTabs();
//        setupButtonEventListener();
//        setupSpinnerChangeListener();
//        setupOnCompletionListener();

        // Prevent LCD screen from turning off
//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "LCD-on");
//        wakeLock.acquire();

//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
//
//        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        channel = manager.initialize(this, getMainLooper(), null);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
    {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
//        wakeLock.acquire();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Allow LCD screen to turn off
//        wakeLock.release();
    }

    /**
     * Remove all peers and clear all fields. This is called on
     * BroadcastReceiver receiving a state change event.
     */
//    public void resetData() {
//        DeviceListFragment fragmentList = (DeviceListFragment) getFragmentManager()
//                .findFragmentById(R.id.frag_list);
//        if (fragmentList != null) {
//            fragmentList.clearPeers();
//        }
//    }

// TODO: find proper place for this
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
//    else if (payloadType.equals(Constants.SONG_LIST_TYPE))
//    {
//        List<Song> songList = new ArrayList<Song>();
//        try
//        {
//            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(payload[0]));
//            songList = (List<Song>)ois.readObject();
//            ois.close();
//        }
//        catch (ClassNotFoundException e)
//        {
//            showMessageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
//        }
//        catch (OptionalDataException e)
//        {
//            showMessageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
//        }
//        catch (StreamCorruptedException e)
//        {
//            showMessageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
//        }
//        catch (IOException e)
//        {
//            showMessageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
//        }
//
//        //Create a list of songs for displaying to the user
//        viewableList = createSongListForSpinner(songList);
//
//        // Display list on UI
//        ArrayAdapter<String> songArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, viewableList);
//        songArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ((Spinner)findViewById(R.id.songListSpinner)).setAdapter(songArrayAdapter);
//    }
//    else if (payloadType.equals(Constants.SONG_ID_TYPE))
//    {
//        mediaLibraryHelper.playSong(Long.parseLong(new String(payload[0]), 10), getApplicationContext(), mediaPlayer);
//    }

    private void createTabs()
    {
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

//    private void setupButtonEventListener()
//    {
//        final Button requestButton = ((Button)findViewById(R.id.requestButton));
//        requestButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                sendRequest();
//            }
//        });
//
//        final Button listenButton = ((Button)findViewById(R.id.listenerToggle));
//        listenButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                toggleListener();
//            }
//        });
//
//        final Button stopButton = (Button)findViewById(R.id.stopButton);
//        stopButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mediaLibraryHelper.stopSong(mediaPlayer);
//        }});
//    }

    private void setupSpinnerChangeListener()
    {
        final Spinner availableSongsSpinner = ((Spinner)findViewById(R.id.songListSpinner));
        availableSongsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Clear text fields
                final EditText songTitle = (EditText)findViewById(R.id.songTitle);
                final EditText songArtist = (EditText)findViewById(R.id.songArtist);

                songTitle.setText("");
                songArtist.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // Do nothing
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
                mediaLibraryHelper.stopSong(mp);
                // If songs are in the queue play them next
                if (!mediaLibraryHelper.songQueue.isEmpty())
                {
                    mediaLibraryHelper.playSong(mediaLibraryHelper.songQueue.get(0), getApplicationContext(), mp);
                    mediaLibraryHelper.songQueue.remove(0);
                }
            }
        });
    }

    private void toggleListener()
    {
        isRequestListener = !isRequestListener;
        if (isRequestListener)
        {
            showMessageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOn));
        }
        else
        {
            showMessageBox(getString(R.string.toggleListener), getString(R.string.toggleListenerMessageOff));
        }
    }

    private void sendRequest()
    {
        // UI Elements
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);
        final Spinner songIdSpinner = (Spinner)findViewById(R.id.songListSpinner);

        // Variables for data type and data
        String type;
        byte[][] request = new byte[2][];

        // Check if request is an id or a search
        if (songTitle.getText().toString().equals("") && songArtist.getText().toString().equals(""))
        {
            // Extract song id and convert to bytes
            request = new byte[1][];
            String songId = viewableList.get(songIdSpinner.getSelectedItemPosition());
            songId = songId.substring(0, songId.indexOf("-"));
            request[0] = songId.getBytes();
            type = Constants.SONG_ID_TYPE;
        }
        else
        {
            // Convert string input to bytes
            request[0] = songTitle.getText().toString().getBytes();
            request[1] = songArtist.getText().toString().getBytes();
            type = Constants.SONG_REQUEST_TYPE;
        }
        //TODO: Send request
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
            showMessageBox(getString(R.string.errorTitle), getString(R.string.listToBytesError) + Arrays.toString(e.getStackTrace()));
        }
        //TODO: send possible matches
    }

    private List<Song> checkSongExists(String songTitle, String songArtist)
    {
        // Check if requested song exists
        return mediaLibraryHelper.getSongList(getContentResolver(), songTitle, songArtist);
    }

    private List<String> createSongListForSpinner(List<Song> songList)
    {
        List<String> temp = new ArrayList<String>();
        for (Song song : songList)
        {
            temp.add(song.id + "-" + song.title + song.artist);
        }
        return temp;
    }

    private void showMessageBox(String title, String message)
    {
        AlertDialog.Builder alert  = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setTitle(title);
        alert.setPositiveButton("OK", null);
        alert.setCancelable(true);
        alert.create().show();
    }
}