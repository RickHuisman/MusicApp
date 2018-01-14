package com.rickhuisman.musicapp.uploadSong;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rickhuisman.musicapp.R;
import com.rickhuisman.musicapp.models.LocalSong;
import com.rickhuisman.musicapp.models.ParentSong;
import com.rickhuisman.musicapp.models.acrcloud.Music;
import com.rickhuisman.musicapp.models.acrcloud.Spotify;
import com.rickhuisman.musicapp.uploadSong.utils.RecyclerViewSongsAdapter;
import com.rickhuisman.musicapp.uploadSong.utils.SpotifyService;
import com.rickhuisman.musicapp.uploadSong.utils.SpotifyServiceCallback;
import com.rickhuisman.musicapp.uploadSong.utils.ACRCloudRecognizer;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by rickh on 1/4/2018.
 */


public class SelectSongFragment extends Fragment implements Step, BlockingStep, SpotifyServiceCallback {

    private final String TAG = "SelectSongFragment";

    private static final int MY_PERMISSION_REQUEST = 1;

    private List<LocalSong> localSongsList = new ArrayList<>();

    private RecyclerViewSongsAdapter mAdapter;

    private SpotifyService service;

    private Music music;

    private DataManager dataManager;

    private RecyclerView mRecyclerViewSongs;

    private ProgressDialog progressDialog;

    private AlertDialog alertDialog;

    private String acrcloudServiceSuccess = "";

    private Boolean fileSelected;

    private int selectedFileInt;

    private StepperLayout.OnNextClickedCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upload_song, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Select a song");

        fileSelected = false;

        mRecyclerViewSongs = (RecyclerView) root.findViewById(R.id.recycler_view_local_songs);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewSongs.setLayoutManager(mLayoutManager);
        mRecyclerViewSongs.setItemAnimator(new DefaultItemAnimator());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Trying to recognise the song");
        progressDialog.setCanceledOnTouchOutside(false);

        service = new SpotifyService(this);

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            setUpSongList();
        }
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

    public void setUpSongList() {
        getLocalSongs();

        if (localSongsList.size() > 0) {
            mAdapter = new RecyclerViewSongsAdapter(localSongsList, getContext());
            mRecyclerViewSongs.setAdapter(mAdapter);

            mAdapter.setListener(new RecyclerViewSongsAdapter.AdapterListener() {
                @Override
                public void onClick(int test) {
                    selectedFileInt = test;
                    fileSelected = true;
//                    callback.getStepperLayout().setNextButtonColor(getResources().getColor(R.color.colorPrimary));
                }
            });
        } else {
            Toast.makeText(getContext(), "You have no song in you storage to upload", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocalSongs() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri localSongUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(localSongUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songSize = songCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentDuration = songCursor.getString(songDuration);
                String currentLocation = songCursor.getString(songLocation);
                String currentSize = getFileSize(songCursor.getLong(songSize));

                LocalSong localSong = new LocalSong(currentTitle, currentArtist, "", currentLocation, currentDuration, currentSize);
                localSongsList.add(localSong);
            } while (songCursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        setUpSongList();
                    }
                } else {
                    Toast.makeText(getContext(), "No permission granted", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.0#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void findSongInfo(String songURI) {
        File file = new File(songURI);
        if(!file.exists()){
            file.mkdirs();
        } else {
            progressDialog.show();
            new RecThread(songURI).start();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(android.os.Message msg) {
            try {
                JSONObject data = new JSONObject(msg.obj.toString());

                JSONObject statusResults = data.optJSONObject("status");
                JSONObject metadataResults = data.optJSONObject("metadata");

                acrcloudServiceSuccess = statusResults.optString("msg");

                if (Objects.equals(acrcloudServiceSuccess, "Success")) {
                    music = new Music();
                    music.populate(metadataResults.optJSONArray("music"));

                    String trackId = music.getExternalMetadata().getTrack().getTrackId();
                    service.getSpotifyData(trackId);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    };

    @Override
    public void serviceSuccess(Spotify spotify) {
        setUpDialog(spotify);
    }

    @Override
    public void serviceFailure(Exception e) {
        Log.d(TAG, e.getMessage());
    }

    public void setUpDialog(final Spotify spotify) {
        if (Objects.equals(acrcloudServiceSuccess, "Success")) {
            progressDialog.cancel();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View root = getLayoutInflater().inflate(R.layout.dialog_show_track_result, null);

            ImageView mCoverImage = (ImageView) root.findViewById(R.id.image_view_cover);
            TextView mTitle = (TextView) root.findViewById(R.id.text_view_title);
            TextView mArtist = (TextView) root.findViewById(R.id.text_view_artist);

            SpannableStringBuilder strTitle = new SpannableStringBuilder("Title " + music.getExternalMetadata().getTrack().getTrackName());
            strTitle.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTitle.setText(strTitle);

            ArrayList<String> artistsList = music.getExternalMetadata().getArtists().getArtistNameList();
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

            Glide.with(this).load(spotify.getThumbnailUrl()).into(mCoverImage);

            builder.setTitle("Is this the song you want to upload?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ParentSong parentSong = new ParentSong(
                            music,
                            spotify,
                            localSongsList.get(selectedFileInt).getDuration(),
                            localSongsList.get(selectedFileInt).getSize(),
                            localSongsList.get(selectedFileInt).getCurrentLocation());

                    dataManager.saveData(parentSong);
                    callback.goToNextStep();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.cancel();
                }
            });

            builder.setView(root);
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        this.callback = callback;

        if (fileSelected) {
            String path = localSongsList.get(selectedFileInt).getCurrentLocation();
            findSongInfo(path);
        } else {
            callback.getStepperLayout().setCurrentStepPosition(0);
        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    class RecThread extends Thread {

        String path;

        RecThread(String uri) {
            this.path = uri;
        }

        public void run() {
            Map<String, Object> config = new HashMap<String, Object>();

            config.put("access_key", "ef41e1ef763dab4f8f32baa1828cf34c");
            config.put("access_secret", "c47yc0BU45j0yDELJrgJLo7DDLOsgUGb4rzZkxel");
            config.put("host", "identify-eu-west-1.acrcloud.com");
            config.put("debug", false);
            config.put("timeout", 5);

            ACRCloudRecognizer re = new ACRCloudRecognizer(config);
            String result = re.recognizeByFile(path, 10);

            try {
                Message msg = new Message();
                msg.obj = result;

                msg.what = 1;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
