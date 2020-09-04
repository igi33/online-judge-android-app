package com.example.onlinejudge.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.onlinejudge.models.Submission;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.models.User;
import com.example.onlinejudge.repositories.OnlineJudgeRepository;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TaskViewModel extends ViewModel {
    private static final String TAG = "TaskViewModel";
    private final SavedStateHandle savedStateHandle;
    private OnlineJudgeRepository repository;

    private MutableLiveData<Task> task = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Submission>> submissions = new MutableLiveData<>();
    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    @ViewModelInject
    public TaskViewModel(OnlineJudgeRepository repository, @Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.repository = repository;
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public MutableLiveData<ArrayList<Submission>> getSubmissions() {
        return submissions;
    }

    public Observable<Task> observeTask(int id) {
        return repository.getTask(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<Submission>> observeBestSubmissions(int taskId, Map<String, Object> options) {
        return repository.getBestSubmissionsOfTask(taskId, options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public MutableLiveData<Task> getTask() {
        return task;
    }
}
