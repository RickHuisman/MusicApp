package com.rickhuisman.musicapp.models;

import android.media.Image;

/**
 * Created by rickh on 1/7/2018.
 */

public class LocalSong {
    private String title, artist, coverUrl, currentLocation, size;

    public LocalSong() {
    }

    public LocalSong(String title, String artist, String coverUrl, String currentLocation, String size) {
        this.title = title;
        this.artist = artist;
        this.coverUrl = coverUrl;
        this.currentLocation = currentLocation;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}
