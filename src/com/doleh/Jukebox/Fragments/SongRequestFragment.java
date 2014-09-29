package com.doleh.Jukebox.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.doleh.Jukebox.Static.Config;
import com.doleh.Jukebox.Interfaces.ISongRequestView;
import com.doleh.Jukebox.MessageTypes.Client.RequestSongId;
import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongRequestFragment extends Fragment implements ISongRequestView
{
    private Activity mainActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.request_song, container, false);
        FragmentHelper.loadBannerAds(view);
        mainActivity = getActivity();
        setupButtonEventListener();
        createSongListForSpinner(NetworkClient.receivedSongs);
        return view;
    }

    private void setupButtonEventListener()
    {
        final Button requestButton = ((Button)view.findViewById(R.id.requestButton));
        requestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendRequest();
                blockUI(requestButton);
            }
        });
    }

    private void createSongListForSpinner(List<Song> songList)
    {
        List<String> viewableList = createHyphenatedList(songList);
        displayListOnUI(viewableList);
    }

    private void displayListOnUI(List<String> viewableList)
    {
        ArrayAdapter<String> songArrayAdapter = new ArrayAdapter<String>(mainActivity.getApplicationContext(), android.R.layout.simple_spinner_item, viewableList);
        songArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)view.findViewById(R.id.songListSpinner)).setAdapter(songArrayAdapter);
    }

    private List<String> createHyphenatedList(List<Song> songList)
    {
        Collections.sort(songList);
        List<String> temp = new ArrayList<String>();
        for (Song song : songList)
        {
            temp.add(song.title + "-" + song.artist);
        }
        return temp;
    }

    private void sendRequest()
    {
        // UI Elements
        final Spinner songIdSpinner = (Spinner)view.findViewById(R.id.songListSpinner);

        int selectedIndex = songIdSpinner.getSelectedItemPosition();
        RequestSongId requestSongId = new RequestSongId(NetworkClient.receivedSongs.get(selectedIndex), Config.MAX_MESSAGE_COUNT);
        NetworkClient.netComm.write(requestSongId);
    }

    private void blockUI(final Button requestButton)
    {
        FragmentHelper.blockUI(mainActivity, requestButton, view);
    }

    public void unBlockUI()
    {
        final Button requestButton = (Button)view.findViewById(R.id.requestButton);
        FragmentHelper.unBlockUI(mainActivity, requestButton, view);
    }
}
