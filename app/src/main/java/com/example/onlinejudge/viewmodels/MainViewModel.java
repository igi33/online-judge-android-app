package com.example.onlinejudge.viewmodels;

import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.onlinejudge.models.Tag;
import com.example.onlinejudge.models.User;
import com.example.onlinejudge.repositories.OnlineJudgeRepository;
import com.example.onlinejudge.ui.fragments.HomeFragment;
import com.example.onlinejudge.ui.fragments.LoginFragment;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private final SavedStateHandle savedStateHandle;
    private OnlineJudgeRepository repository;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> toastMessage = new MutableLiveData<>(null);
    private MutableLiveData<Fragment> fragment = new MutableLiveData<>(new HomeFragment());
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>(false);
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Tag>> tags = new MutableLiveData<>();
    private MutableLiveData<String> title = new MutableLiveData<>("Online Judge");

    @ViewModelInject
    public MainViewModel(OnlineJudgeRepository repository, @Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.repository = repository;
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public MutableLiveData<Boolean> isLoggedIn() {
        return isLoggedIn;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public MutableLiveData<Boolean> isLoading() {
        return isLoading;
    }
    public void setLoading(boolean isLoading) {
        this.isLoading.setValue(isLoading);
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }
    public void setToastMessage(String toastMessage) {
        this.toastMessage.setValue(toastMessage);
    }

    public MutableLiveData<Fragment> getFragment() {
        return fragment;
    }
    public void setFragment(Fragment fragment) {
        this.fragment.setValue(fragment);
    }

    public Observable<ArrayList<Tag>> observeTags() {
        return repository.getTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public MutableLiveData<ArrayList<Tag>> getTags() {
        return tags;
    }
}
