package com.doleh.Jukebox;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongRequestFragment extends Fragment
{
    private List<String> viewableList = new ArrayList<String>();
    private MainActivity mainActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.request_song, container, false);
        mainActivity = (MainActivity)getActivity();
        setupButtonEventListener();
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

//    private void setupSpinnerChangeListener()
//    {
//        final Spinner availableSongsSpinner = ((Spinner)view.findViewById(R.id.songListSpinner));
//        availableSongsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                // Clear text fields
//                final EditText songTitle = (EditText)view.findViewById(R.id.songTitle);
//                final EditText songArtist = (EditText)view.findViewById(R.id.songArtist);
//
//                songTitle.setText("");
//                songArtist.setText("");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//                // Do nothing
//            }
//        });
//    }

    private List<String> createSongListForSpinner(List<Song> songList)
    {
        List<String> temp = new ArrayList<String>();
        for (Song song : songList)
        {
            temp.add(song.id + "-" + song.title + song.artist);
        }
        return temp;
    }

    private void sendRequest()
    {
        // UI Elements
        final Spinner songIdSpinner = (Spinner)view.findViewById(R.id.songListSpinner);

        // Variables for data type and data
        String type;
        byte[][] request = new byte[1][];

        // Extract song id and convert to bytes
        String songId = viewableList.get(songIdSpinner.getSelectedItemPosition());
        songId = songId.substring(0, songId.indexOf("-"));
        request[0] = songId.getBytes();
        type = Constants.SONG_ID_TYPE;

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
            mainActivity.showMessageBox(getString(R.string.errorTitle), getString(R.string.listToBytesError) + Arrays.toString(e.getStackTrace()));
        }
        //TODO: send possible matches
    }
}
