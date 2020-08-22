package com.example.onlinejudge.viewmodels;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.repositories.OnlineJudgeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TaskViewModel extends ViewModel {
    private static final String TAG = "TaskViewModel";
    private OnlineJudgeRepository repository;
    private MutableLiveData<ArrayList<Task>> tasks = new MutableLiveData<>();
    private LiveData<List<Task>> savedTasks;

    @ViewModelInject
    public TaskViewModel(OnlineJudgeRepository repository) {
        this.repository = repository;
        savedTasks = repository.getSavedTasks();
    }

    public MutableLiveData<ArrayList<Task>> getTasks() {
        return tasks;
    }

    public LiveData<List<Task>> getSavedTasks() {
        return savedTasks;
    }

    public void pullTasks(Map<String, Object> options) {
        repository.getTasks(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> tasks.setValue(result),
                        error-> Log.e(TAG, "pullTasks: " + error.getMessage()));
    }

    public void pullSavedTasks() {
        savedTasks = repository.getSavedTasks();
    }

    public void insertSavedTask(Task task) {
        repository.insertSavedTask(task);
    }
    public void deleteSavedTask(int taskId) {
        repository.deleteSavedTask(taskId);
    }
}
