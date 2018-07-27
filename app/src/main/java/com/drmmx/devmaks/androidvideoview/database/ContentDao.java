package com.drmmx.devmaks.androidvideoview.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by dev3rema
 */
@Dao
public interface ContentDao {

    @Query("SELECT * FROM content")
    List<Content> getContent();

    @Query("SELECT * FROM content WHERE   ID = (SELECT MAX(ID)  FROM content)")
    Content getLastContent();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Content content);

    @Update
    void update(Content content);

    @Insert
    void insertAll(Content... contents);

    @Delete
    void delete(Content content);

}
