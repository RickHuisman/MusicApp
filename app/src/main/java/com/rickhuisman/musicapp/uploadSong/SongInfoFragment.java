package com.rickhuisman.musicapp.uploadSong;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rickhuisman.musicapp.R;
import com.rickhuisman.musicapp.models.acrcloud.Music;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

/**
 * Created by rickh on 1/4/2018.
 */

public class SongInfoFragment extends Fragment implements Step {

    // TODO

    TextView tv;

    public static SongInfoFragment newInstance() {
        return new SongInfoFragment();
    }

    private DataManager dataManager;
    // TODO


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_song_info, container, false);

        tv = (TextView)root.findViewById(R.id.textView2);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Upload the song");

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

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
        tv.setText("Entered text:\n" + dataManager.getData());
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }
}