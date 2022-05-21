package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.LiveCampaignAdapter;
import com.example.espace_ads.models.LiveCampaignModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFlag extends Fragment {

    RecyclerView liveCampaignRecycleView;
    LiveCampaignAdapter liveCampaignAdapter;
    FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_flag, container, false);

        db = FirebaseFirestore.getInstance();
        liveCampaignRecycleView = view.findViewById(R.id.live_campaigns_list);
        MaterialCardView cardView = view.findViewById(R.id.business_profile_card);
//        cardView.setOnClickListener(view1 -> getFragmentManager().beginTransaction().remove(HomeFlag.this).commit());


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragments(new BusinessProfileStore());
            }
        });

        db.collection("Advert")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                List<LiveCampaignModel> liveCampaignModelList = new ArrayList<>();

                                String headline = documentSnapshot.getString("Headline");
                                String primaryText = documentSnapshot.getString("Primary Text");
                                LiveCampaignModel liveCampaignModel = new LiveCampaignModel();
                                liveCampaignModel.setHeadline(headline);
                                liveCampaignModel.setPrimaryText(primaryText);
                                liveCampaignModelList.add(liveCampaignModel);

                                if (liveCampaignModelList.size() > 0){
                                    LiveCampaignAdapter adapter = new LiveCampaignAdapter(liveCampaignModelList, getContext());
                                    liveCampaignRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    liveCampaignRecycleView.setAdapter(adapter);
                                }
                            }
                        }else {
                            Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }


    private void replaceFragments(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack("Home");
        fragmentTransaction.commit();
    }


}