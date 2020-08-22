package com.example.onlinejudge.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.onlinejudge.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Query("DELETE FROM saved_tasks WHERE id = :taskId")
    void delete(int taskId);

    @Query("SELECT * FROM saved_tasks")
    LiveData<List<Task>> getAll();
}
