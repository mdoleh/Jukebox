package com.doleh.Jukebox;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created with IntelliJ IDEA.
 * User: Mohammad Doleh
 * Date: 10/19/13
 * Time: 2:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class MediaLibraryHelper
{
    public void getSongList(ContentResolver contentResolver, String songTitle, String songArtist)
    {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                // ...process entry...
            } while (cursor.moveToNext());
        }
    }
}
