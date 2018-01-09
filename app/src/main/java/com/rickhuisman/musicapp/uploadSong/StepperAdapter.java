package com.rickhuisman.musicapp.uploadSong;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.rickhuisman.musicapp.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by rickh on 1/4/2018.
 */

public class StepperAdapter extends AbstractFragmentStepAdapter {

    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                final SelectSongFragment selectSongFragment = new SelectSongFragment();
                Bundle selectSongFragmentBundle = new Bundle();
                selectSongFragment.setArguments(selectSongFragmentBundle);
                return selectSongFragment;
            case 1:
                final SongInfoFragment songInfoFragment = new SongInfoFragment();
                Bundle songInfoFragmentBundle = new Bundle();
                songInfoFragment.setArguments(songInfoFragmentBundle);
                return songInfoFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context);
        switch (position) {
            case 0:
                builder.setEndButtonVisible(true);
                break;
            case 1:
                builder.setEndButtonLabel("Upload");
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }
}
