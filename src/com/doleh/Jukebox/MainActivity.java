package com.doleh.Jukebox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.println;

public class MainActivity extends Activity
{
    private MediaLibraryHelper mediaLibraryHelper;
    private IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;
    private BroadcastReceiver receiver;
    private List peers = new ArrayList();

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

        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank.  Code for peer discovery goes in the
                // onReceive method, detailed below.
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
            }
        });

        WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {

                // Out with the old, in with the new.
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                if (peers.size() == 0) {
                    println("No devices found");
                    return;
                }
            }
        };
    }

    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

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
        populateAvailableDevices();
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

    private void populateAvailableDevices()
    {
        // Retrieve available devices
    }

    private void sendRequest()
    {
        // Send song request
    }

    private void startListening()
    {
        // UI Elements
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);

        // Start listening for song requests


        // Check if requested song exists
        mediaLibraryHelper = new MediaLibraryHelper();
        List<List> songInfo = mediaLibraryHelper.getSongList(getContentResolver(), songTitle.getText().toString(), songArtist.getText().toString());
        List songIds = songInfo.get(0);
        List songTitles = songInfo.get(1);
        if (songIds.size() != 0)
        {
            // Play song / reply with results
            mediaLibraryHelper.playSong((Long) songIds.get(0), getApplicationContext());
        }
        else
        {
            System.out.println("No matching songs found.");
        }
    }

    private class WiFiDirectBroadcastReceiver extends BroadcastReceiver
    {
        private WifiP2pManager manager;
        private WifiP2pManager.Channel channel;
        private MainActivity activity;

        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                           MainActivity activity) {
            super();
            this.manager = manager;
            this.channel = channel;
            this.activity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                // Determine if Wifi P2P mode is enabled or not, alert
                // the Activity.
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    // do stuff
                } else {
                    // do stuff
                }
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

                // The peer list has changed!  We should probably do something about
                // that.

            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

                // Connection state changed!  We should probably do something about
                // that.

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
//            DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
//                    .findFragmentById(R.id.frag_list);
//            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
//                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
            }
        }
    }

    private class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice>
    {
        private List<WifiP2pDevice> items;

        public WiFiPeerListAdapter(Context context, int textViewResourceId,
                                   List<WifiP2pDevice> objects) {
            super(context, textViewResourceId, objects);
            items = objects;
        }
    }
}