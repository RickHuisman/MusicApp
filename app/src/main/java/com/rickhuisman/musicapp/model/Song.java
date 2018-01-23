package com.rickhuisman.musicapp.model;

/**
 * Created by rickh on 1/17/2018.
 */

public class Song {

    private String title, artist, imageUrl;

    public Song(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
