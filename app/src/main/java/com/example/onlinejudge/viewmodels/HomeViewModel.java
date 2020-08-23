package com.example.onlinejudge.viewmodels;

import android.util.Log;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.onlinejudge.models.Submission;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.repositories.OnlineJudgeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    private final SavedStateHandle savedStateHandle;
    private OnlineJudgeRepository repository;
    private MutableLiveData<ArrayList<Task>> tasks = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Submission>> submissions = new MutableLiveData<>();
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private LiveData<List<Task>> savedTasks;

    @ViewModelInject
    public HomeViewModel(OnlineJudgeRepository repository, @Assisted SavedStateHandle savedStateHandle) {
        this.repository = repository;
        this.savedStateHandle = savedStateHandle;
        savedTasks = repository.getSavedTasks();
    }

    public MutableLiveData<ArrayList<Task>> getTasks() {
        return tasks;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<ArrayList<Submission>> getSubmissions() {
        return submissions;
    }

    public LiveData<List<Task>> getSavedTasks() {
        return savedTasks;
    }

    public void pullTasks(Map<String, Object> options) {
        repository.getTasks(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> tasks.setValue(result),
                        error -> {
                            Log.e(TAG, "pullTasks: " + error.getMessage());
                            toastMessage.setValue("Error loading tasks. Try again later!");
                        });
    }

    public void pullSubmissions(Map<String, Object> options) {
        repository.getSubmissions(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> submissions.setValue(result),
                        error -> {
                            Log.e(TAG, "pullSubmissions: " + error.getMessage());
                            toastMessage.setValue("Error loading submissions. Try again later!");
                        });
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
