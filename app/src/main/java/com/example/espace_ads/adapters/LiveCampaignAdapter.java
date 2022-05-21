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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LiveCampaignAdapter extends RecyclerView.Adapter<LiveCampaignAdapter.LiveCampaignViewHolder> {

    List<LiveCampaignModel> liveCampaignModelList = new ArrayList<>();
    FirebaseFirestore db;
    Context context;

    public LiveCampaignAdapter(List<LiveCampaignModel> liveCampaignModelList, Context context) {
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
        db = FirebaseFirestore.getInstance();
        db.collection("Advert")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String headline = documentSnapshot.getString("Headline");
                                String primaryText = documentSnapshot.getString("Primary text");
                                holder.binding.campaignName.setText(headline);
                                holder.binding.campaignDetail.setText(primaryText);
                            }
                        }else {
                            Toast.makeText(context, "Failed to get data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return liveCampaignModelList.size();
    }

    public class LiveCampaignViewHolder extends RecyclerView.ViewHolder {

        LiveCampaignLayoutBinding binding;
        public LiveCampaignViewHolder(@NonNull LiveCampaignLayoutBinding liveCampaignLayoutBinding) {
            super(liveCampaignLayoutBinding.getRoot());
            binding = liveCampaignLayoutBinding;
        }
    }
}
