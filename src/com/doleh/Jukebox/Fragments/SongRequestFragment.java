package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MessageTypes.RequestSongId;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Song;

import java.util.ArrayList;
import java.util.List;

public class SongRequestFragment extends Fragment
{
    private MainActivity mainActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.request_song, container, false);
        mainActivity = (MainActivity)getActivity();
        setupButtonEventListener();
        createSongListForSpinner(mainActivity.receivedSongs);
//        setupSpinnerChangeListener();
        return view;
    }

    private void setupButtonEventListener()
    {
        final Button requestButton = ((Button)view.findViewById(R.id.requestButton));
        requestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void createSongListForSpinner(List<Song> songList)
    {
        List<String> viewableList = new ArrayList<String>();
        for (Song song : songList)
        {
            viewableList.add(song.id + "-" + song.title + song.artist);
        }
        // Display list on UI
        ArrayAdapter<String> songArrayAdapter = new ArrayAdapter<String>(mainActivity.getApplicationContext(), android.R.layout.simple_spinner_item, viewableList);
        songArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)view.findViewById(R.id.songListSpinner)).setAdapter(songArrayAdapter);
    }

    private void sendRequest()
    {
        // UI Elements
        final Spinner songIdSpinner = (Spinner)view.findViewById(R.id.songListSpinner);

        String selectedItem = songIdSpinner.getSelectedItem().toString();
        Long id = Long.parseLong(selectedItem.substring(0, selectedItem.indexOf("-")));
        RequestSongId requestSongId = new RequestSongId(id);
        mainActivity.netComm.write(requestSongId);
    }
}
