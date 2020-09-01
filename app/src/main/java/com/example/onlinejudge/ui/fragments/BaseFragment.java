package com.example.onlinejudge.ui.fragments;

import androidx.fragment.app.Fragment;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class BaseFragment extends Fragment {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onDestroyView() {
        compositeDisposable.clear();
        super.onDestroyView();
    }
}
