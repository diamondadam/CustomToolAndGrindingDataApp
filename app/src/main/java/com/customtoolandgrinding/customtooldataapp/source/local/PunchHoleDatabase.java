package com.customtoolandgrinding.customtooldataapp.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.customtoolandgrinding.customtooldataapp.models.PunchHole;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PunchHole.class}, version = 1, exportSchema = false)
public abstract class PunchHoleDatabase extends RoomDatabase {

    public abstract PunchHoleDao clockDao();

    private static volatile PunchHoleDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PunchHoleDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (PunchHoleDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PunchHoleDatabase.class, "punch_hole_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
