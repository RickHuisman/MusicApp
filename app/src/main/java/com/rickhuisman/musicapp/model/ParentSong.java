package com.rickhuisman.musicapp.model;

import com.rickhuisman.musicapp.model.acrcloud.Music;
import com.rickhuisman.musicapp.model.acrcloud.Spotify;

/**
 * Created by rickh on 1/10/2018.
 */

public class ParentSong {

    private Music music;

    private Spotify spotify;

    private String duration;

    private String size;

    private String filePath;

    public ParentSong() {
    }

    public ParentSong(Music music, Spotify spotify, String duration, String size, String filePath) {
        this.music = music;
        this.spotify = spotify;
        this.duration = duration;
        this.size = size;
        this.filePath = filePath;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Spotify getSpotify() {
        return spotify;
    }

    public void setSpotify(Spotify spotify) {
        this.spotify = spotify;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
