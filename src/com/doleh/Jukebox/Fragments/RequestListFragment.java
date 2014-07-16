package com.doleh.Jukebox.Fragments;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    private void setupButtonListeners()
    {
        final ImageView editButton = (ImageView)view.findViewById(R.id.editButton);
        final ImageView saveButton = (ImageView)view.findViewById(R.id.saveButton);

        saveButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, saveButton, new handleSaveTouch());
                return true;
            }
        });

        editButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, editButton, new handleEditTouch());
                return true;
            }
        });
    }

    private void makeEdits(ImageView editButton, ImageView saveButton)
    {
        if (!MediaLibraryHelper.getSongQueue().isEmpty())
        {
            ((DragNDropListView)listView).editingEnabled = true;
            showAllImages();
            notifyDataSetChanged();
            enableAndShow(saveButton);
            disableAndHide(editButton);
        }
    }

    private void saveChanges(ImageView editButton, ImageView saveButton)
    {
        ((DragNDropListView)listView).editingEnabled = false;
        notifyDataSetChanged();
        enableAndShow(editButton);
        disableAndHide(saveButton);
        hideAllImages();
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
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                int size = listView.getChildCount();
                for (int ii = 0; ii < size; ++ii)
                {
                    listView.getChildAt(ii).findViewById(R.id.moveImage).setVisibility(View.VISIBLE);
                    listView.getChildAt(ii).findViewById(R.id.deleteImage).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void hideAllImages()
    {
        int size = listView.getChildCount();
        for (int ii = 0; ii < size; ++ii)
        {
            listView.getChildAt(ii).findViewById(R.id.moveImage).setVisibility(View.GONE);
            listView.getChildAt(ii).findViewById(R.id.deleteImage).setVisibility(View.GONE);
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
                        syncSongQueueMoved(from, to);
                        ((DragNDropAdapter)adapter).onDrop(from, to);
                        getListView().invalidateViews();
                    }
                }
            };

    private void syncSongQueueMoved(int from, int to)
    {
        List<Song> songQueue = MediaLibraryHelper.getSongQueue();
        Song movedSong = songQueue.get(from);
        songQueue.remove(from);
        songQueue.add(to, movedSong);
    }

    private void syncSongQueueDeleted(int which)
    {
        List<Song> songQueue = MediaLibraryHelper.getSongQueue();
        songQueue.remove(which);
    }

    private RemoveListener mRemoveListener =
            new RemoveListener() {
                public void onRemove(int which) {
                    ListAdapter adapter = getListAdapter();
                    if (adapter instanceof DragNDropAdapter) {
                        syncSongQueueDeleted(which);
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
                    hideImage(itemView, R.id.moveImage);
                    hideImage(itemView, R.id.deleteImage);
                }

                public void onStopDrag(View itemView) {
                    resetBackgroundColor(defaultBackgroundColor, previousPosition);
                    showImage(itemView, R.id.moveImage);
                    showImage(itemView, R.id.deleteImage);
                }
            };

    private void resetBackgroundColor(int defaultBackgroundColor, int position)
    {
        if (position != ListView.INVALID_POSITION)
        {
            listView.getChildAt(position).setBackgroundColor(defaultBackgroundColor);
        }
    }

    private void showImage(View view, int id)
    {
        View element = view.findViewById(id);
        if (element != null) { view.findViewById(id).setVisibility(View.VISIBLE); }
    }

    private void hideImage(View view, int id)
    {
        View element = view.findViewById(id);
        if (element != null) { view.findViewById(id).setVisibility(View.GONE); }
    }

    private class handleEditTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            final ImageView editButton = (ImageView)view.findViewById(R.id.editButton);
            final ImageView saveButton = (ImageView)view.findViewById(R.id.saveButton);
            makeEdits(editButton, saveButton);
        }
    }

    private class handleSaveTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            final ImageView editButton = (ImageView)view.findViewById(R.id.editButton);
            final ImageView saveButton = (ImageView)view.findViewById(R.id.saveButton);
            saveChanges(editButton, saveButton);
        }
    }

    public void updateUI()
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (((DragNDropListView)listView).editingEnabled)
                {
                    showAllImages();
                }
                else
                {
                    hideAllImages();
                }
            }
        });
    }
}
