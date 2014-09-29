package com.doleh.Jukebox.Static;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.doleh.Jukebox.R;

public class MessageDialog
{
    public static void showErrorMessage(final Activity activity)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertDialog.Builder alert  = new AlertDialog.Builder(activity);
                alert.setMessage(activity.getString(R.string.forceCloseMsg));
                alert.setTitle(activity.getString(R.string.forceClose));
                alert.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Utils.closeApplication();
                    }
                });
                alert.setCancelable(false);
                alert.create().show();
            }
        });
    }

    public static void showMessageBox(final Activity mainActivity, final String title, final String message)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertDialog.Builder alert  = new AlertDialog.Builder(mainActivity);
                alert.setMessage(message);
                alert.setTitle(title);
                alert.setPositiveButton(mainActivity.getString(R.string.ok), null);
                alert.setCancelable(false);
                alert.create().show();
            }
        });
    }
}
