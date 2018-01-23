package com.rickhuisman.musicapp.model.acrcloud;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rickh on 1/6/2018.
 */

public class Genres {

    ArrayList<String> genresList = new ArrayList<String>();

    public ArrayList<String> getGenresList() {
        return genresList;
    }

    public void populate(JSONArray data) throws JSONException {
        try {
            for(int i = 0; i < data.length(); i++){
                JSONObject parser = data.getJSONObject(i);

                genresList.add(i, parser.getString("name"));
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("genresList", genresList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
