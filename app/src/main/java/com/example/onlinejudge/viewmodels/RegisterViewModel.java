package com.example.onlinejudge.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.onlinejudge.models.User;
import com.example.onlinejudge.models.UserCredentials;
import com.example.onlinejudge.repositories.OnlineJudgeRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {
    private static final String TAG = "RegisterViewModel";
    private final SavedStateHandle savedStateHandle;
    private OnlineJudgeRepository repository;

    @ViewModelInject
    public RegisterViewModel(OnlineJudgeRepository repository, @Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.repository = repository;
    }

    public Observable<User> observeRegister(String username, String email, String password) {
        UserCredentials userCred = new UserCredentials(username, password, email);
        return repository.postUser(userCred)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
