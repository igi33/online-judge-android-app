package com.example.onlinejudge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinejudge.databinding.ListItemTaskBinding;
import com.example.onlinejudge.databinding.ListItemTaskSolvedBinding;
import com.example.onlinejudge.models.Task;

import java.util.ArrayList;

public class SolvedTaskAdapter extends RecyclerView.Adapter<SolvedTaskAdapter.SolvedTaskViewHolder> {
    private Context mContext;
    private ArrayList<Task> mList;
    private ListItemTaskSolvedBinding binding;

    public SolvedTaskAdapter(Context mContext, ArrayList<Task> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public SolvedTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = ListItemTaskSolvedBinding.inflate(inflater, parent,false);
        return new SolvedTaskAdapter.SolvedTaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SolvedTaskViewHolder holder, int position) {
        holder.itemBinding.textViewTaskName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class SolvedTaskViewHolder extends RecyclerView.ViewHolder {
        private ListItemTaskSolvedBinding itemBinding;

        public SolvedTaskViewHolder(ListItemTaskSolvedBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public void updateList(ArrayList<Task> updatedList) {
        mList = updatedList;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        return mList.get(position);
    }
}
