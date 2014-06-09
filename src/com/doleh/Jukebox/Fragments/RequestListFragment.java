package com.doleh.Jukebox.Fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Song;

import java.util.ArrayList;
import java.util.List;

public class RequestListFragment extends ListFragment
{
    private View view;
    private MainActivity mainActivity;
    public ArrayAdapter<String> requestListAdapter;
    private List<String> viewableList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.request_list, container, false);
        mainActivity = (MainActivity)getActivity();
        setupListAdapter();

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }

    private void setupListAdapter()
    {
        createViewableList(MediaLibraryHelper.getSongQueue());
        requestListAdapter = new ArrayAdapter<String>(mainActivity.getApplicationContext(), android.R.layout.simple_list_item_1, viewableList);
        setListAdapter(requestListAdapter);
    }

    public void createViewableList(List<Song> songQueue)
    {
        viewableList.clear();
        for (Song song: songQueue)
        {
            viewableList.add(song.title + "-" + song.artist);
        }
    }

    public void notifyDataSetChanged()
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                updateAdapter();
            }
        });
    }

    private void updateAdapter()
    {
        requestListAdapter.notifyDataSetChanged();
    }
}
