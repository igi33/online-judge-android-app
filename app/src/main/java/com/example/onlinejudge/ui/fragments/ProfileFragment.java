package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinejudge.adapters.SolvedTaskAdapter;
import com.example.onlinejudge.databinding.FragmentProfileBinding;
import com.example.onlinejudge.viewmodels.MainViewModel;
import com.example.onlinejudge.viewmodels.ProfileViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends BaseFragment {
    private static final String TAG = "ProfileFragment";
    private static final String ARG_USERID = "userId";

    private FragmentProfileBinding binding;
    private MainViewModel mainViewModel;
    private ProfileViewModel viewModel;
    private SolvedTaskAdapter solvedTaskAdapter;
    private int userId;

    public ProfileFragment() {}

    public static ProfileFragment newInstance(int userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USERID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setViewModel(viewModel);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mainViewModel.setLoading(true);
        compositeDisposable.add(viewModel.observeUser(userId)
            .subscribe(
                user -> {
                    mainViewModel.setLoading(false);
                    viewModel.getUser().setValue(user);
                    mainViewModel.setTitle(user.getUsername());
                },
                error -> {
                    mainViewModel.setLoading(false);
                    mainViewModel.setToastMessage("Could not load user data. Try again later!");
                }
            )
        );

        solvedTaskAdapter = new SolvedTaskAdapter(getContext(), new ArrayList<>());
        binding.recyclerViewSolvedTasks.setNestedScrollingEnabled(false);
        binding.recyclerViewSolvedTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSolvedTasks.setAdapter(solvedTaskAdapter);

        mainViewModel.setLoading(true);
        compositeDisposable.add(viewModel.observeSolvedTasks(userId, new HashMap<>())
            .subscribe(
                tasks -> {
                    mainViewModel.setLoading(false);
                    viewModel.getSolvedTasks().setValue(tasks);
                },
                error -> {
                    mainViewModel.setLoading(false);
                    mainViewModel.setToastMessage("Could not load solved tasks. Try again later!");
                }
            )
        );

        viewModel.getSolvedTasks().observe(getViewLifecycleOwner(), tasks -> {
            Log.e(TAG, "solvedTasks->onChanged: " + tasks.size());
            solvedTaskAdapter.updateList(tasks);
        });
    }
}
