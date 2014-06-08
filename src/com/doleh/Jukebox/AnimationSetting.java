package com.doleh.Jukebox;

public class AnimationSetting
{
    // occurs when the current fragment is being added
    public int enter;
    // occurs when another fragment is added and the current one exits
    public int exit;
    // occurs when a fragment is removed and the previous one appears
    public int popEnter;
    // occurs when the current fragment is removed
    public int popExit;

    public AnimationSetting(int _enter, int _exit, int _popEnter, int _popExit)
    {
        enter = _enter;
        exit = _exit;
        popEnter = _popEnter;
        popExit = _popExit;
    }
}
