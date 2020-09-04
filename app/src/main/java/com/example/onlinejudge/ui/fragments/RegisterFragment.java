package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.onlinejudge.R;
import com.example.onlinejudge.databinding.FragmentRegisterBinding;
import com.example.onlinejudge.viewmodels.MainViewModel;
import com.example.onlinejudge.viewmodels.RegisterViewModel;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "RegisterFragment";
    private FragmentRegisterBinding binding;
    private MainViewModel mainViewModel;
    private RegisterViewModel viewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonRegister.setOnClickListener(this);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding.setViewModel(viewModel);
        mainViewModel.setTitle("Register");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                // hide keyboard if active
                try {
                    View view = getActivity() != null ? getActivity().getWindow().getCurrentFocus() : null;
                    if (view != null && view.getWindowToken() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } catch (Exception e) {}

                String username = binding.editTextUsername.getText().toString();
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                String confirmPassword = binding.editTextPasswordConfirm.getText().toString();

                if (!password.equals(confirmPassword)) {
                    mainViewModel.setToastMessage("Passwords do not match. Try again!");
                    return;
                }

                mainViewModel.setLoading(true);
                compositeDisposable.add(viewModel.observeRegister(username, email, password)
                        .subscribe(
                                result -> {
                                    mainViewModel.setLoading(false);
                                    mainViewModel.setFragment(LoginFragment.newInstance());
                                    mainViewModel.setToastMessage("Successfully registered as " + username + "!");
                                },
                                error -> {
                                    mainViewModel.setLoading(false);
                                    Log.e(TAG, "observeRegister: " + error.getMessage());
                                    mainViewModel.setToastMessage("Could not register. Please try again!");
                                }));
                break;
        }
    }
}