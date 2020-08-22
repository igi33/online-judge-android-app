package com.example.onlinejudge.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.onlinejudge.R;
import com.example.onlinejudge.databinding.ActivityMainBinding;
import com.example.onlinejudge.ui.fragments.HomeFragment;
import com.example.onlinejudge.ui.fragments.SavedTasksFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isSavedTasksVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new HomeFragment())
                .commit();

        binding.changeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSavedTasksVisible) {
                    isSavedTasksVisible = false;
                    binding.changeFragment.setText("Saved Tasks");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, new HomeFragment())
                            .commit();
                } else {
                    isSavedTasksVisible = true;
                    binding.changeFragment.setText("Home");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, new SavedTasksFragment())
                            .commit();
                }
            }
        });
    }
}
