package com.doleh.Jukebox;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SongSearchFragment extends Fragment
{
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.song_search, container, false);
        setupButtonEventListener();
        hideActionBar();
        return view;
    }

    private void setupButtonEventListener()
    {
        final Button listenButton = ((Button)view.findViewById(R.id.searchButton));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSongRequest();
            }
        });
    }

    private void hideActionBar()
    {
        getActivity().getActionBar().hide();
    }

    private void showSongRequest()
    {
        Fragment fragment = new SongRequestFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.add(R.id.main, fragment, "song_request");
        transaction.addToBackStack("song_search");
        transaction.commit();
    }

    private void sendSearch()
    {
        // UI elements
        final EditText songTitle = (EditText)view.findViewById(R.id.songTitle);
        final EditText songArtist = (EditText)view.findViewById(R.id.songArtist);

        // Variables for data type and data
        String type;
        byte[][] request = new byte[2][];

        // Convert string input to bytes
        request[0] = songTitle.getText().toString().getBytes();
        request[1] = songArtist.getText().toString().getBytes();
        type = Constants.SONG_REQUEST_TYPE;

        // TODO: send search and show next screen if songList returned
    }

//    // TODO: probably belongs on song search, convert data and store in activity for song request
//    else if (payloadType.equals(Constants.SONG_LIST_TYPE))
//    {
//        List<Song> songList = new ArrayList<Song>();
//        try
//        {
//            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(payload[0]));
//            songList = (List<Song>)ois.readObject();
//            ois.close();
//        }
//        catch (ClassNotFoundException e)
//        {
//            showMessageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
//        }
//        catch (OptionalDataException e)
//        {
//            showMessageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
//        }
//        catch (StreamCorruptedException e)
//        {
//            showMessageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
//        }
//        catch (IOException e)
//        {
//            showMessageBox(getString(R.string.errorTitle), Arrays.toString(e.getStackTrace()));
//        }
//
//        //Create a list of songs for displaying to the user
//        viewableList = createSongListForSpinner(songList);
//
//        // Display list on UI
//        ArrayAdapter<String> songArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, viewableList);
//        songArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ((Spinner)findViewById(R.id.songListSpinner)).setAdapter(songArrayAdapter);
//    }
}
