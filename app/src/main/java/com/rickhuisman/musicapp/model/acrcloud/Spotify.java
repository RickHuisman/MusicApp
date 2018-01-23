package com.rickhuisman.musicapp.model.acrcloud;

import com.rickhuisman.musicapp.utils.JSONPopulator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rickh on 1/7/2018.
 */

public class Spotify implements JSONPopulator{

    private int thumbnailHeight;

    private int thumbnailWidth;

    private int height;

    private int width;

    private String thumbnailUrl;

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public void populate(JSONObject data) throws JSONException {
        thumbnailHeight = data.optInt("thumbnail_height");
        thumbnailWidth = data.optInt("thumbnail_width");
        height = data.optInt("height");
        width = data.optInt("width");
        thumbnailUrl = data.optString("thumbnail_url");

    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("thumbnailHeight", thumbnailHeight);
            data.put("thumbnailWidth", thumbnailWidth);
            data.put("height", height);
            data.put("width", width);
            data.put("thumbnailUrl", thumbnailUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
