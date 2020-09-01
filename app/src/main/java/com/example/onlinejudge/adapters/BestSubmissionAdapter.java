package com.example.onlinejudge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.onlinejudge.databinding.ListItemSubmissionBestBinding;
import com.example.onlinejudge.databinding.ListItemSubmissionBinding;
import com.example.onlinejudge.models.Submission;

import java.util.ArrayList;

public class BestSubmissionAdapter extends RecyclerView.Adapter<BestSubmissionAdapter.BestSubmissionViewHolder> {
    private Context mContext;
    private ArrayList<Submission> mList;
    private ListItemSubmissionBestBinding binding;

    public BestSubmissionAdapter(Context mContext, ArrayList<Submission> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public BestSubmissionAdapter.BestSubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ListItemSubmissionBestBinding binding = ListItemSubmissionBestBinding.inflate(inflater, parent, false);
        return new BestSubmissionAdapter.BestSubmissionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSubmissionViewHolder holder, int position) {
        holder.itemBinding.textViewIndex.setText((position+1)+".");
        holder.itemBinding.textViewLangName.setText(mList.get(position).getComputerLanguage().getName());
        holder.itemBinding.textViewUserName.setText(mList.get(position).getUser().getUsername());
        holder.itemBinding.textViewTime.setText((mList.get(position).getExecutionTime() / 1000.0) + " s");
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class BestSubmissionViewHolder extends RecyclerView.ViewHolder {
        private ListItemSubmissionBestBinding itemBinding;

        public BestSubmissionViewHolder(ListItemSubmissionBestBinding itemBinding) {
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
