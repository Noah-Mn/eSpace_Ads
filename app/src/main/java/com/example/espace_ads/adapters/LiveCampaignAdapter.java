package com.example.espace_ads.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.espace_ads.databinding.LiveCampaignLayoutBinding;
import com.example.espace_ads.models.AdModel;
import com.example.espace_ads.models.LiveCampaignModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LiveCampaignAdapter extends RecyclerView.Adapter<LiveCampaignAdapter.LiveCampaignViewHolder> {

    ArrayList<LiveCampaignModel> liveCampaignModelList;
    FirebaseFirestore db;
    Context context;
    FirebaseUser currentUser;

    public LiveCampaignAdapter(ArrayList<LiveCampaignModel> liveCampaignModelList, Context context) {
        this.liveCampaignModelList = liveCampaignModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public LiveCampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LiveCampaignLayoutBinding liveCampaignLayoutBinding = LiveCampaignLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LiveCampaignViewHolder(liveCampaignLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveCampaignViewHolder holder, int position) {

        LiveCampaignModel liveCampaignModel = liveCampaignModelList.get(position);
        holder.binding.campaignName.setText(liveCampaignModel.getHeadline());
        holder.binding.campaignDetail.setText(liveCampaignModel.getPrimaryText());
    }

    @Override
    public int getItemCount() {
        return liveCampaignModelList.size();
    }

    public static class LiveCampaignViewHolder extends RecyclerView.ViewHolder {

        LiveCampaignLayoutBinding binding;
        public LiveCampaignViewHolder(@NonNull LiveCampaignLayoutBinding liveCampaignLayoutBinding) {
            super(liveCampaignLayoutBinding.getRoot());
            binding = liveCampaignLayoutBinding;
        }
    }

    public void setLiveCampaignList(ArrayList<LiveCampaignModel> liveCampaignModelList) {
        this.liveCampaignModelList = liveCampaignModelList;
        notifyDataSetChanged();
    }
}
