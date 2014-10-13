package com.doleh.Jukebox.Static;

import android.app.Fragment;
import android.view.View;

public class Tracking
{
    private static StringBuilder trackingLog = new StringBuilder();
    private static String LINE_SEPARATOR = "\n";

    public static void initialize()
    {
        trackingLog.append("\n************ USER TRACKING ************\n").append(LINE_SEPARATOR);
    }

    public static void logTouch(View view)
    {
        trackingLog.append("User touched: ").append(Utils.getIDName(view)).append(LINE_SEPARATOR);
    }

    public static void logFragmentChange(Fragment fragment)
    {
        trackingLog.append("User loaded: ").append(fragment.getClass().getSimpleName()).append(LINE_SEPARATOR);
    }

    public static void logBackButton()
    {
        trackingLog.append("User touched back button").append(LINE_SEPARATOR);
    }

    public static void logConfig()
    {
        trackingLog.append(LINE_SEPARATOR).append("Configurations: ").append(LINE_SEPARATOR);
        trackingLog.append("APP_PAID: ").append(Config.APP_PAID).append(LINE_SEPARATOR);
        trackingLog.append("MAX_MESSAGE_COUNT: ").append(Config.MAX_MESSAGE_COUNT).append(LINE_SEPARATOR);
        trackingLog.append("AUTO_PLAY: ").append(Config.AUTO_PLAY).append(LINE_SEPARATOR);
        trackingLog.append("SHOULD_SHOW_ADS: ").append(Config.SHOULD_SHOW_ADS).append(LINE_SEPARATOR);
        trackingLog.append("SHOW_SPLASH_SCREEN: ").append(Config.SHOW_SPLASH_SCREEN).append(LINE_SEPARATOR);
    }

    public static void logPause()
    {
        trackingLog.append("App has been paused").append(LINE_SEPARATOR);
    }

    public static void logOrientationChange(int orientation)
    {
        trackingLog.append("User changed orientation to ").append(orientation).append(LINE_SEPARATOR);
    }

    public static void logSeekBarBeginTouch(View seekbar, int progress)
    {
        trackingLog.append("User touched seekbar (").append(Utils.getIDName(seekbar)).append(") at ").append(progress);
    }

    public static void logSeekBarEndTouch(View seekbar, int progress)
    {
        trackingLog.append("User released seekbar (").append(Utils.getIDName(seekbar)).append(") at ").append(progress);
    }

    public static String getTrackingLog()
    {
        return trackingLog.toString();
    }
}
