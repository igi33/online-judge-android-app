package com.example.onlinejudge.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.onlinejudge.R;
import com.example.onlinejudge.databinding.ActivityMainBinding;
import com.example.onlinejudge.ui.fragments.HomeFragment;
import com.example.onlinejudge.viewmodels.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private boolean isSavedTasksVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        /*
        binding.changeFragment.setOnClickListener(view -> {
            if (isSavedTasksVisible) {
                isSavedTasksVisible = false;
                binding.changeFragment.setText("Saved Tasks");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            } else {
                isSavedTasksVisible = true;
                binding.changeFragment.setText("Home");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SavedTasksFragment())
                        .commit();
            }
        });
        */

    }
}
