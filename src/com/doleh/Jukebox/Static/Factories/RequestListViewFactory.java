package com.doleh.Jukebox.Static.Factories;

import com.doleh.Jukebox.Fragments.RequestListFragment;
import com.doleh.Jukebox.Interfaces.IRequestListView;

public class RequestListViewFactory
{
    public static IRequestListView createRequestListView()
    {
        return new RequestListFragment();
    }
}
