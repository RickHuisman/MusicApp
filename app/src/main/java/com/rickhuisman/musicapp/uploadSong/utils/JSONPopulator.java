package com.rickhuisman.musicapp.uploadSong.utils;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONPopulator {
    void populate(JSONObject data) throws JSONException;

    JSONObject toJSON();
}