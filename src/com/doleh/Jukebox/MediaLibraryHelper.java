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
        String selector = "TITLE=%?% AND ARTIST=%?%";
        String[] selectionArgs = {songTitle, songArtist};

        List songIds = new ArrayList();

        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, projection, selector, selectionArgs, null);
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
}
