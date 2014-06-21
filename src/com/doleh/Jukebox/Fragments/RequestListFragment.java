package com.doleh.Jukebox.Fragments;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.doleh.Jukebox.*;
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
    private List<Song> songQueueOriginal = new ArrayList<Song>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.request_list, container, false);
        mainActivity = (MainActivity)getActivity();
        setupListAdapter();
        setupButtonListeners();

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

    private void setupButtonListeners()
    {
        final Button editButton = (Button)view.findViewById(R.id.editButton);
        final Button saveButton = (Button)view.findViewById(R.id.saveButton);
        final Button cancelButton = (Button)view.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MediaLibraryHelper.setSongQueue(songQueueOriginal);
                createViewableList(songQueueOriginal);
                saveChanges(editButton, cancelButton, saveButton);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveChanges(editButton, cancelButton, saveButton);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                makeEdits(editButton, cancelButton, saveButton);
            }
        });
    }

    private void makeEdits(Button editButton, Button cancelButton, Button saveButton)
    {
        if (!MediaLibraryHelper.getSongQueue().isEmpty())
        {
            ((DragNDropListView)listView).draggingEnabled = true;
            showAllImages();
            notifyDataSetChanged();
            enableAndShow(saveButton);
            enableAndShow(cancelButton);
            disableAndHide(editButton);
            songQueueOriginal = new ArrayList<Song>(MediaLibraryHelper.getSongQueue());
        }
    }

    private void saveChanges(Button editButton, Button cancelButton, Button saveButton)
    {
        ((DragNDropListView)listView).draggingEnabled = false;
        hideAllImages();
        notifyDataSetChanged();
        enableAndShow(editButton);
        disableAndHide(saveButton);
        disableAndHide(cancelButton);
    }

    private void disableAndHide(View view)
    {
        view.setEnabled(false);
        view.setVisibility(View.INVISIBLE);
    }

    private void enableAndShow(View view)
    {
        view.setEnabled(true);
        view.setVisibility(View.VISIBLE);
    }

    private void showAllImages()
    {
        int size = listView.getChildCount();
        for (int ii = 0; ii < size; ++ii)
        {
            listView.getChildAt(ii).findViewById(R.id.moveImage).setVisibility(View.VISIBLE);
        }
    }

    private void hideAllImages()
    {
        int size = listView.getChildCount();
        for (int ii = 0; ii < size; ++ii)
        {
            listView.getChildAt(ii).findViewById(R.id.moveImage).setVisibility(View.INVISIBLE);
        }
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
                int dropBackgroundColor = Color.RED;
                int defaultBackgroundColor;
                int previousPosition;

                public void onDrag(int x, int y, ListView listView) {
                    resetBackgroundColor(defaultBackgroundColor, previousPosition);
                    previousPosition = listView.pointToPosition(x,y);
                    if (previousPosition != ListView.INVALID_POSITION)
                    {
                        listView.getChildAt(previousPosition).setBackgroundColor(dropBackgroundColor);
                    }
                }

                public void onStartDrag(View itemView) {
                    previousPosition = listView.getPositionForView(itemView);
                    defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
                    itemView.setBackgroundColor(backgroundColor);
                    ImageView iv = (ImageView)itemView.findViewById(R.id.moveImage);
                    if (iv != null) iv.setVisibility(View.INVISIBLE);
                }

                public void onStopDrag(View itemView) {
                    resetBackgroundColor(defaultBackgroundColor, previousPosition);
                    ImageView iv = (ImageView)itemView.findViewById(R.id.moveImage);
                    if (iv != null) iv.setVisibility(View.VISIBLE);
                }
            };

    private void resetBackgroundColor(int defaultBackgroundColor, int position)
    {
        if (position != ListView.INVALID_POSITION)
        {
            listView.getChildAt(position).setBackgroundColor(defaultBackgroundColor);
        }
    }
}
