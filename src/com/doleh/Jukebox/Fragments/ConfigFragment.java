package com.doleh.Jukebox.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.doleh.Jukebox.Static.Config;
import com.doleh.Jukebox.Interfaces.IConfigView;
import com.doleh.Jukebox.Interfaces.INetworkServer;
import com.doleh.Jukebox.Static.MessageDialog;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Static.Tracking;

public class ConfigFragment extends Fragment implements IConfigView
{
    private View view;
    private Activity mainActivity;
    private final int SEEK_BAR_STEP = 5;
    private INetworkServer _networkServer;

    public ConfigFragment(INetworkServer networkServer)
    {
        _networkServer = networkServer;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.config, container, false);
        mainActivity = getActivity();
        setupCheckBoxListeners();
        setupSeekBarListeners();
        setupButtonListeners();
        return view;
    }

    private void setupButtonListeners()
    {
        final CheckedTextView autoplay = (CheckedTextView)view.findViewById(R.id.autoplay);
        final SeekBar seekBar = (SeekBar)view.findViewById(R.id.maxRequestsBar);
        final Button saveButton = (Button)view.findViewById(R.id.configSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int progress = seekBar.getProgress();
                if (Config.MAX_MESSAGE_COUNT != progress)
                {
                    _networkServer.notifyConfigUpdate(progress);
                }
                Config.MAX_MESSAGE_COUNT = progress;
                Config.AUTO_PLAY = autoplay.isChecked();
                MessageDialog.showMessageBox(mainActivity, getString(R.string.settingsSaved), getString(R.string.settingsSavedMsg), null);
            }
        });
    }

    private void initialize()
    {
        SeekBar seekBar = (SeekBar)view.findViewById(R.id.maxRequestsBar);
        CheckedTextView autoplay = (CheckedTextView)view.findViewById(R.id.autoplay);

        seekBar.setProgress(Config.MAX_MESSAGE_COUNT);
        autoplay.setChecked(Config.AUTO_PLAY);
    }

    private void setupCheckBoxListeners()
    {
        final CheckedTextView autoplay = (CheckedTextView)view.findViewById(R.id.autoplay);
        autoplay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autoplay.setChecked(!autoplay.isChecked());
            }
        });
    }

    private void setupSeekBarListeners()
    {
        final TextView maxCount = (TextView)view.findViewById(R.id.maxRequestCount);
        final SeekBar seekBar = (SeekBar)view.findViewById(R.id.maxRequestsBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progress = adjustProgress(seekBar, progress);
                maxCount.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                Tracking.logSeekBarBeginTouch(seekBar, seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                Tracking.logSeekBarEndTouch(seekBar, seekBar.getProgress());
            }
        });
    }

    private int adjustProgress(SeekBar seekBar, int progress)
    {
        progress = progress / SEEK_BAR_STEP;
        progress = progress * SEEK_BAR_STEP;
        seekBar.setProgress(progress);
        return progress;
    }
}
