package com.example.tanushree.popularmovies.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by tanushree on 14/04/19.
 */

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;
    private static final String DATABASE_NAME = "movies";

    public static AppDatabase getInstance(Context context)
    {
        if(sInstance==null)
        {
            synchronized (LOCK)
            {
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
