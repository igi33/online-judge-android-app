package com.example.onlinejudge.repositories;

import androidx.lifecycle.LiveData;

import com.example.onlinejudge.api.OnlineJudgeApi;
import com.example.onlinejudge.db.TaskDao;
import com.example.onlinejudge.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import javax.inject.Inject;

public class OnlineJudgeRepository {
    private TaskDao taskDao;
    private OnlineJudgeApi apiService;

    @Inject
    public OnlineJudgeRepository(TaskDao taskDao, OnlineJudgeApi apiService) {
        this.taskDao = taskDao;
        this.apiService = apiService;
    }

    public Observable<ArrayList<Task>> getTasks(Map<String, Object> options) {
        return apiService.getTasks(options);
    }

    public void insertSavedTask(Task task) {
        taskDao.insert(task);
    }

    public void deleteSavedTask(int taskId) {
        taskDao.delete(taskId);
    }

    public LiveData<List<Task>> getSavedTasks() {
        return taskDao.getAll();
    }
}
