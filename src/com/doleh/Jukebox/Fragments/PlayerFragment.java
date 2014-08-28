package com.doleh.Jukebox.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.doleh.Jukebox.*;

public class PlayerFragment extends Fragment
{
    private View view;
    public MediaPlayer mediaPlayer = new MediaPlayer();
    private Activity mainActivity;
    private boolean mediaPlayerReady = false;
    private Server _server;
    private Handler updateHandler = new Handler();
    private boolean pauseOnButtonPush = false;

    public PlayerFragment(Server server)
    {
        _server = server;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.player, container, false);
        FragmentHelper.loadBannerAds(view);
        mainActivity = getActivity();
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
        final ImageView pauseButton = (ImageView)view.findViewById(R.id.pauseButton);
        pauseButton.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, pauseButton, new handlePauseTouch());
                return true;
            }
        });

        final ImageView stopButton = (ImageView)view.findViewById(R.id.stopButton);
        stopButton.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, stopButton, new handleStopTouch());
                return true;
            }
        });

        final ImageView skipButton = (ImageView)view.findViewById(R.id.skipButton);
        skipButton.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                FragmentHelper.handleTouchEvents(event, skipButton, new handleSkipTouch());
                return true;
            }
        });
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
                MediaLibraryHelper.playNextSongInQueue(mp, mainActivity.getApplicationContext(), _server.controlCenterFragment);
                if (!mediaPlayerReady && !mediaPlayer.isPlaying()) { resetMusicPlayer(); }
                _server.clearMessageCounts();
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
                if (fromUser)
                {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                if (mediaPlayer.isPlaying() && !MediaLibraryHelper.isPaused)
                {
                    togglePlay(null);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if (!pauseOnButtonPush)
                {
                    togglePlay(null);
                }
            }
        });
    }

    private void togglePlay(ImageView pauseButton)
    {
        pauseOnButtonPush = pauseButton != null;
        Integer id = MediaLibraryHelper.togglePlay(mediaPlayer);
        if (id != null && pauseOnButtonPush) { pauseButton.setImageResource(id); }
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
        unDimAllElements();
    }

    public void disableAllElements()
    {
        disableElement(R.id.pauseButton);
        disableElement(R.id.stopButton);
        disableElement(R.id.skipButton);
        disableElement(R.id.songSeekBar);
        dimAllElements();
    }

    private void unDimElement(int elementId)
    {
        ImageView element = (ImageView)view.findViewById(elementId);
        element.clearColorFilter();
    }

    private void unDimAllElements()
    {
        unDimElement(R.id.pauseButton);
        unDimElement(R.id.stopButton);
        unDimElement(R.id.skipButton);
    }

    private void dimElement(int elementId)
    {
        ImageView element = (ImageView)view.findViewById(elementId);
        element.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
    }

    private void dimAllElements()
    {
        dimElement(R.id.pauseButton);
        dimElement(R.id.stopButton);
        dimElement(R.id.skipButton);
    }

    public void setCurrentSongPlaying(final String currentSongPlaying)
    {
        mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final TextView currentSong = (TextView) view.findViewById(R.id.currentSong);
                currentSong.setText(currentSongPlaying);

                final TextView songDuration = (TextView) view.findViewById(R.id.totalTime);
                setTime(songDuration, mediaPlayer.getDuration());

                final SeekBar songPosition = (SeekBar) view.findViewById(R.id.songSeekBar);
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

    private class handlePauseTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            togglePlay(button);
        }
    }

    private class handleStopTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            if (mediaPlayer.isPlaying())
            {
                togglePlay((ImageView)view.findViewById(R.id.pauseButton));
            }
            mediaPlayer.seekTo(0);
            setTime((TextView)view.findViewById(R.id.currentTime), 0);
            resetSeekBar();
        }
    }

    private class handleSkipTouch implements IFunction
    {
        @Override
        public void execute(ImageView button)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
            ((ImageView)view.findViewById(R.id.pauseButton)).setImageResource(R.drawable.pause_icon);
            MediaLibraryHelper.playNextSongInQueue(mediaPlayer, mainActivity.getApplicationContext(), _server.controlCenterFragment);
            _server.clearMessageCounts();
            if (!mediaPlayer.isPlaying()) { resetMusicPlayer(); }
            setTime((TextView)view.findViewById(R.id.currentTime), 0);
            resetSeekBar();
        }
    }
}

