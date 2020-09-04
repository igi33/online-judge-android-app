package com.example.onlinejudge.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.models.User;
import com.example.onlinejudge.repositories.OnlineJudgeRepository;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {
    private static final String TAG = "ProfileViewModel";
    private final SavedStateHandle savedStateHandle;
    private OnlineJudgeRepository repository;

    private MutableLiveData<ArrayList<Task>> solvedTasks = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();

    @ViewModelInject
    public ProfileViewModel(OnlineJudgeRepository repository, @Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.repository = repository;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public MutableLiveData<ArrayList<Task>> getSolvedTasks() {
        return solvedTasks;
    }

    public Observable<User> observeUser(int id) {
        return repository.getUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<Task>> observeSolvedTasks(int id, Map<String, Object> options) {
        return repository.getSolvedTasksByUser(id, options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
