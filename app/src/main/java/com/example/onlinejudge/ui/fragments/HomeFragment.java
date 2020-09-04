package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinejudge.R;
import com.example.onlinejudge.adapters.SubmissionAdapter;
import com.example.onlinejudge.adapters.TaskAdapter;
import com.example.onlinejudge.databinding.FragmentHomeBinding;
import com.example.onlinejudge.helpers.RecyclerItemClickListener;
import com.example.onlinejudge.models.Task;
import com.example.onlinejudge.viewmodels.HomeViewModel;
import com.example.onlinejudge.viewmodels.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    private static final String ARG_TAGID = "tagId";
    private static final String ARG_SHOW_SUBMISSIONS = "showSubmissions";

    private FragmentHomeBinding binding;
    private MainViewModel mainViewModel;
    private HomeViewModel viewModel;
    private TaskAdapter taskAdapter;
    private SubmissionAdapter submissionAdapter;
    private int tagId;
    private boolean showSubmissions;

    public HomeFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tagId ID of the task tag by which the listed tasks are filtered.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(int tagId) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAGID, tagId);
        args.putBoolean(ARG_SHOW_SUBMISSIONS, false);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tagId ID of the task tag by which the listed tasks are filtered.
     * @param showSubmissions If true, will show the submissions tab when the fragment loads
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(int tagId, boolean showSubmissions) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAGID, tagId);
        args.putBoolean(ARG_SHOW_SUBMISSIONS, showSubmissions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tagId = getArguments().getInt(ARG_TAGID);
            showSubmissions = getArguments().getBoolean(ARG_SHOW_SUBMISSIONS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setViewModel(viewModel);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        if (tagId > 0) {
            mainViewModel.setLoading(true);
            compositeDisposable.add(viewModel.observeTag(tagId)
                    .subscribe(tag -> {
                                mainViewModel.setLoading(false);
                                mainViewModel.setTitle("Tagged " + tag.getName());
                            },
                            error -> {
                                mainViewModel.setLoading(false);
                                Log.e(TAG, "observeTag: " + error.getMessage());
                                mainViewModel.getToastMessage().setValue("Error loading tag. Try again later!");
                            }));
        } else {
            mainViewModel.setTitle(getResources().getString(R.string.app_name));
        }

        binding.tabLayoutHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Map<String, Object> options = new HashMap<>();
                if (tagId > 0) {
                    options.put("tagId", tagId);
                }

                mainViewModel.setLoading(true);
                if (tab.getPosition() == 0) {
                    compositeDisposable.add(viewModel.observeTasks(options)
                            .subscribe(result -> {
                                        mainViewModel.setLoading(false);
                                        viewModel.getTasks().setValue(result);
                                    },
                                    error -> {
                                        mainViewModel.setLoading(false);
                                        Log.e(TAG, "observeTasks: " + error.getMessage());
                                        mainViewModel.getToastMessage().setValue("Error loading tasks. Try again later!");
                                    },
                                    () -> mainViewModel.setLoading(false)));
                    binding.recyclerViewHome.setAdapter(taskAdapter);
                } else {
                    compositeDisposable.add(viewModel.observeSubmissions(options)
                            .subscribe(result -> {
                                        mainViewModel.setLoading(false);
                                        viewModel.getSubmissions().setValue(result);
                                    },
                                    error -> {
                                        mainViewModel.setLoading(false);
                                        Log.e(TAG, "observeSubmissions: " + error.getMessage());
                                        mainViewModel.getToastMessage().setValue("Error loading submissions. Try again later!");
                                    }));
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

        binding.recyclerViewHome.setNestedScrollingEnabled(false);
        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewHome.setAdapter(taskAdapter);
        binding.recyclerViewHome.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), binding.recyclerViewHome, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (binding.tabLayoutHome.getTabAt(0).isSelected()) {
                            TextView tvId = view.findViewById(R.id.text_view_task_id);
                            int taskId = Integer.parseInt(tvId.getText().toString());
                            mainViewModel.setFragment(TaskFragment.newInstance(taskId));
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {}
                })
        );

        observeData();
        setUpItemTouchHelper();
        binding.tabLayoutHome.getTabAt(showSubmissions ? 1 : 0).select();
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (binding.tabLayoutHome.getTabAt(0).isSelected()) {
                    int swipedTaskPosition = viewHolder.getAdapterPosition();
                    Task task = taskAdapter.getTaskAt(swipedTaskPosition);
                    viewModel.insertSavedTask(task);
                    taskAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Task saved.",Toast.LENGTH_SHORT).show();
                } else {
                    submissionAdapter.notifyDataSetChanged();
                }
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
}
