package com.doleh.Jukebox.Fragments;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Song;
import com.ericharlow.DragNDrop.*;

import java.util.ArrayList;
import java.util.List;

public class RequestListFragment extends ListFragment
{
    private View view;
    private ListView listView;
    private MainActivity mainActivity;
    public ListAdapter requestListAdapter;
    private ArrayList<String> viewableList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.request_list, container, false);
        mainActivity = (MainActivity)getActivity();
        setupListAdapter();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        listView = getListView();
        ((DragNDropListView)listView).setDragListener(mDragListener);
        ((DragNDropListView)listView).setDropListener(mDropListener);
        ((DragNDropListView)listView).setRemoveListener(mRemoveListener);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }

    private void setupListAdapter()
    {
        createViewableList(MediaLibraryHelper.getSongQueue());

        requestListAdapter = new DragNDropAdapter(mainActivity.getApplicationContext(), new int[]{R.layout.dragitem}, new int[]{R.id.listText}, viewableList);
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
        ((DragNDropAdapter)requestListAdapter).notifyDataSetChanged();
    }

    private DropListener mDropListener =
            new DropListener() {
                public void onDrop(int from, int to) {
                    ListAdapter adapter = getListAdapter();
                    if (adapter instanceof DragNDropAdapter) {
                        syncSongQueue(from, to);
                        ((DragNDropAdapter)adapter).onDrop(from, to);
                        getListView().invalidateViews();
                    }
                }
            };

    private void syncSongQueue(int from, int to)
    {
        List<Song> songQueue = MediaLibraryHelper.getSongQueue();
        Song movedSong = songQueue.get(from);
        songQueue.remove(from);
        songQueue.add(to, movedSong);
        MediaLibraryHelper.setSongQueue(songQueue);
    }

    private RemoveListener mRemoveListener =
            new RemoveListener() {
                public void onRemove(int which) {
                    ListAdapter adapter = getListAdapter();
                    if (adapter instanceof DragNDropAdapter) {
                        ((DragNDropAdapter)adapter).onRemove(which);
                        getListView().invalidateViews();
                    }
                }
            };

    private DragListener mDragListener =
            new DragListener() {

                int backgroundColor = Color.BLUE;
                int defaultBackgroundColor;

                public void onDrag(int x, int y, ListView listView) {
                    // TODO Auto-generated method stub
                }

                public void onStartDrag(View itemView) {
                    itemView.setVisibility(View.INVISIBLE);
                    defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
                    itemView.setBackgroundColor(backgroundColor);
                    ImageView iv = (ImageView)itemView.findViewById(R.id.moveImage);
                    if (iv != null) iv.setVisibility(View.INVISIBLE);
                }

                public void onStopDrag(View itemView) {
                    itemView.setVisibility(View.VISIBLE);
                    itemView.setBackgroundColor(defaultBackgroundColor);
                    ImageView iv = (ImageView)itemView.findViewById(R.id.moveImage);
                    if (iv != null) iv.setVisibility(View.VISIBLE);
                }

            };
}
