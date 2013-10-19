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

public class MainActivity extends Activity
{
    // UI Elements
    TextView label = (TextView)findViewById(R.id.textView);
    EditText songTitle = (EditText)findViewById(R.id.songTitle);
    EditText songArtist = (EditText)findViewById(R.id.songArtist);
    Spinner availableDevices = (Spinner)findViewById(R.id.availableDevices);
    Button button = ((Button)findViewById(R.id.button));

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
        songTitle.setVisibility(View.VISIBLE);
        songArtist.setVisibility(View.VISIBLE);
        availableDevices.setVisibility(View.VISIBLE);
        populateAvailableDevices();
        label.setText("Request a Song");
        button.setText("Send Request");
    }

    private void showListenUI()
    {
        label.setText("Listen for Requests");
        button.setText("Start Listening");
    }

    private void hideRequestUI()
    {
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
        // Start listening for song requests

        // Check if requested song exists
        MediaLibraryHelper mediaLibraryHelper = new MediaLibraryHelper();
        mediaLibraryHelper.getSongList(getContentResolver(), songTitle.getText().toString(), songArtist.getText().toString());
    }
}

