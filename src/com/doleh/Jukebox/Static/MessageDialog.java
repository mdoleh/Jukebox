package com.doleh.Jukebox.Static;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.doleh.Jukebox.R;

public class MessageDialog
{
    public static void showMessageBox(final Activity mainActivity, final String title, final String message, final DialogInterface.OnClickListener closeHandler)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertDialog.Builder alert  = new AlertDialog.Builder(mainActivity);
                alert.setMessage(message);
                alert.setTitle(title);
                alert.setPositiveButton(mainActivity.getString(R.string.ok), closeHandler);
                alert.setCancelable(false);
                alert.create().show();
            }
        });
    }
}
