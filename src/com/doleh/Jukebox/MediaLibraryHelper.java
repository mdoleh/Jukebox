package com.doleh.Jukebox;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaLibraryHelper
{
    private static List<Long> songQueue = new ArrayList<Long>();
    public static boolean isPaused = false;

    public static void removeSong(int index)
    {
        songQueue.remove(index);
    }

    public static Long getSongId(int index)
    {
        return songQueue.get(index);
    }

    public static boolean songQueueIsEmpty()
    {
        return songQueue.isEmpty();
    }

    public static List<Song> getSongList(ContentResolver contentResolver, String songTitle, String songArtist)
    {
        // Setup parameters for query
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST};
        String selector;
        if (!songTitle.equals("") && !songArtist.equals(""))
        {
            selector = "TITLE LIKE \"%" + songTitle + "%\" AND ARTIST LIKE \"%" + songArtist + "%\"";
        }
        else if (!songTitle.equals("") && songArtist.equals(""))
        {
            selector = "TITLE LIKE \"%" + songTitle + "%\"";
        }
        else if (songTitle.equals("") && !songArtist.equals(""))
        {
            selector = "ARTIST LIKE \"%" + songArtist + "%\"";
        }
        else
        {
            selector = "";
        }

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

    public static void playSong(Long songId, Context context, MediaPlayer mediaPlayer)
    {
        if (mediaPlayer.isPlaying() || isPaused)
        {
            if (isSongInQueue(songId)) { moveSongUp(songId); }
            else { songQueue.add(songId); }
        }
        else
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
            catch (IOException e)
            {
                // Unable to play song
            }
        }
    }

    public static String togglePlay(MediaPlayer mediaPlayer, Activity activity)
    {
        String text;
        if (!isPaused)
        {
            mediaPlayer.pause();
            text = activity.getString(R.string.playSong);
        }
        else
        {
            mediaPlayer.start();
            text = activity.getString(R.string.pauseSong);
        }
        isPaused = !isPaused;
        return text;
    }

    public static boolean isSongInQueue(Long id)
    {
        return songQueue.contains(id);
    }

    public static void moveSongUp(Long id)
    {
        int index = songQueue.indexOf(id);
        List<Long> temp = songQueue;
        temp.remove(index);
        if (index > 0) { --index; }
        if (temp.size() != 0) { temp.set(index, id); }
        else { temp.add(id); }
        songQueue = new ArrayList<Long>(temp);
    }
}
