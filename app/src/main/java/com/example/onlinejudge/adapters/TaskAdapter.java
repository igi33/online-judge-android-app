package com.example.onlinejudge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinejudge.databinding.ListItemTaskBinding;
import com.example.onlinejudge.models.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context mContext;
    private ArrayList<Task> mList;
    private ListItemTaskBinding binding;

    public TaskAdapter(Context mContext, ArrayList<Task> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = ListItemTaskBinding.inflate(inflater, parent,false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        holder.itemBinding.textViewTaskId.setText(mList.get(position).getId()+"");
        holder.itemBinding.textViewTaskName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private ListItemTaskBinding itemBinding;

        public TaskViewHolder(ListItemTaskBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public  void updateList(ArrayList<Task> updatedList) {
        mList = updatedList;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        return mList.get(position);
    }
}
