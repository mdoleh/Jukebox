package com.doleh.Jukebox;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class MediaLibraryHelper
{
    public List getSongList(ContentResolver contentResolver, String songTitle, String songArtist)
    {
        // Setup parameters for query
        String[] projection = {"_ID"};
        String selector;
        if (!songTitle.equals("") && !songArtist.equals(""))
        {
            selector = "TITLE=%" + songTitle + "% AND ARTIST=%" + songArtist + "%";
        }
        else if (!songTitle.equals("") && songArtist.equals(""))
        {
            selector = "TITLE=%" + songTitle + "%";
        }
        else if (songTitle.equals("") && !songArtist.equals(""))
        {
            selector = "ARTIST=%" + songArtist + "%";
        }
        else
        {
            selector = "";
        }

        List songIds = new ArrayList();

        Uri externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(externalContentUri, projection, selector, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do {
                songIds.add(cursor.getLong(idColumn));
            } while (cursor.moveToNext());
        }
        return songIds;
    }

    public void playSong(long songId)
    {

    }
}
