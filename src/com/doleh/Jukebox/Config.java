package com.doleh.Jukebox;

import android.content.Intent;

public class Config
{
    public static boolean SHOW_SPLASH_SCREEN = true;
    public static String APP_PNAME;
    public static String APP_TITLE;
    public static boolean SHOULD_SHOW_ADS;
    public static int MAX_MESSAGE_COUNT;
    public static boolean APP_PAID;
    public static boolean SHOULD_PLAY_AUTOMATICALLY;
    /** networking port that server listens on */
    public static final int PORT = 35768;

    // Intent keys
    public static final String SHOULD_SHOW_ADS_KEY = "com.doleh.Jukebox.MainActivity.show_ads";
    public static final String APP_PNAME_KEY = "com.doleh.Jukebox.MainActivity.app_pname";
    public static final String APP_TITLE_KEY = "com.doleh.Jukebox.MainActivity.app_title";
    public static final String MAX_MESSAGE_COUNT_KEY = "com.doleh.Jukebox.MainActivity.max_message_count";
    public static final String APP_PAID_KEY = "com.doleh.Jukebox.MainActivity.app_paid";

    public static void initialize(Intent intent)
    {
        APP_PNAME = intent.getStringExtra(APP_PNAME_KEY);
        APP_TITLE = intent.getStringExtra(APP_TITLE_KEY);
        SHOULD_SHOW_ADS = intent.getBooleanExtra(SHOULD_SHOW_ADS_KEY, true);
        MAX_MESSAGE_COUNT = intent.getIntExtra(MAX_MESSAGE_COUNT_KEY, 3);
        APP_PAID = intent.getBooleanExtra(APP_PAID_KEY, false);
        SHOULD_PLAY_AUTOMATICALLY = true;
        SHOW_SPLASH_SCREEN = false;
    }
}
