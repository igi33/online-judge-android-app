package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinejudge.R;
import com.example.onlinejudge.adapters.SubmissionAdapter;
import com.example.onlinejudge.adapters.TaskAdapter;
import com.example.onlinejudge.auth.SessionManager;
import com.example.onlinejudge.databinding.FragmentHomeBinding;
import com.example.onlinejudge.databinding.FragmentLoginBinding;
import com.example.onlinejudge.helpers.JwtTokenInterceptor;
import com.example.onlinejudge.viewmodels.HomeViewModel;
import com.example.onlinejudge.viewmodels.LoginViewModel;
import com.example.onlinejudge.viewmodels.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    private FragmentLoginBinding binding;
    private MainViewModel mainViewModel;
    private LoginViewModel viewModel;

    @Inject
    SessionManager sessionManager;
    @Inject
    JwtTokenInterceptor jwtTokenInterceptor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonLogin.setOnClickListener(this);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding.setViewModel(viewModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                mainViewModel.setLoading(true);
                viewModel.observeLogin(username, password)
                        .subscribe(
                                result -> {
                                    sessionManager.createLoginSession(result);
                                    jwtTokenInterceptor.setToken(result.getToken());
                                    mainViewModel.isLoggedIn().setValue(true);
                                    mainViewModel.getUser().setValue(result);
                                    mainViewModel.setToastMessage("Welcome back, " + result.getUsername() + "!");
                                    mainViewModel.setFragment(new HomeFragment());
                                },
                                error -> {
                                    Log.e(TAG, "observeLogin: " + error.getMessage());
                                    mainViewModel.setToastMessage("Wrong username or password. Please try again!");
                                },
                                () -> mainViewModel.setLoading(false));
                break;
        }
    }
}
