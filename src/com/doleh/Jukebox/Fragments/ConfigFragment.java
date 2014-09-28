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
import com.doleh.Jukebox.Config;
import com.doleh.Jukebox.MessageDialog;
import com.doleh.Jukebox.R;

public class ConfigFragment extends Fragment
{
    private View view;
    private Activity mainActivity;
    private final int SEEK_BAR_STEP = 5;

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
                Config.MAX_MESSAGE_COUNT = seekBar.getProgress();
                Config.SHOULD_PLAY_AUTOMATICALLY = autoplay.isChecked();
                MessageDialog.showMessageBox(mainActivity, getString(R.string.settingsSaved), getString(R.string.settingsSavedMsg));
            }
        });
    }

    private void initialize()
    {
        SeekBar seekBar = (SeekBar)view.findViewById(R.id.maxRequestsBar);
        CheckedTextView autoplay = (CheckedTextView)view.findViewById(R.id.autoplay);

        seekBar.setProgress(Config.MAX_MESSAGE_COUNT);
        autoplay.setChecked(Config.SHOULD_PLAY_AUTOMATICALLY);
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
                progress = progress / SEEK_BAR_STEP;
                progress = progress * SEEK_BAR_STEP;
                maxCount.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                // do nothing
            }
        });
    }
}
