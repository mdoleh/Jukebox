package com.doleh.Jukebox.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.doleh.Jukebox.R;

public class ConfigFragment extends Fragment
{
    private View view;
    private Activity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.config, container, false);
        FragmentHelper.loadBannerAds(view);
        mainActivity = getActivity();
        return view;
    }

}
