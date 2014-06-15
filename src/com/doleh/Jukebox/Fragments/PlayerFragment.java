package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MediaLibraryHelper;
import com.doleh.Jukebox.R;
import com.doleh.Jukebox.Utils;

public class PlayerFragment extends Fragment
{
    private View view;
    public MediaPlayer mediaPlayer = new MediaPlayer();
    private MainActivity mainActivity;
    private boolean mediaPlayerReady = false;
    private ControlCenterFragment controlCenterFragment;
    private Handler updateHandler = new Handler();
    private boolean pauseOnButtonPush = false;

    public PlayerFragment(ControlCenterFragment _controlCenterFragment)
    {
        controlCenterFragment = _controlCenterFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.player, container, false);
        mainActivity = (MainActivity)getActivity();
        setupButtonEventListeners();
        setupMediaPlayerListeners();
        setupSeekBarListeners();
        disableAllElements();

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mediaPlayer.release();
        stopUpdatingUI();
        MediaLibraryHelper.isPaused = false;
    }

    private void setupButtonEventListeners()
    {
        final Button pauseButton = (Button)view.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                togglePlay(pauseButton);
        }});

        final Button stopButton = (Button)view.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    togglePlay(pauseButton);
                }
                mediaPlayer.seekTo(0);
                setTime((TextView)view.findViewById(R.id.currentTime), 0);
                resetSeekBar();
            }});

        final Button skipButton = (Button)view.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                pauseButton.setText(getString(R.string.pauseSong));
                MediaLibraryHelper.playNextSongInQueue(mediaPlayer, mainActivity.getApplicationContext(), controlCenterFragment.server);
                controlCenterFragment.server.clearMessageCounts();
                if (!mediaPlayer.isPlaying()) { resetMusicPlayer(); }
                setTime((TextView)view.findViewById(R.id.currentTime), 0);
                resetSeekBar();
            }});
    }

    private void setupMediaPlayerListeners()
    {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                mediaPlayerReady = false;
                mp.stop();
                mp.reset();
                // If songs are in the queue play them next
                MediaLibraryHelper.playNextSongInQueue(mp, mainActivity.getApplicationContext(), controlCenterFragment.server);
                if (!mediaPlayerReady && !mediaPlayer.isPlaying()) { resetMusicPlayer(); }
                controlCenterFragment.server.clearMessageCounts();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mediaPlayerReady = true;
                enableAllElements();
            }
        });
    }

    private void setupSeekBarListeners()
    {
        final SeekBar seekBar = (SeekBar)view.findViewById(R.id.songSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser) { mediaPlayer.seekTo(progress); }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                if (mediaPlayer.isPlaying() && !MediaLibraryHelper.isPaused) { togglePlay(null); }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if (!pauseOnButtonPush) { togglePlay(null); }
            }
        });
    }

    private void togglePlay(Button pauseButton)
    {
        pauseOnButtonPush = pauseButton != null;
        String text = MediaLibraryHelper.togglePlay(mediaPlayer, mainActivity);
        if (text != null && pauseOnButtonPush) { pauseButton.setText(text); }
    }

    private void disableElement(int elementId)
    {
        final View element = view.findViewById(elementId);
        element.setEnabled(false);
    }

    private void enableElement(int elementId)
    {
        final View element = view.findViewById(elementId);
        element.setEnabled(true);
    }

    public void enableAllElements()
    {
        enableElement(R.id.pauseButton);
        enableElement(R.id.stopButton);
        enableElement(R.id.skipButton);
        enableElement(R.id.songSeekBar);
    }

    public void disableAllElements()
    {
        disableElement(R.id.pauseButton);
        disableElement(R.id.stopButton);
        disableElement(R.id.skipButton);
        disableElement(R.id.songSeekBar);
    }

    public void setCurrentSongPlaying(final String currentSongPlaying)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final TextView currentSong = (TextView)view.findViewById(R.id.currentSong);
                currentSong.setText(currentSongPlaying);

                final TextView songDuration = (TextView)view.findViewById(R.id.totalTime);
                setTime(songDuration, mediaPlayer.getDuration());

                final SeekBar songPosition = (SeekBar)view.findViewById(R.id.songSeekBar);
                songPosition.setMax(mediaPlayer.getDuration());
            }
        });
    }

    private void setTime(final TextView element, final int duration)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (mediaPlayer.isPlaying() || mediaPlayerReady) { element.setText(Utils.millisecondsToTime(duration)); }
                else { element.setText(Utils.millisecondsToTime(0)); }
            }
        });
    }

    private void resetSeekBar()
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final SeekBar seekBar = (SeekBar)view.findViewById(R.id.songSeekBar);
                seekBar.setProgress(0);
            }
        });
    }

    private void resetMusicPlayer()
    {
        disableAllElements();
        setCurrentSongPlaying(getString(R.string.empty));
        resetSeekBar();
    }

    public void stopUpdatingUI()
    {
        updateHandler.removeCallbacks(updateTime);
    }

    public void startUpdatingUI()
    {
        updateTime.run();
    }

    private Runnable updateTime = new Runnable()
    {
        @Override
        public void run()
        {
            mainActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    final TextView currentDuration = (TextView)view.findViewById(R.id.currentTime);
                    setTime(currentDuration, mediaPlayer.getCurrentPosition());

                    final SeekBar songPosition = (SeekBar)view.findViewById(R.id.songSeekBar);
                    songPosition.setProgress(mediaPlayer.getCurrentPosition());
                }
            });

            // Run every second
            updateHandler.postDelayed(this, 1000);
        }
    };
}
