package com.example.reely;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {SavedReel.class},
        version = 1,
        exportSchema = false
)
public abstract class ReelyDatabase extends RoomDatabase {

    private static volatile ReelyDatabase INSTANCE;

    public static final ExecutorService databaseExecutor =
            Executors.newSingleThreadExecutor();

    public abstract SavedReelDao savedReelDao();

    public static ReelyDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ReelyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ReelyDatabase.class,
                            "reely_database"
                    ).build();
                }
            }
        }

        return INSTANCE;
    }
}