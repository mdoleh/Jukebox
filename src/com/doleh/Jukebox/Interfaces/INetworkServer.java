package com.doleh.Jukebox.Interfaces;

public interface INetworkServer
{
    public void toggleListener();
    public void closePort();
    public boolean checkMessageCount(String ipAddress, int MAX_MESSAGE_COUNT);
    public void clearMessageCounts();
    public int getRemainingRequests(String ipAddress, int MAX_MESSAGE_COUNT);
    public void notifyConfigUpdate(int MAX_MESSAGE_COUNT);
}