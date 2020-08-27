package com.example.onlinejudge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinejudge.databinding.ListItemTagBinding;
import com.example.onlinejudge.models.Tag;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    private Context mContext;
    private ArrayList<Tag> mList;
    private ListItemTagBinding binding;

    public TagAdapter(Context mContext, ArrayList<Tag> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public TagAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = ListItemTagBinding.inflate(inflater, parent,false);
        return new TagAdapter.TagViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.TagViewHolder holder, int position) {
        holder.itemBinding.textViewTagName.setTag(mList.get(position).getId());
        holder.itemBinding.textViewTagName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        private ListItemTagBinding itemBinding;

        public TagViewHolder(ListItemTagBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public void updateList(ArrayList<Tag> updatedList) {
        mList = updatedList;
        notifyDataSetChanged();
    }

    public Tag getTagAt(int position) {
        return mList.get(position);
    }
}
