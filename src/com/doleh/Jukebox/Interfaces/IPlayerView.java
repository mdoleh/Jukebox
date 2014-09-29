package com.doleh.Jukebox.Interfaces;

public interface IPlayerView
{
    public void enableAllElements();
    public void disableAllElements();
    public void startUpdatingUI();
    public void stopUpdatingUI();
    public void setCurrentSongPlaying(final String currentSongPlaying);
}