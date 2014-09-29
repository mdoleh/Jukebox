package com.doleh.Jukebox.Static.Factories;

import com.doleh.Jukebox.Fragments.ControlCenterFragment;
import com.doleh.Jukebox.Interfaces.IControlCenterView;

public class ControlCenterViewFactory
{
    public static IControlCenterView createControlCenterView()
    {
        return new ControlCenterFragment();
    }
}
