package com.doleh.Jukebox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
    private Activity mainActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.request_song, container, false);
        mainActivity = getActivity();
        setupButtonEventListener();
        setupSpinnerChangeListener();
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

    private void setupSpinnerChangeListener()
    {
        final Spinner availableSongsSpinner = ((Spinner)view.findViewById(R.id.songListSpinner));
        availableSongsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Clear text fields
                final EditText songTitle = (EditText)view.findViewById(R.id.songTitle);
                final EditText songArtist = (EditText)view.findViewById(R.id.songArtist);

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
        final EditText songTitle = (EditText)view.findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)view.findViewById(R.id.songArtist);
        final Spinner songIdSpinner = (Spinner)view.findViewById(R.id.songListSpinner);

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
            ((MainActivity)mainActivity).showMessageBox(getString(R.string.errorTitle), getString(R.string.listToBytesError) + Arrays.toString(e.getStackTrace()));
        }
        //TODO: send possible matches
    }

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
}
