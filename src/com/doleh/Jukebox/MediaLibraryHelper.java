package com.doleh.Jukebox;

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
    public List<List> getSongList(ContentResolver contentResolver, String songTitle, String songArtist)
    {
        // Setup parameters for query
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE};
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

        List<Long> songIds = new ArrayList<Long>();
        List<String> songTitles = new ArrayList<String>();

        Uri externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(externalContentUri, projection, selector, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            do {
                songIds.add(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                songTitles.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            } while (cursor.moveToNext());
        }
        ArrayList<List> allInfo = new ArrayList<List>();
        allInfo.add(songIds);
        allInfo.add(songTitles);
        return allInfo;
    }

    public void playSong(Long songId, Context context)
    {
        try
        {
            Uri contentUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);

            MediaPlayer mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(context, contentUri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }
        catch (IOException e)
        {
            // shit's broke yo
        }
    }
}
