package com.rickhuisman.musicapp.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rickhuisman.musicapp.R;

public class LibraryFragment extends Fragment {

    View root;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_library, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager());

        mViewPager = (ViewPager) root.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return root;
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SongsFragment songsFragment = new SongsFragment();
                    return songsFragment;
                case 1:
                    PlaylistsFragment playlistsFragment = new PlaylistsFragment();
                    return playlistsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Songs";
                case 1:
                    return "Playlists";
            }
            return null;
        }
    }
}
