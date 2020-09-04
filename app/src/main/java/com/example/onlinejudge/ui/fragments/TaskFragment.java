package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinejudge.R;
import com.example.onlinejudge.adapters.BestSubmissionAdapter;
import com.example.onlinejudge.auth.SessionManager;
import com.example.onlinejudge.databinding.FragmentTaskBinding;
import com.example.onlinejudge.models.Tag;
import com.example.onlinejudge.viewmodels.MainViewModel;
import com.example.onlinejudge.viewmodels.TaskViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TaskFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "TaskFragment";
    private static final String ARG_TASKID = "taskId";

    private FragmentTaskBinding binding;
    private MainViewModel mainViewModel;
    private TaskViewModel viewModel;
    private BestSubmissionAdapter submissionAdapter;
    private int taskId;

    @Inject
    SessionManager sessionManager;

    public TaskFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param taskId ID of the task tag by which the listed tasks are filtered.
     * @return A new instance of fragment HomeFragment.
     */
    public static TaskFragment newInstance(int taskId) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TASKID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getInt(ARG_TASKID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        binding.setViewModel(viewModel);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        viewModel.getCurrentUser().setValue(sessionManager.getUserDetails());

        mainViewModel.setLoading(true);
        compositeDisposable.add(viewModel.observeTask(taskId)
                .subscribe(
                        task -> {
                            mainViewModel.setLoading(false);
                            mainViewModel.setTitle(task.getName());
                            viewModel.getTask().setValue(task);
                            for (Tag tag : task.getTags()) {
                                Button button = new MaterialButton(requireActivity(), null, R.attr.textButton);
                                button.setText(tag.getName());
                                button.setOnClickListener(v -> {
                                    mainViewModel.setFragment(HomeFragment.newInstance(tag.getId()));
                                });
                                binding.chipGroupTags.addView(button);
                            }
                        },
                        error -> {
                            mainViewModel.setLoading(false);
                            mainViewModel.setToastMessage("Could not load task. Try again later!");
                        }
                ));

        submissionAdapter = new BestSubmissionAdapter(getContext(), new ArrayList<>());
        binding.recyclerViewTaskSubmissions.setNestedScrollingEnabled(false);
        binding.recyclerViewTaskSubmissions.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewTaskSubmissions.setAdapter(submissionAdapter);

        mainViewModel.setLoading(true);
        compositeDisposable.add(viewModel.observeBestSubmissions(taskId, new HashMap<String, Object>(){{
            put("limit", 10);
        }}).subscribe(
                submissionList -> {
                    mainViewModel.setLoading(false);
                    viewModel.getSubmissions().setValue(submissionList);
                },
                error -> {
                    mainViewModel.setLoading(false);
                    mainViewModel.setToastMessage("Could not load best submissions. Try again later!");
                }
        ));

        viewModel.getSubmissions().observe(getViewLifecycleOwner(), submissions -> {
            Log.e(TAG, "submissions->onChanged: " + submissions.size());
            submissionAdapter.updateList(submissions);
        });

        binding.buttonTaskEdit.setOnClickListener(this);
        binding.buttonTaskSubmittedBy.setOnClickListener(this);

        if (sessionManager.isLoggedIn()) {
            mainViewModel.getFabOnClickListener().setValue(v -> {
                mainViewModel.setFragment(SolutionFormFragment.newInstance(taskId));
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainViewModel.getFabOnClickListener().setValue(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_task_edit:
                mainViewModel.setFragment(TaskFormFragment.newInstance(taskId));
                break;
            case R.id.button_task_submitted_by:
                int userId = Integer.parseInt(v.getTag().toString());
                mainViewModel.setFragment(ProfileFragment.newInstance(userId));
                break;
        }
    }
}
