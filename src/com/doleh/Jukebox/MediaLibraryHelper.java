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
    private static List<Song> songQueue = new ArrayList<Song>();
    public static boolean isPaused = false;

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

    private static Song getSongById(Long id, ContentResolver contentResolver)
    {
        // Setup parameters for query
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST};
        String selector = "_ID=" + id;

        Song song = new Song();

        Uri externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(externalContentUri, projection, selector, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            song.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            song.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            song.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        }
        return song;
    }

    public static void playRequest(Long songId, Context context, MediaPlayer mediaPlayer, ContentResolver contentResolver, Server server)
    {
        if (mediaPlayer.isPlaying() || isPaused || songQueue.size() > 0)
        {
            Song request = getSongById(songId, contentResolver);
            int index = songExistsInQueue(request);
            if (index != -1)
            {
                if (request.id != null) {
                    moveSongUp(request, index);
                    notifyDataSetUpdate(server);
                }
            }
            else
            {
                if (request.id != null) {
                    songQueue.add(request);
                    notifyDataSetUpdate(server);
                }
            }
        }
        else
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
            playSong(songId, context, mediaPlayer);
        }
    }

    public static void playNextSongInQueue(MediaPlayer mediaPlayer, Context context)
    {
        if (!songQueue.isEmpty())
        {
            Song nextSong = songQueue.get(0);
            songQueue.remove(0);
            playSong(nextSong.id, context, mediaPlayer);
        }
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
        }
    }

    public static String togglePlay(MediaPlayer mediaPlayer, Activity activity)
    {
        String text = null;
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
        songQueue = new ArrayList<Song>();
    }

    public static List<Song> getSongQueue()
    {
        return songQueue;
    }

    private static void notifyDataSetUpdate(final Server server)
    {
        server.mainActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                server.requestListFragment.requestListAdapter.notifyDataSetChanged();
            }
        });
    }
}
