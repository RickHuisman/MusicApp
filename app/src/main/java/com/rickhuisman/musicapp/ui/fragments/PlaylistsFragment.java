package com.rickhuisman.musicapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rickhuisman.musicapp.R;

/**
 * Created by rickh on 1/15/2018.
 */

public class PlaylistsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab_playlist_list, container, false);

        return root;
    }
}
