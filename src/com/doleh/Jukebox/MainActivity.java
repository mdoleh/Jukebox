package com.doleh.Jukebox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import com.doleh.Jukebox.Fragments.ControlCenterFragment;
import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.Fragments.StartupFragment;
import com.hardiktrivedi.Exception.ExceptionHandler;
import com.snippets.Utils.AppRater;

public class MainActivity extends Activity
{
    public static final String SHOW_ADS = "com.doleh.Jukebox.MainActivity.show_ads";
    public static final String APP_PNAME = "com.doleh.Jukebox.MainActivity.app_pname";
    private PowerManager.WakeLock wakeLock;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.main);

        // For handling a bug when the user presses the home button then reopens the app
        // closes the second instance and returns the user to the original instance
        if (!isTaskRoot()) {
            // Android launched another instance of the root activity into an existing task
            //  so just quietly finish and go away, dropping the user back into the activity
            //  at the top of the stack (ie: the last state of this task)
            finish();
            return;
        }

        // Add this section of code wherever a fragment should appear (i.e. new screen)
        FragmentHelper.showFragment("startup", new StartupFragment(), getFragmentManager());

        // Prevent LCD screen from turning off
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "LCD-on");
        wakeLock.acquire();

        AppRater.APP_PNAME = getIntent().getStringExtra(APP_PNAME);
        AppRater.app_launched(this);

        FragmentHelper.shouldShowAds = getIntent().getBooleanExtra(SHOW_ADS, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Allow LCD screen to turn off
        if (wakeLock.isHeld()) { wakeLock.release(); }
    }

    // Detects back button press and asks the user if they want to leave the control center
    // Prevents accidental disconnecting of requesters and stopping music
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        final FragmentManager fragmentManager = getFragmentManager();
        ControlCenterFragment controlCenterFragment = (ControlCenterFragment)fragmentManager.findFragmentByTag("control_center");
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK && controlCenterVisibleAndOnBackStack(fragmentManager, controlCenterFragment)) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.exit)
                    .setMessage(R.string.exitMsg)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fragmentManager.popBackStack();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private boolean controlCenterVisibleAndOnBackStack(FragmentManager fragmentManager, ControlCenterFragment controlCenterFragment)
    {
        return fragmentManager.getBackStackEntryCount() <= 2 && controlCenterFragment != null && controlCenterFragment.isVisible();
    }
}