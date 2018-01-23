package com.rickhuisman.musicapp.model.acrcloud;

import com.rickhuisman.musicapp.utils.JSONPopulator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rickh on 1/5/2018.
 */

public class ExternalMetadataSpotify implements JSONPopulator {

    private Album album;

    private Artists artists;

    private Track track;

    public Album getAlbum() {
        return album;
    }

    public Artists getArtists() {
        return artists;
    }

    public Track getTrack() {
        return track;
    }

    @Override
    public void populate(JSONObject data) throws JSONException {

        album = new Album();
        album.populate(data.optJSONObject("album"));

        artists = new Artists();
        artists.populate(data.optJSONArray("artists"));

        track = new Track();
        track.populate(data.optJSONObject("track"));
    }

    @Override
    public JSONObject toJSON() {

        JSONObject data = new JSONObject();
        try {
            data.put("album", album.toJSON());
            data.put("artist", artists.toJSON());
            data.put("track", track.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
