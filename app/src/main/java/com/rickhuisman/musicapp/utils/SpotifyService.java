package com.rickhuisman.musicapp.utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.rickhuisman.musicapp.model.acrcloud.Spotify;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by rickh on 1/6/2018.
 */

public class SpotifyService {

    private SpotifyServiceCallback callback;

    private String trackId;

    private Exception error;

    public SpotifyService(SpotifyServiceCallback callback) {
        this.callback = callback;
    }

    @SuppressLint("StaticFieldLeak")
    public void getSpotifyData(final String trackId) {
        this.trackId = trackId;


        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                String endpoint = "https://open.spotify.com/oembed?url=http://open.spotify.com/track/" + trackId;

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if (s == null && error != null) {
                    callback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);

                    Spotify spotify = new Spotify();
                    spotify.populate(data);
                    callback.serviceSuccess(spotify);

                } catch (JSONException e) {
                    callback.serviceFailure(e);
                }

            }

        }.execute(trackId);
    }
}
