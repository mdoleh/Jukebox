package com.doleh.Jukebox.Interfaces;

import com.doleh.Jukebox.Song;

import java.util.List;

public interface IRequestListView
{
    public void createViewableList(List<Song> songQueue);
    public void notifyDataSetChanged();
    public void updateUI();
}