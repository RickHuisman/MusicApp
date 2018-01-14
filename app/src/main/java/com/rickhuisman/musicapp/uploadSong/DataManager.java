package com.rickhuisman.musicapp.uploadSong;

import com.rickhuisman.musicapp.models.ParentSong;

/**
 * Created by rickh on 1/7/2018.
 */

public interface DataManager {

    void saveData(ParentSong data);

    ParentSong getData();

}
