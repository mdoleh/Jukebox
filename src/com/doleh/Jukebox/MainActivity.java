package com.doleh.Jukebox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    MediaLibraryHelper mediaLibraryHelper;
    CommunicationHelper communicationHelper;
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
        communicationHelper = new CommunicationHelper();
        communicationHelper.setActivity(this);


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
}

