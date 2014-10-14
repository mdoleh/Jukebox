package com.doleh.Jukebox;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import com.doleh.Jukebox.Static.MessageDialog;
import com.doleh.Jukebox.Static.Utils;

public class CrashActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crash_screen);
        hideActionBar();
        MessageDialog.showMessageBox(this, getString(R.string.forceClose), getString(R.string.forceCloseMsg), errorDialogCloseHandler);
    }

    private void hideActionBar()
    {
        getActionBar().hide();
    }

    private DialogInterface.OnClickListener errorDialogCloseHandler = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            Utils.closeApplication();
        }
    };
}
