package com.example.reely;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_reels")
public class SavedReel {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String topic = "";

    @NonNull
    public String category = "";

    @NonNull
    public String style = "";

    @NonNull
    public String filmingGuide = "";

    @NonNull
    public String caption = "";

    @NonNull
    public String hashtags = "";

    public long createdAt;
}