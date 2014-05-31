package com.doleh.Jukebox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import com.doleh.Jukebox.Fragments.ControlCenterFragment;
import com.doleh.Jukebox.Fragments.StartupFragment;
import com.doleh.Jukebox.MessageTypes.ServerMessage;
import com.jackieloven.thebasics.CloseConnectionMsg;
import com.jackieloven.thebasics.NetComm;
import com.jackieloven.thebasics.Networked;

import java.net.Socket;
import java.util.List;

public class MainActivity extends Activity implements Networked
{
    private PowerManager.WakeLock wakeLock;
    public List<Song> receivedSongs;
    public String ip;
    public NetComm netComm;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Add this section of code wherever a fragment should appear (i.e. new screen)
        Fragment fragment = new StartupFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main, fragment, "startup");
        // addToBackStack allows android back button to return to previous screen
//        transaction.addToBackStack(null);
        transaction.commit();

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

    @Override
    public void msgReceived(Object msgObj, NetComm sender)
    {
        if (!(msgObj instanceof CloseConnectionMsg))
        {
            ((ServerMessage)msgObj).Execute(this);
        }
    }

    public void startNetworkThread()
    {
        new Thread(new NetworkThread()).start();
    }
    private class NetworkThread implements Runnable
    {
        public void run() {
            try {
                netComm = new NetComm(new Socket(ip, ControlCenterFragment.PORT), MainActivity.this);
            } catch (Exception ex) {
                netComm = null;
                showMessageBox(getString(R.string.connectionFailed), getString(R.string.connectionFailedMsg2));
            }
        }
    }

    public void goBackToBeginning()
    {
        int size = getFragmentManager().getBackStackEntryCount();
        for (int ii = 0; ii < size; ++ii)
        {
            getFragmentManager().popBackStack();
        }
    }

    // TODO: will eventually replace all places where fragments are shown
    public void showFragment(String currentFragmentTag, Fragment currentFragment, String newFragmentTag, Fragment newFragment)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(currentFragment);
        transaction.add(R.id.main, newFragment, newFragmentTag);
        transaction.addToBackStack(currentFragmentTag);
        transaction.commit();
    }
}