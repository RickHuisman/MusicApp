package com.rickhuisman.musicapp.uploadSong.utils;

import com.rickhuisman.musicapp.models.acrcloud.Spotify;

/**
 * Created by rickh on 1/6/2018.
 */

public interface SpotifyServiceCallback {

    void serviceSuccess(Spotify spotify);

    void serviceFailure(Exception exception);
}
