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
    List<RecentCampaignModel> recentCampaignModelList;
    FirebaseFirestore db;
    Context context;

    public RecentCampaignAdapter(List<RecentCampaignModel> recentCampaignModelList, Context context) {
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

        db = FirebaseFirestore.getInstance();
        db.collection("Advert")
//                .whereEqualTo("Status", "Recent")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String headline = documentSnapshot.getString("Headline");
                                String primaryText = documentSnapshot.getString("Primary Text");
                                holder.binding.campaignName.setText(headline);
//                                holder.binding.campaignDetail.setText(primaryText);
                            }
                        }else {
                            Toast.makeText(context, "Failed to get data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
}
