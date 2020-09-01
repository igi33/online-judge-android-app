package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinejudge.R;
import com.example.onlinejudge.auth.SessionManager;
import com.example.onlinejudge.databinding.FragmentLoginBinding;
import com.example.onlinejudge.helpers.JwtTokenInterceptor;
import com.example.onlinejudge.viewmodels.LoginViewModel;
import com.example.onlinejudge.viewmodels.MainViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.INPUT_METHOD_SERVICE;

@AndroidEntryPoint
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    private FragmentLoginBinding binding;
    private MainViewModel mainViewModel;
    private LoginViewModel viewModel;

    @Inject
    SessionManager sessionManager;
    @Inject
    JwtTokenInterceptor jwtTokenInterceptor;

    public LoginFragment() {}

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
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
                // hide keyboard if active
                try {
                    View view = getActivity() != null ? getActivity().getWindow().getCurrentFocus() : null;
                    if (view != null && view.getWindowToken() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } catch (Exception e) {}

                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                mainViewModel.setLoading(true);
                compositeDisposable.add(viewModel.observeLogin(username, password)
                        .subscribe(
                                result -> {
                                    sessionManager.createLoginSession(result);
                                    jwtTokenInterceptor.setToken(result.getToken());
                                    mainViewModel.isLoggedIn().setValue(true);
                                    mainViewModel.getUser().setValue(result);
                                    mainViewModel.setToastMessage("Welcome back, " + result.getUsername() + "!");
                                    mainViewModel.setFragment(HomeFragment.newInstance(0));
                                },
                                error -> {
                                    Log.e(TAG, "observeLogin: " + error.getMessage());
                                    mainViewModel.setToastMessage("Wrong username or password. Please try again!");
                                },
                                () -> mainViewModel.setLoading(false)));
                break;
        }
    }
}
