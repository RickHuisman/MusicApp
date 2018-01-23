package com.rickhuisman.musicapp.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;

import com.rickhuisman.musicapp.R;
import com.rickhuisman.musicapp.adapter.StepperAdapter;
import com.rickhuisman.musicapp.interfaces.DataManager;
import com.rickhuisman.musicapp.model.ParentSong;
import com.stepstone.stepper.StepperLayout;

public class UploadSongActivity extends AppCompatActivity implements DataManager {

    private final String TAG = "UploadSongActivity";

    private static final String CURRENT_STEP_POSITION_KEY = "position";

    private StepperLayout mStepperLayout;

    private ParentSong parentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_song);

        Toolbar toolbar = (Toolbar) findViewById(R.id.test);
        setSupportActionBar(toolbar);

        int startingStepPosition = savedInstanceState != null ? savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY) : 0;

        mStepperLayout = (StepperLayout) findViewById(R.id.stepper_layout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this), startingStepPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void saveData(ParentSong data) {
        parentSong = data;
    }

    public ParentSong getData() {
        return parentSong;
    }
}
