package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.MessageTypes.RequestSongList;
import com.doleh.Jukebox.R;

public class SongSearchFragment extends Fragment
{
    private View view;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.song_search, container, false);
        mainActivity = (MainActivity)getActivity();
        setupButtonEventListener();
        hideActionBar();
        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mainActivity.netComm.close();
    }

    private void setupButtonEventListener()
    {
        final Button listenButton = ((Button)view.findViewById(R.id.searchButton));
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSearch();
            }
        });
    }

    private void hideActionBar()
    {
        getActivity().getActionBar().hide();
    }

    public void showSongRequest()
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

        RequestSongList requestSongList = new RequestSongList(songTitle.getText().toString(), songArtist.getText().toString());
        mainActivity.netComm.write(requestSongList);
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
