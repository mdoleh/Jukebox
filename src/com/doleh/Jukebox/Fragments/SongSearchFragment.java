package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.doleh.Jukebox.NetworkClient;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MessageTypes.Client.RequestSongList;
import com.doleh.Jukebox.R;

public class SongSearchFragment extends Fragment
{
    private View view;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.song_search, container, false);
        FragmentHelper.loadBannerAds(view);
        mainActivity = (MainActivity)getActivity();
        NetworkClient.songSearchFragment = this;
        setupButtonEventListener();
        hideActionBar();
        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        NetworkClient.netComm.close();
    }

    private void setupButtonEventListener()
    {
        final Button searchButton = ((Button)view.findViewById(R.id.searchButton));
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSearch();
                blockUI(searchButton);
            }
        });
    }

    private void hideActionBar()
    {
        getActivity().getActionBar().hide();
    }

    public void showSongRequest()
    {
        hideKeyboard();
        FragmentHelper.showFragment("song_search", this, "song_request", new SongRequestFragment(), getFragmentManager());
    }

    private void sendSearch()
    {
        // UI elements
        final EditText songTitle = (EditText)view.findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)view.findViewById(R.id.songArtist);

        RequestSongList requestSongList = new RequestSongList(songTitle.getText().toString(), songArtist.getText().toString());
        NetworkClient.netComm.write(requestSongList);
    }

    private void hideKeyboard()
    {
        final EditText textbox = (EditText)view.findViewById(R.id.songTitle);
        InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textbox.getWindowToken(), 0);
    }

    private void blockUI(final Button searchButton)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final ImageView blocker = (ImageView)view.findViewById(R.id.loadingImage);
                FragmentHelper.blockUI(searchButton, blocker);
            }
        });
    }

    public void unBlockUI()
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final Button searchButton = (Button)view.findViewById(R.id.searchButton);
                final ImageView blocker = (ImageView)view.findViewById(R.id.loadingImage);
                FragmentHelper.unBlockUI(searchButton, blocker);
            }
        });
    }
}
