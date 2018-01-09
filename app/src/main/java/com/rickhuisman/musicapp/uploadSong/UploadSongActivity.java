package com.rickhuisman.musicapp.uploadSong;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.rickhuisman.musicapp.R;
import com.rickhuisman.musicapp.models.acrcloud.Music;
import com.rickhuisman.musicapp.models.acrcloud.Spotify;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class UploadSongActivity extends AppCompatActivity implements DataManager {

    private final String TAG = "UploadSongActivity";

    private static final String CURRENT_STEP_POSITION_KEY = "position";

    private StepperLayout mStepperLayout;

    private static final String DATA = "data";

    private String mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_song);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int startingStepPosition = savedInstanceState != null ? savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY) : 0;

        mStepperLayout = (StepperLayout) findViewById(R.id.stepper_layout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this), startingStepPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        outState.putString(DATA, mData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void saveData(String data) {
        mData = data;
    }

    public String getData() {
        return mData;
    }
}
