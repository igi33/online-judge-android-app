package com.example.onlinejudge.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.repositories.OnlineJudgeRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

public class TaskFormViewModel extends ViewModel {
    private static final String TAG = "TaskFormViewModel";
    private final SavedStateHandle savedStateHandle;
    private OnlineJudgeRepository repository;

    private MutableLiveData<Task> task = new MutableLiveData<>();
    private MutableLiveData<String> allTags = new MutableLiveData<>("");
    private MutableLiveData<Integer> numberOfTestCases = new MutableLiveData<>(0);

    @ViewModelInject
    public TaskFormViewModel(OnlineJudgeRepository repository, @Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.repository = repository;
    }

    public MutableLiveData<Task> getTask() {
        return task;
    }

    public MutableLiveData<String> getAllTags() {
        return allTags;
    }

    public MutableLiveData<Integer> getNumberOfTestCases() {
        return numberOfTestCases;
    }

    public Observable<Task> observeTask(int id) {
        return repository.getTask(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<Void>> putTask(int id, Task task) {
        return repository.putTask(id, task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Task> postTask(Task task) {
        return repository.postTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
