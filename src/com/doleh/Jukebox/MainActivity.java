package com.doleh.Jukebox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import com.doleh.Jukebox.Fragments.FragmentHelper;
import com.doleh.Jukebox.Fragments.StartupFragment;

public class MainActivity extends Activity
{
    private PowerManager.WakeLock wakeLock;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Add this section of code wherever a fragment should appear (i.e. new screen)
        FragmentHelper.showFragment("startup", new StartupFragment(), getFragmentManager());

        // Prevent LCD screen from turning off
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "LCD-on");
        wakeLock.acquire();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Allow LCD screen to turn off
        if (wakeLock.isHeld()) { wakeLock.release(); }
    }

    public void showMessageBox(final String title, final String message)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertDialog.Builder alert  = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage(message);
                alert.setTitle(title);
                alert.setPositiveButton(getString(R.string.ok), null);
                alert.setCancelable(false);
                alert.create().show();
            }
        });
    }
}