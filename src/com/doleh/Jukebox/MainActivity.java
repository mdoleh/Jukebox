package com.doleh.Jukebox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import com.doleh.Jukebox.Fragments.ControlCenterFragment;
import com.doleh.Jukebox.Fragments.FragmentHelper;
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
        FragmentHelper.showFragment(null, null, "startup", new StartupFragment(), getFragmentManager());

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
}