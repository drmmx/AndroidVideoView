package com.drmmx.devmaks.androidvideoview.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

/**
 * Created by dev3rema
 */
@Database(entities = {Content.class}, version = 1)
public abstract class ContentDatabase extends RoomDatabase {

    public abstract ContentDao mContentDao();

    private static final Object sLock = new Object();
    private static ContentDatabase INSTANCE;

    /**
     * Singleton pattern
     */
    public static ContentDatabase getInstance(final Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ContentDatabase.class, "app.db")
                        /*.addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        getInstance(context).mContentDao().insertAll(Content.populateData(context));
                                    }
                                });
                            }
                        })*/
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }

}
