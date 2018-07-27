package com.drmmx.devmaks.androidvideoview.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.net.Uri;

/**
 * Created by dev3rema
 */
@Entity(tableName = "content")
public class Content {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String background;

    private String firstVideo;

    private String secondVideo;

    public Content(String background, String firstVideo, String secondVideo) {
        this.background = background;
        this.firstVideo = firstVideo;
        this.secondVideo = secondVideo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getFirstVideo() {
        return firstVideo;
    }

    public void setFirstVideo(String firstVideo) {
        this.firstVideo = firstVideo;
    }

    public String getSecondVideo() {
        return secondVideo;
    }

    public void setSecondVideo(String secondVideo) {
        this.secondVideo = secondVideo;
    }
}
