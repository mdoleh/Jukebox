package com.doleh.Jukebox;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SongSearchFragment extends Fragment
{
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.song_search, container, false);
        setupButtonEventListener();
        return view;
    }

    private void setupButtonEventListener()
    {
        final Button listenButton = ((Button)view.findViewById(R.id.searchButton));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSongRequest();
            }
        });
    }

    private void showSongRequest()
    {
        Fragment fragment = new SongRequestFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.add(R.id.main, fragment, "song_request");
        transaction.addToBackStack("song_search");
        transaction.commit();
    }

    private void sendSearch()
    {
        // UI elements
        final EditText songTitle = (EditText)view.findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)view.findViewById(R.id.songArtist);

        // Variables for data type and data
        String type;
        byte[][] request = new byte[2][];

        // Convert string input to bytes
        request[0] = songTitle.getText().toString().getBytes();
        request[1] = songArtist.getText().toString().getBytes();
        type = Constants.SONG_REQUEST_TYPE;

        // TODO: send search and show next screen
    }
}
