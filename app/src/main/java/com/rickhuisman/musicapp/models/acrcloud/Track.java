package com.rickhuisman.musicapp.models.acrcloud;

import com.rickhuisman.musicapp.uploadSong.utils.JSONPopulator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rickh on 1/6/2018.
 */

public class Track implements JSONPopulator{

    private String trackName;

    private String trackId;

    public String getTrackName() {
        return trackName;
    }

    public String getTrackId() {
        return trackId;
    }

    @Override
    public void populate(JSONObject data) throws JSONException {
        trackName = data.optString("name");

        trackId = data.optString("id");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("trackName", trackName);
            data.put("trackId", trackId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
