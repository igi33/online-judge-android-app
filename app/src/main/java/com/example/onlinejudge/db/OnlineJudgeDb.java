package com.example.onlinejudge.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.onlinejudge.models.Task;

@Database(entities = {Task.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class OnlineJudgeDb extends RoomDatabase {
    public abstract TaskDao taskDao();
}
