package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinejudge.adapters.SubmissionAdapter;
import com.example.onlinejudge.adapters.TaskAdapter;
import com.example.onlinejudge.databinding.FragmentHomeBinding;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.viewmodels.HomeViewModel;
import com.example.onlinejudge.viewmodels.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private MainViewModel mainViewModel;
    private HomeViewModel viewModel;
    private TaskAdapter taskAdapter;
    private SubmissionAdapter submissionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setViewModel(viewModel);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        binding.tabLayoutHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainViewModel.setLoading(true);
                if (tab.getPosition() == 0) {
                    viewModel.observeTasks(new HashMap<>())
                            .subscribe(result -> viewModel.getTasks().setValue(result),
                                    error -> {
                                        Log.e(TAG, "observeTasks: " + error.getMessage());
                                        mainViewModel.getToastMessage().setValue("Error loading tasks. Try again later!");
                                    },
                                    () -> mainViewModel.setLoading(false));
                    binding.recyclerViewHome.setAdapter(taskAdapter);
                } else {
                    viewModel.observeSubmissions(new HashMap<>())
                            .subscribe(result -> viewModel.getSubmissions().setValue(result),
                                    error -> {
                                        Log.e(TAG, "observeSubmissions: " + error.getMessage());
                                        mainViewModel.getToastMessage().setValue("Error loading submissions. Try again later!");
                                    },
                                    () -> mainViewModel.setLoading(false));
                    binding.recyclerViewHome.setAdapter(submissionAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
        taskAdapter = new TaskAdapter(getContext(), new ArrayList<>());
        submissionAdapter = new SubmissionAdapter(getContext(), new ArrayList<>());
        initRecyclerView();
        observeData();
        setUpItemTouchHelper();
        binding.tabLayoutHome.getTabAt(0).select();
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedTaskPosition = viewHolder.getAdapterPosition();
                Task task = taskAdapter.getTaskAt(swipedTaskPosition);
                viewModel.insertSavedTask(task);
                taskAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"Task saved.",Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewHome);
    }

    private void observeData() {
        viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            Log.e(TAG, "tasks->onChanged: " + tasks.size());
            taskAdapter.updateList(tasks);
        });
        viewModel.getSubmissions().observe(getViewLifecycleOwner(), submissions -> {
            Log.e(TAG, "submissions->onChanged: " + submissions.size());
            submissionAdapter.updateList(submissions);
        });

    }

    private void initRecyclerView() {
        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewHome.setAdapter(taskAdapter);
    }

    @Override
    public void onClick(View v) {
    }
}
