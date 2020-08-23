package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;
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

import com.example.onlinejudge.adapters.TaskAdapter;
import com.example.onlinejudge.databinding.FragmentSavedTasksBinding;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.viewmodels.HomeViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedTasksFragment extends Fragment {
    private FragmentSavedTasksBinding binding;
    private HomeViewModel viewModel;
    private TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        initRecyclerView();
        setUpItemTouchHelper();
        observeData();
        viewModel.pullSavedTasks();
    }

    private void observeData() {
        viewModel.getSavedTasks().observe(getViewLifecycleOwner(), savedTasks -> {
            if (savedTasks == null || savedTasks.size() == 0) {
                binding.textViewNoSavedTasks.setVisibility(View.VISIBLE);
            } else {
                ArrayList<Task> list = new ArrayList<>(savedTasks);
                adapter.updateList(list);
            }
        });
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPokemonPosition = viewHolder.getAdapterPosition();
                Task task = adapter.getTaskAt(swipedPokemonPosition);
                viewModel.deleteSavedTask(task.getId());
                Toast.makeText(getContext(),"Task unsaved.",Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewSavedTasks);
    }

    private void initRecyclerView() {
        binding.recyclerViewSavedTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(getContext(), null);
        binding.recyclerViewSavedTasks.setAdapter(adapter);
    }
}
