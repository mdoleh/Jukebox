package com.doleh.Jukebox;

import android.content.Intent;

public class Config
{
    public static String APP_PNAME;
    public static String APP_TITLE;
    public static boolean SHOULD_SHOW_ADS;

    public static final String SHOULD_SHOW_ADS_KEY = "com.doleh.Jukebox.MainActivity.show_ads";
    public static final String APP_PNAME_KEY = "com.doleh.Jukebox.MainActivity.app_pname";
    public static final String APP_TITLE_KEY = "com.doleh.Jukebox.MainActivity.app_title";

    public static void initialize(Intent intent)
    {
        APP_PNAME = intent.getStringExtra(APP_PNAME_KEY);
        APP_TITLE = intent.getStringExtra(APP_TITLE_KEY);
        SHOULD_SHOW_ADS = intent.getBooleanExtra(SHOULD_SHOW_ADS_KEY, true);
    }
}
