package com.example.onlinejudge.viewmodels;

import androidx.databinding.ObservableBoolean;
import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final SavedStateHandle savedStateHandle;

    @ViewModelInject
    public MainViewModel(@Assisted SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading.setValue(isLoading);
    }

    public MutableLiveData<Boolean> isLoading() {
        return isLoading;
    }
}
