package com.doleh.Jukebox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WiFiDirectFragment extends Fragment
{
    private Activity mainActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.wifi_direct, container, false);
        mainActivity = getActivity();
        return view;
    }
}
