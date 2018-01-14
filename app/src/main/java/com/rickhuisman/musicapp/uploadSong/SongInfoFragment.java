package com.rickhuisman.musicapp.uploadSong;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rickhuisman.musicapp.MainActivity;
import com.rickhuisman.musicapp.R;
import com.rickhuisman.musicapp.models.ParentSong;
import com.rickhuisman.musicapp.models.acrcloud.Music;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rickh on 1/4/2018.
 */

public class SongInfoFragment extends Fragment implements Step, BlockingStep {

    private final String TAG = "SongInfoFragment";

    private DataManager dataManager;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    private String user;

    private StorageReference mStorageRef;

    private ProgressDialog progressDialog;

    private Map<String, Object> PublicSongsMap;

    private ImageView mCoverImage;

    private TextView mTitle;

    private TextView mArtist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_song_info, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Upload the song");

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading song and metadata");
        progressDialog.setCanceledOnTouchOutside(false);

        mCoverImage = (ImageView) root.findViewById(R.id.image_view_cover);
        mTitle = (TextView) root.findViewById(R.id.text_view_title);
        mArtist = (TextView) root.findViewById(R.id.text_view_artist);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DataManager) {
            dataManager = (DataManager) context;
        } else {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }

    public void uploadSong() {
        Uri file = Uri.fromFile(new File(PublicSongsMap.get("FilePath").toString()));
        StorageReference storageReference = mStorageRef.child("music/songs/" + PublicSongsMap.get("TrackId") + ".mp3");
        UploadTask uploadTask = storageReference.putFile(file);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);
        progressDialog.setTitle("Uploading song");
        progressDialog.show();

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Upload is " + String.valueOf(progress) + "% done");
                progressDialog.show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.setMessage("Upload is paused");
                progressDialog.show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Succes, uploaded song!");
                db.collection("Music").document("Public Songs").collection("Songs").document(PublicSongsMap.get("TrackId").toString())
                        .set(PublicSongsMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("Music").document("Users").collection(user).document("Songs").collection(PublicSongsMap.get("TrackId").toString()).document("Song")
                                        .set(PublicSongsMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(getContext(), MainActivity.class));
                                    }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                progressDialog.cancel();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        user = mAuth.getCurrentUser().getUid();

        PublicSongsMap = new HashMap<>();
        PublicSongsMap.put("AlbumId", dataManager.getData().getMusic().getExternalMetadata().getAlbum().getAlbumId());
        PublicSongsMap.put("AlbumName", dataManager.getData().getMusic().getExternalMetadata().getAlbum().getAlbumName());
        PublicSongsMap.put("ArtistIdList", dataManager.getData().getMusic().getExternalMetadata().getArtists().getArtistIdList());
        PublicSongsMap.put("ArtistNameList", dataManager.getData().getMusic().getExternalMetadata().getArtists().getArtistNameList());
        PublicSongsMap.put("TrackId", dataManager.getData().getMusic().getExternalMetadata().getTrack().getTrackId());
        PublicSongsMap.put("TrackName", dataManager.getData().getMusic().getExternalMetadata().getTrack().getTrackName());
        PublicSongsMap.put("GenreList", dataManager.getData().getMusic().getGenres().getGenresList());
        PublicSongsMap.put("Label", dataManager.getData().getMusic().getLabel());
        PublicSongsMap.put("ReleaseDate", dataManager.getData().getMusic().getReleaseDate());
        PublicSongsMap.put("ThumbnailHeight", dataManager.getData().getSpotify().getThumbnailHeight());
        PublicSongsMap.put("ThumbnailWidth", dataManager.getData().getSpotify().getThumbnailWidth());
        PublicSongsMap.put("ThumbnailUrl", dataManager.getData().getSpotify().getThumbnailUrl());
        PublicSongsMap.put("TrackDuration", dataManager.getData().getDuration());
        PublicSongsMap.put("TrackSize", dataManager.getData().getSize());
        PublicSongsMap.put("FilePath", dataManager.getData().getFilePath());
        PublicSongsMap.put("UploadedByUser", user);

        SpannableStringBuilder strTitle = new SpannableStringBuilder("Title " + dataManager.getData().getMusic().getExternalMetadata().getTrack().getTrackName());
        strTitle.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTitle.setText(strTitle);

        ArrayList<String> artistsList = dataManager.getData().getMusic().getExternalMetadata().getArtists().getArtistNameList();
        StringBuilder artistsString = new StringBuilder();

        for (int i = 0; i < artistsList.size(); i++) {
            if (i == 0) {
                artistsString.append("").append(artistsList.get(i));
            } else {
                artistsString.append(", ").append(artistsList.get(i));
            }
        }

        SpannableStringBuilder strArtists = new SpannableStringBuilder("Artists " + artistsString);
        strArtists.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mArtist.setText(strArtists);

        Glide.with(this).load(dataManager.getData().getSpotify().getThumbnailUrl()).into(mCoverImage);
    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        uploadSong();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }
}