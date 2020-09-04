package com.example.onlinejudge.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.onlinejudge.models.ComputerLanguage;
import com.example.onlinejudge.models.Submission;
import com.example.onlinejudge.models.SubmissionSkeleton;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.repositories.OnlineJudgeRepository;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SolutionFormViewModel extends ViewModel {
    private static final String TAG = "SolutionFormViewModel";
    private final SavedStateHandle savedStateHandle;
    private OnlineJudgeRepository repository;

    private MutableLiveData<Task> task = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ComputerLanguage>> languages = new MutableLiveData<>();

    @ViewModelInject
    public SolutionFormViewModel(OnlineJudgeRepository repository, @Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.repository = repository;
    }

    public MutableLiveData<Task> getTask() {
        return task;
    }
    public MutableLiveData<ArrayList<ComputerLanguage>> getLanguages() {
        return languages;
    }

    public Observable<ArrayList<ComputerLanguage>> observeComputerLanguages() {
        return repository.getComputerLanguages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Submission> postSubmission(int taskId, int langId, String sourceCode) {
        return repository.postSubmission(taskId, new SubmissionSkeleton(langId, sourceCode))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Task> observeTask(int id) {
        return repository.getTask(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
