package com.rickhuisman.musicapp.model.acrcloud;

import com.rickhuisman.musicapp.utils.JSONPopulator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rickh on 1/6/2018.
 */

public class Album implements JSONPopulator{

    private String albumName;

    private String albumId;

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    @Override
    public void populate(JSONObject data) throws JSONException {
        albumName = data.optString("name");

        albumId = data.optString("id");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("albumName", albumName);
            data.put("albumId", albumId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
