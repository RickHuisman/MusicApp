package com.rickhuisman.musicapp.models.acrcloud;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by rickh on 1/5/2018.
 */

public class Music {

    private String label;

    private String releaseDate;

    private ExternalMetadataSpotify externalMetadataSpotify;

    private Genres genres;

    public String getLabel() {
        return label;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public ExternalMetadataSpotify getExternalMetadata() {
        return externalMetadataSpotify;
    }

    public Genres getGenres() {
        return genres;
    }

    public void populate(JSONArray data) throws JSONException {
        // 0 for spotify data
        JSONObject parser = data.getJSONObject(0);

        label = parser.optString("label");
        releaseDate = parser.optString("release_date");

        genres = new Genres();
        genres.populate(parser.optJSONArray("genres"));

        externalMetadataSpotify = new ExternalMetadataSpotify();
        externalMetadataSpotify.populate(parser.optJSONObject("external_metadata").optJSONObject("spotify"));
    }

    public JSONObject toJSON() {

        JSONObject data = new JSONObject();

        try {
            data.put("label", label);
            data.put("releaseDate", releaseDate);
            data.put("externalMetadataSpotify", externalMetadataSpotify.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
