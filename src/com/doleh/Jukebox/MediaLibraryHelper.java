package com.doleh.Jukebox;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import com.doleh.Jukebox.Fragments.PlayerFragment;
import com.doleh.Jukebox.Fragments.RequestListFragment;
import com.doleh.Jukebox.Static.Config;
import com.doleh.Jukebox.Static.Email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaLibraryHelper
{
    private static List<Song> songQueue = new ArrayList<Song>();
    public static boolean isPaused = false;

    public static List<Song> getSongList(ContentResolver contentResolver, String songTitle, String songArtist)
    {
        // Setup parameters for query
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST};
        String selector;
        if (!songTitle.equals("") && !songArtist.equals(""))
        {
            selector = "TITLE LIKE \"%" + songTitle + "%\" AND ARTIST LIKE \"%" + songArtist + "%\" AND ";
        }
        else if (!songTitle.equals("") && songArtist.equals(""))
        {
            selector = "TITLE LIKE \"%" + songTitle + "%\" AND ";
        }
        else if (songTitle.equals("") && !songArtist.equals(""))
        {
            selector = "ARTIST LIKE \"%" + songArtist + "%\" AND ";
        }
        else
        {
            selector = "";
        }
        selector += "NOT " + MediaStore.Audio.Media.IS_MUSIC + " == 0";

        ArrayList<Song> allInfo = new ArrayList<Song>();

        Uri externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(externalContentUri, projection, selector, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            do {
                allInfo.add(new Song(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))));
            } while (cursor.moveToNext());
        }
        return allInfo;
    }

    public static void playRequest(Song requestedSong, Context context, MediaPlayer mediaPlayer, PlayerFragment playerFragment, RequestListFragment requestListFragment)
    {
        if (!Config.AUTO_PLAY || mediaPlayer.isPlaying() || isPaused || songQueue.size() > 0)
        {
            int index = songExistsInQueue(requestedSong);
            if (index != -1)
            {
                if (requestedSong.id != null) {
                    moveSongUp(requestedSong, index);
                    notifyDataSetUpdate(requestListFragment);
                }
            }
            else
            {
                if (requestedSong.id != null) {
                    songQueue.add(requestedSong);
                    notifyDataSetUpdate(requestListFragment);
                }
            }
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
            playSong(requestedSong.id, context, mediaPlayer);
            setCurrentSongPlaying(playerFragment, requestedSong);
            startUpdatingPlayerUI(playerFragment);
        }
    }

    public static void playNextSongInQueue(MediaPlayer mediaPlayer, Context context, PlayerFragment playerFragment, RequestListFragment requestListFragment)
    {
        if (!songQueue.isEmpty())
        {
            Song nextSong = songQueue.get(0);
            songQueue.remove(0);
            playSong(nextSong.id, context, mediaPlayer);
            isPaused = false;
            notifyDataSetUpdate(requestListFragment);
            setCurrentSongPlaying(playerFragment, nextSong);
            startUpdatingPlayerUI(playerFragment);
        }
    }

    private static void startUpdatingPlayerUI(PlayerFragment playerFragment)
    {
        playerFragment.stopUpdatingUI();
        playerFragment.startUpdatingUI();
    }

    private static void setCurrentSongPlaying(PlayerFragment playerFragment, Song song)
    {
        playerFragment.setCurrentSongPlaying(song.title + "-" + song.artist);
    }

    private static void playSong(Long songId, Context context, MediaPlayer mediaPlayer)
    {
        try
        {
            Uri contentUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, contentUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IOException ex)
        {
            // Unable to play song
            Email.sendErrorReport(ex);
        }
    }

    public static Integer togglePlay(MediaPlayer mediaPlayer)
    {
        Integer id = null;
        if (!isPaused)
        {
            mediaPlayer.pause();
            id = R.drawable.play_icon;
        }
        else
        {
            mediaPlayer.start();
            id = R.drawable.pause_icon;
        }
        isPaused = !isPaused;
        return id;
    }

    public static void moveSongUp(Song request, int index)
    {
        List<Song> temp = new ArrayList<Song>(songQueue);
        temp.remove(index);
        if (index > 0) { --index; }
        temp.add(index, request);
        songQueue = new ArrayList<Song>(temp);
    }

    private static int songExistsInQueue(Song request)
    {
        int index = 0;
        for (Song song: songQueue)
        {
            if (song.id.equals(request.id)) { return index; }
            ++index;
        }
        return -1;
    }

    public static void clearSongQueue()
    {
        songQueue.clear();
    }

    public static List<Song> getSongQueue()
    {
        return songQueue;
    }

    private static void notifyDataSetUpdate(final RequestListFragment requestListFragment)
    {
        requestListFragment.createViewableList(songQueue);
        requestListFragment.notifyDataSetChanged();
    }
}
