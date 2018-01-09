package com.rickhuisman.musicapp.models.acrcloud;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rickh on 1/6/2018.
 */

public class Artists {

    private String artistName;

    private String artistId;

    ArrayList<String> artistNameList = new ArrayList<String>();

    ArrayList<String> artistIdList = new ArrayList<String>();

    public String getArtistName() {
        return artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public ArrayList<String> getArtistNameList() {
        return artistNameList;
    }

    public ArrayList<String> getArtistIdList() {
        return artistIdList;
    }

    public void populate(JSONArray data) throws JSONException {
        try {
            for(int i = 0; i < data.length(); i++){
                JSONObject parser = data.getJSONObject(i);
                artistName = parser.getString("name");

                artistNameList.add(i, parser.getString("name"));
                artistIdList.add(i, parser.getString("id"));
            }
        } catch (Exception e) {
                System.out.print(e.getMessage());
        }
    }

    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("artistNameList", artistNameList);
            data.put("artistIdList", artistIdList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
