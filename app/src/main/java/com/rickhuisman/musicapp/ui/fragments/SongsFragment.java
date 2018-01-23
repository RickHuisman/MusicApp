package com.rickhuisman.musicapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rickhuisman.musicapp.R;
import com.rickhuisman.musicapp.adapter.SongsListAdapter;
import com.rickhuisman.musicapp.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {

    private final String TAG = "SongsFragment";

    private FirebaseDatabase realtimeDB;

    private SongsListAdapter mAdapter;

    private RecyclerView mSongsRV;

    private List<Song> songsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab_song_list, container, false);

        realtimeDB = FirebaseDatabase.getInstance();

        mSongsRV = (RecyclerView) root.findViewById(R.id.recycler_view_songs);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mSongsRV.setLayoutManager(mLayoutManager);
        mSongsRV.setItemAnimator(new DefaultItemAnimator());

        getSongList();

        return root;
    }

    public void getSongList() {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        realtimeDB.getReference("Music/Users/" + user + "/Songs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String location = postSnapshot.getValue().toString();

                    realtimeDB.getReference("Music/Public Songs/Songs/" + location).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, dataSnapshot.getValue().toString());

                            String title = dataSnapshot.child("TrackName").getValue().toString();
                            String artist = dataSnapshot.child("ArtistNameList").child("0").getValue().toString();
                            String imageUrl = dataSnapshot.child("ThumbnailUrl").getValue().toString();

                            songsList.add(new Song(title, artist, imageUrl));

                            mAdapter = new SongsListAdapter(songsList, getContext());
                            mSongsRV.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
