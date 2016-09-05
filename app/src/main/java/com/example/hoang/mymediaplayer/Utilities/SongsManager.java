package com.example.hoang.mymediaplayer.Utilities;

import android.os.Environment;

import com.example.hoang.mymediaplayer.Model.SongsModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hoang on 2/18/2016.
 */
public class SongsManager {
    // SDCard Path
    final String MEDIA_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/";
    private ArrayList<SongsModel> songsList = new ArrayList<SongsModel>();
    private String mp3Pattern = ".mp3";

    // Constructor
    public SongsManager() {

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public ArrayList<SongsModel> getPlayList() {
        System.out.println(MEDIA_PATH);
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return songsList;
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }

    private void addSongToList(File song) {
        if (song.getName().endsWith(mp3Pattern)) {
            SongsModel songMap = new SongsModel();
            songMap.setTitle(song.getName().substring(0, (song.getName().length() - 4)));
            songMap.setPath(song.getPath());

            // Adding each song to SongList
            songsList.add(songMap);
        }
    }
}