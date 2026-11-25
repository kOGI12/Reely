package com.example.reely;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedReelDao {

    @Insert
    long insert(SavedReel reel);

    @Query("SELECT * FROM saved_reels ORDER BY createdAt DESC, id DESC")
    List<SavedReel> getAll();

    @Nullable
    @Query("SELECT * FROM saved_reels WHERE id = :reelId LIMIT 1")
    SavedReel getById(long reelId);

    @Delete
    int delete(SavedReel reel);
}