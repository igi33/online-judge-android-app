package com.example.onlinejudge.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.onlinejudge.R;
import com.example.onlinejudge.databinding.FragmentSolutionFormBinding;
import com.example.onlinejudge.models.ComputerLanguage;
import com.example.onlinejudge.viewmodels.MainViewModel;
import com.example.onlinejudge.viewmodels.SolutionFormViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.INPUT_METHOD_SERVICE;

@AndroidEntryPoint
public class SolutionFormFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "SolutionFormFragment";
    private static final String ARG_TASKID = "taskId";

    private FragmentSolutionFormBinding binding;
    private MainViewModel mainViewModel;
    private SolutionFormViewModel viewModel;
    private int taskId;

    public SolutionFormFragment() {}

    public static SolutionFormFragment newInstance(int taskId) {
        SolutionFormFragment fragment = new SolutionFormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TASKID, taskId);
        fragment.setArguments(args);
        return fragment;
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
        binding = FragmentSolutionFormBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSolutionSubmit.setOnClickListener(this);

        viewModel = new ViewModelProvider(this).get(SolutionFormViewModel.class);
        binding.setViewModel(viewModel);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        ArrayAdapter<ComputerLanguage> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                new ArrayList<>());

        mainViewModel.setLoading(true);
        compositeDisposable.add(viewModel.observeComputerLanguages()
                .subscribe(computerLanguages -> {
                    adapter.clear();
                    adapter.addAll(computerLanguages);
                    adapter.notifyDataSetChanged();
                }, error -> { }
                ,() -> mainViewModel.setLoading(false)));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLang.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_solution_submit:
                // hide keyboard if active
                try {
                    View view = getActivity() != null ? getActivity().getWindow().getCurrentFocus() : null;
                    if (view != null && view.getWindowToken() != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } catch (Exception e) {}

                ComputerLanguage selectedLang = (ComputerLanguage)binding.spinnerLang.getSelectedItem();
                int langId = selectedLang.getId();
                String sourceCode = binding.editTextSourceCode.getText().toString();

                mainViewModel.setLoading(true);
                compositeDisposable.add(viewModel.postSubmission(taskId, langId, sourceCode)
                        .subscribe(
                                result -> {
                                    mainViewModel.setFragment(HomeFragment.newInstance(0, true));
                                },
                                error -> {
                                    Log.e(TAG, "observeLogin: " + error.getMessage());
                                    mainViewModel.setToastMessage("Couldn't submit solution. Please try again!");
                                },
                                () -> mainViewModel.setLoading(false)));
                break;
        }
    }
}
