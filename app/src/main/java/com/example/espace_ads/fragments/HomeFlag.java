package com.example.espace_ads.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.LiveCampaignAdapter;
import com.example.espace_ads.adapters.RecentCampaignAdapter;
import com.example.espace_ads.models.LiveCampaignModel;
import com.example.espace_ads.models.RecentCampaignModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mig35.carousellayoutmanager.CarouselLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFlag extends Fragment {

    RecyclerView liveCampaignRecycleView, recentCampaignRecyclerView;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    String name;
    MaterialTextView username;
    private final String TAG = "Home Fragment";
    ArrayList<LiveCampaignModel> liveCampaignModelList = new ArrayList<>();
    ArrayList<RecentCampaignModel> recentCampaignModelList = new ArrayList<>();
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_flag, container, false);
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        db = FirebaseFirestore.getInstance();
        liveCampaignRecycleView = view.findViewById(R.id.live_campaigns_list);
        recentCampaignRecyclerView = view.findViewById(R.id.recent_campaigns_list);
        MaterialCardView cardView = view.findViewById(R.id.business_profile_card);
        username = view.findViewById(R.id.text_name);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        getUserData();
//        cardView.setOnClickListener(view1 -> getFragmentManager().beginTransaction().remove(HomeFlag.this).commit());


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragments(new BusinessProfileStore());
            }
        });

        LiveCampaignAdapter adapter = new LiveCampaignAdapter(liveCampaignModelList, getContext());
        liveCampaignRecycleView.setHasFixedSize(true);
        liveCampaignRecycleView.setAdapter(adapter);

        RecentCampaignAdapter adapter1 = new RecentCampaignAdapter(recentCampaignModelList, getContext());
        recentCampaignRecyclerView.setHasFixedSize(true);
        recentCampaignRecyclerView.setAdapter(adapter1);

        db.collection("Advert")
                .whereEqualTo("Email Address", getEmail())
                .whereEqualTo("Status", "Live")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String headline = documentSnapshot.getString("Headline");
                                String primaryText = documentSnapshot.getString("Primary Text");
                                LiveCampaignModel liveCampaignModel = new LiveCampaignModel(headline, primaryText);
                                liveCampaignModelList.add(liveCampaignModel);
                            }
                            adapter.setLiveCampaignList(liveCampaignModelList);
                        } else {
                            Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        db.collection("Advert")
                .whereEqualTo("Email Address", getEmail())
                .whereEqualTo("Status", "Recent")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                RecentCampaignModel recentCampaignModel = new RecentCampaignModel();
                                String headline = documentSnapshot.getString("Headline");
                                recentCampaignModel.setHeadline(headline);
                                recentCampaignModelList.add(recentCampaignModel);

                            }
                            adapter1.setRecentCampaignList(recentCampaignModelList);
                        } else {
                            Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack("Home");
        fragmentTransaction.commit();
    }

    public void getUserData() {
        db.collection("Ad_Investors")
                .whereEqualTo("Email Address", getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                name = document.getString("Full Name");
                                username.setText(name);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public String getEmail() {
        String emailAddress;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }

}