package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinejudge.R;
import com.example.onlinejudge.adapters.BestSubmissionAdapter;
import com.example.onlinejudge.auth.SessionManager;
import com.example.onlinejudge.databinding.FragmentTaskBinding;
import com.example.onlinejudge.databinding.FragmentTaskFormBinding;
import com.example.onlinejudge.models.Tag;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.models.TestCase;
import com.example.onlinejudge.viewmodels.MainViewModel;
import com.example.onlinejudge.viewmodels.TaskFormViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TaskFormFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "TaskFormFragment";
    private static final String ARG_TASKID = "taskId";

    private FragmentTaskFormBinding binding;
    private MainViewModel mainViewModel;
    private TaskFormViewModel viewModel;
    private int taskId; // if taskId == 0 => task create page, else task edit page

    @Inject
    SessionManager sessionManager;

    public TaskFormFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param taskId ID of the task tag by which the listed tasks are filtered.
     * @return A new instance of fragment HomeFragment.
     */
    public static TaskFormFragment newInstance(int taskId) {
        TaskFormFragment fragment = new TaskFormFragment();
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
        binding = FragmentTaskFormBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TaskFormViewModel.class);
        binding.setViewModel(viewModel);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        if (taskId != 0) {
            mainViewModel.setLoading(true);
            compositeDisposable.add(viewModel.observeTask(taskId)
                    .subscribe(
                            task -> {
                                viewModel.getTask().setValue(task);
                                viewModel.getAllTags().setValue(
                                        task.getTags().stream()
                                                .map(t -> t.getName())
                                                .collect(Collectors.joining(", "))
                                );
                                // will also set test case data in text inputs
                                viewModel.getNumberOfTestCases().setValue(task.getTestCases().size());
                            },
                            error -> {
                                mainViewModel.setToastMessage("Could not load task. Try again later!");
                            },
                            () -> mainViewModel.setLoading(false)
                    ));
        } else {
            viewModel.getTask().setValue(new Task());
        }

        binding.buttonTaskSubmit.setOnClickListener(this);

        // callback when user edits the number of test cases
        viewModel.getNumberOfTestCases().observe(getViewLifecycleOwner(), n -> {
            int nOld = binding.layoutTaskCasesForm.getChildCount();
            if (n != nOld) {
                if (n > nOld) {
                    // Adding rows
                    int rowsToAdd = n - nOld;
                    for (int i = 0; i < rowsToAdd; ++i) {
                        boolean populateTcData = taskId > 0 && nOld == 0 && i < viewModel.getTask().getValue().getTestCases().size();
                        TestCase tc = populateTcData ? viewModel.getTask().getValue().getTestCases().get(i) : null;

                        // input
                        TextInputLayout tilInput = new TextInputLayout(requireContext());
                        tilInput.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                        tilInput.setHint("Input " + (nOld + i + 1));

                        TextInputEditText tietInput = new TextInputEditText(tilInput.getContext());
                        tietInput.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tietInput.setGravity(Gravity.TOP);
                        tietInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        tietInput.setSingleLine(false);
                        tietInput.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                        tietInput.setLines(4);
                        tietInput.setText(tc != null ? tc.getInput() : "");
                        tilInput.addView(tietInput);

                        // output
                        TextInputLayout tilOutput = new TextInputLayout(requireContext());
                        tilOutput.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                        tilOutput.setHint("Output " + (nOld + i + 1));

                        TextInputEditText tietOutput = new TextInputEditText(tilOutput.getContext());
                        tietOutput.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tietOutput.setGravity(Gravity.TOP);
                        tietOutput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        tietOutput.setSingleLine(false);
                        tietOutput.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                        tietOutput.setLines(4);
                        tietOutput.setText(tc != null ? tc.getOutput() : "");
                        tilOutput.addView(tietOutput);

                        LinearLayout singleTcContainer = new LinearLayout(getContext());
                        singleTcContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        singleTcContainer.setOrientation(LinearLayout.HORIZONTAL);
                        singleTcContainer.addView(tilInput);
                        singleTcContainer.addView(tilOutput);

                        binding.layoutTaskCasesForm.addView(singleTcContainer);
                    }
                } else {
                    // Removing rows
                    int rowsToRemove = nOld - n;

                    for (int i = 0; i < rowsToRemove; ++i) {
                        LinearLayout ll = (LinearLayout)binding.layoutTaskCasesForm.getChildAt(n+i);
                        ll.removeAllViews();
                    }

                    binding.layoutTaskCasesForm.removeViews(n, rowsToRemove);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_task_submit:
                int numberOfTestCases = binding.layoutTaskCasesForm.getChildCount();
                if (numberOfTestCases == 0) {
                    mainViewModel.setToastMessage("The task must contain at least one test case!");
                    return;
                }
                Task task = viewModel.getTask().getValue();
                if (task != null) {
                    if (task.getName() == null || task.getName().isEmpty()) {
                        mainViewModel.setToastMessage("The task name cannot be empty!");
                        return;
                    }
                    if (task.getDescription() == null || task.getDescription().isEmpty()) {
                        mainViewModel.setToastMessage("The task description cannot be empty!");
                        return;
                    }
                    if (task.getTimeLimit() == 0) {
                        mainViewModel.setToastMessage("The task time limit cannot be zero!");
                        return;
                    }
                    if (task.getMemoryLimit() == 0) {
                        mainViewModel.setToastMessage("The task memory limit cannot be zero!");
                        return;
                    }

                    // collect test cases
                    ArrayList<TestCase> tcs = new ArrayList<>();
                    for (int i = 0; i < numberOfTestCases; ++i) {
                        LinearLayout ll = (LinearLayout)binding.layoutTaskCasesForm.getChildAt(i);
                        TextInputLayout til;
                        TextInputEditText tiet;

                        til = (TextInputLayout)(ll.getChildAt(0));
                        tiet = (TextInputEditText)(til.getEditText());
                        String input = tiet.getText().toString();

                        til = (TextInputLayout)(ll.getChildAt(1));
                        tiet = (TextInputEditText)(til.getEditText());
                        String output = tiet.getText().toString();

                        if (!input.isEmpty() || !output.isEmpty()) {
                            tcs.add(new TestCase(0, input, output));
                        }
                    }
                    task.setTestCases(tcs);

                    if (tcs.size() == 0) {
                        mainViewModel.setToastMessage("The task must contain at least one filled test case!");
                        return;
                    }

                    // proceed as task meets all validation criteria
                    if (taskId > 0) {
                        task.setId(taskId);
                    }
                    if (task.getOrigin() == null) {
                        task.setOrigin("");
                    }

                    // explode tags string and populate array of Tag models
                    ArrayList<Tag> tags = new ArrayList<>();
                    if (viewModel.getAllTags().getValue() != null) {
                        tags.addAll(Arrays.stream(viewModel.getAllTags().getValue().split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .map(s -> new Tag(0, s, ""))
                                .collect(Collectors.toCollection(ArrayList::new)));
                    }
                    task.setTags(tags);

                    mainViewModel.setLoading(true);
                    if (taskId > 0) {
                        // send edit request
                        compositeDisposable.add(viewModel.putTask(taskId, task)
                                .subscribe(
                                        result -> {
                                            mainViewModel.setTitle(task.getName());
                                            mainViewModel.setFragment(TaskFragment.newInstance(taskId));
                                        },
                                        error -> {},
                                        () -> mainViewModel.setLoading(false)
                                )
                        );
                    } else {
                        // send create request
                        compositeDisposable.add(viewModel.postTask(task)
                                .subscribe(
                                        t -> {
                                            mainViewModel.setTitle(t.getName());
                                            mainViewModel.setFragment(TaskFragment.newInstance(t.getId()));
                                        },
                                        error -> {},
                                        () -> mainViewModel.setLoading(false)
                                )
                        );
                    }
                }
                break;
        }
    }
}
