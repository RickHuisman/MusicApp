package com.rickhuisman.musicapp.interfaces;

import com.rickhuisman.musicapp.model.ParentSong;

/**
 * Created by rickh on 1/7/2018.
 */

public interface DataManager {

    void saveData(ParentSong data);

    ParentSong getData();

}
