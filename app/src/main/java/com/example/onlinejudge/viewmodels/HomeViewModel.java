package com.example.onlinejudge.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.onlinejudge.models.Submission;
import com.example.onlinejudge.models.Tag;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.repositories.OnlineJudgeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    private final SavedStateHandle savedStateHandle;
    private OnlineJudgeRepository repository;

    private MutableLiveData<ArrayList<Task>> tasks = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Submission>> submissions = new MutableLiveData<>();
    private LiveData<List<Task>> savedTasks;

    @ViewModelInject
    public HomeViewModel(OnlineJudgeRepository repository, @Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.repository = repository;
        savedTasks = repository.getSavedTasks();
    }

    public MutableLiveData<ArrayList<Task>> getTasks() {
        return tasks;
    }

    public MutableLiveData<ArrayList<Submission>> getSubmissions() {
        return submissions;
    }

    public LiveData<List<Task>> getSavedTasks() {
        return savedTasks;
    }

    public Observable<ArrayList<Task>> observeTasks(Map<String, Object> options) {
        return repository.getTasks(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<Submission>> observeSubmissions(Map<String, Object> options) {
        return repository.getSubmissions(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Tag> observeTag(int id) {
        return repository.getTag(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
