package com.doleh.Jukebox.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.doleh.Jukebox.MainActivity;
import com.doleh.Jukebox.R;

public class RequestListFragment extends Fragment
{
    private View view;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.request_list, container, false);
        mainActivity = (MainActivity)getActivity();

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }
}
