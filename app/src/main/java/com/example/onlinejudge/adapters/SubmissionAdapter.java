package com.example.onlinejudge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinejudge.databinding.ListItemSubmissionBinding;
import com.example.onlinejudge.models.Submission;

import java.util.ArrayList;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder> {
    private Context mContext;
    private ArrayList<Submission> mList;
    private ListItemSubmissionBinding binding;

    public SubmissionAdapter(Context mContext, ArrayList<Submission> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public SubmissionAdapter.SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = ListItemSubmissionBinding.inflate(inflater, parent,false);
        return new SubmissionAdapter.SubmissionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmissionAdapter.SubmissionViewHolder holder, int position) {
        holder.itemBinding.textViewSubmissionId.setText(mList.get(position).getId()+"");
        holder.itemBinding.textViewTaskName.setText(mList.get(position).getTask().getName());
        holder.itemBinding.textViewLangName.setText(mList.get(position).getComputerLanguage().getName());
        holder.itemBinding.textViewUserName.setText(mList.get(position).getUser().getUsername());
        holder.itemBinding.textViewStatus.setText(mList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class SubmissionViewHolder extends RecyclerView.ViewHolder {
        private ListItemSubmissionBinding itemBinding;

        public SubmissionViewHolder(ListItemSubmissionBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public void updateList(ArrayList<Submission> updatedList) {
        mList = updatedList;
        notifyDataSetChanged();
    }

    public Submission getSubmissionAt(int position) {
        return mList.get(position);
    }
}
