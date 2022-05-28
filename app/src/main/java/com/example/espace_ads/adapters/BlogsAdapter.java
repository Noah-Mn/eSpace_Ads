package com.example.espace_ads.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.espace_ads.databinding.BlogLayoutBinding;
import com.example.espace_ads.models.BlogsModel;

import java.util.ArrayList;

public class BlogsAdapter extends RecyclerView.Adapter<BlogsAdapter.BlogsViewHolder> {

    ArrayList<BlogsModel> blogsModelArrayList;
    Context context;

    public BlogsAdapter(ArrayList<BlogsModel> blogsModelArrayList, Context context) {
        this.blogsModelArrayList = blogsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BlogsAdapter.BlogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BlogLayoutBinding blogLayoutBinding = BlogLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BlogsViewHolder(blogLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogsAdapter.BlogsViewHolder holder, int position) {

        BlogsModel blogsModel = blogsModelArrayList.get(position);
        holder.binding.campaignDetail.setText(blogsModel.getCampaignDetail());
        holder.binding.campaignLogo.setImageURI(Uri.parse(blogsModel.getCampaignLogo()));
        holder.binding.date.setText((CharSequence) blogsModel.getDate());
    }

    @Override
    public int getItemCount() {
        return blogsModelArrayList.size();
    }

    public static class BlogsViewHolder extends RecyclerView.ViewHolder {

        BlogLayoutBinding binding;

        public BlogsViewHolder(@NonNull BlogLayoutBinding blogLayoutBinding) {
            super(blogLayoutBinding.getRoot());
            binding = blogLayoutBinding;
        }
    }

    public void setBlogsList(ArrayList<BlogsModel> blogsList) {
        this.blogsModelArrayList = blogsList;
        notifyDataSetChanged();
    }
}
