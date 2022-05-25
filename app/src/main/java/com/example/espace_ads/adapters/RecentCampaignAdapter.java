package com.example.espace_ads.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.espace_ads.databinding.RecentCampaignLayoutBinding;
import com.example.espace_ads.models.LiveCampaignModel;
import com.example.espace_ads.models.RecentCampaignModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecentCampaignAdapter extends RecyclerView.Adapter<RecentCampaignAdapter.RecentCampaignViewHolder> {
    ArrayList<RecentCampaignModel> recentCampaignModelList;
    Context context;

    public RecentCampaignAdapter(ArrayList<RecentCampaignModel> recentCampaignModelList, Context context) {
        this.recentCampaignModelList = recentCampaignModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecentCampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecentCampaignLayoutBinding recentCampaignLayoutBinding = RecentCampaignLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);
        return new RecentCampaignViewHolder(recentCampaignLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentCampaignViewHolder holder, int position) {
        RecentCampaignModel recentCampaignModel = recentCampaignModelList.get(position);
        holder.binding.campaignName.setText(recentCampaignModel.getHeadline());
    }

    @Override
    public int getItemCount() {
        return recentCampaignModelList.size();
    }

    public static class RecentCampaignViewHolder extends RecyclerView.ViewHolder {

        RecentCampaignLayoutBinding binding;
        public RecentCampaignViewHolder(@NonNull RecentCampaignLayoutBinding recentCampaignLayoutBinding) {
            super(recentCampaignLayoutBinding.getRoot());
            binding = recentCampaignLayoutBinding;
        }
    }
    public void setRecentCampaignList(ArrayList<RecentCampaignModel> recentCampaignModelList) {
        this.recentCampaignModelList = recentCampaignModelList;
        notifyDataSetChanged();
    }
}
