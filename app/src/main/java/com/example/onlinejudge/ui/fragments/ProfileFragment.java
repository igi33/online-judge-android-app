package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;

import com.example.onlinejudge.databinding.FragmentProfileBinding;
import com.example.onlinejudge.viewmodels.MainViewModel;
import com.example.onlinejudge.viewmodels.ProfileViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends BaseFragment {
    private static final String TAG = "ProfileFragment";
    private static final String ARG_USERID = "userId";

    private FragmentProfileBinding binding;
    private MainViewModel mainViewModel;
    private ProfileViewModel viewModel;
    private int userId;

    public ProfileFragment() {}

    public static ProfileFragment newInstance(int userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }
}
