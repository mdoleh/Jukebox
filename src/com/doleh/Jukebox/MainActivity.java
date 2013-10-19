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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
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

        songTitle.setVisibility(View.VISIBLE);
        songArtist.setVisibility(View.VISIBLE);
        availableDevices.setVisibility(View.VISIBLE);
        populateAvailableDevices();
        label.setText("Request a Song");
        button.setText("Send Request");
    }

    private void showListenUI()
    {
        // UI Elements
        final TextView label = (TextView)findViewById(R.id.textView);
        final Button button = ((Button)findViewById(R.id.button));

        label.setText("Listen for Requests");
        button.setText("Start Listening");
    }

    private void hideRequestUI()
    {
        // UI Elements
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);
        final Spinner availableDevices = (Spinner)findViewById(R.id.availableDevices);

        songTitle.setVisibility(View.INVISIBLE);
        songArtist.setVisibility(View.INVISIBLE);
        availableDevices.setVisibility(View.INVISIBLE);
    }

    private void hideListenUI()
    {
        // Will hide Listen UI when more functionality is implemented
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
        final TextView label = (TextView)findViewById(R.id.textView);
        final EditText songTitle = (EditText)findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)findViewById(R.id.songArtist);
        final Spinner availableDevices = (Spinner)findViewById(R.id.availableDevices);
        final Button button = ((Button)findViewById(R.id.button));

        // Start listening for song requests

        // Check if requested song exists
        MediaLibraryHelper mediaLibraryHelper = new MediaLibraryHelper();
        List songIds = mediaLibraryHelper.getSongList(getContentResolver(), songTitle.getText().toString(), songArtist.getText().toString());
        if (songIds.size() != 0)
        {
            // Play song / reply with results
            System.out.print(songIds.get(0));
        }
        else
        {
            System.out.print("No matching songs found.");
        }
    }
}

