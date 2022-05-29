package com.example.espace_ads.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.BlogsAdapter;
import com.example.espace_ads.adapters.LiveCampaignAdapter;
import com.example.espace_ads.adapters.RecentCampaignAdapter;
import com.example.espace_ads.models.BlogsModel;
import com.example.espace_ads.models.LiveCampaignModel;
import com.example.espace_ads.models.RecentCampaignModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mig35.carousellayoutmanager.CarouselLayoutManager;

import java.util.ArrayList;

public class HomeFlag extends Fragment {

    RecyclerView liveCampaignRecycleView, recentCampaignRecyclerView, blogsRecyclerView;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    String name;
    MaterialTextView username, liveCampaign, recentCampaign;
    ProgressBar liveCampaignProgressBar, recentCampaignProgressBar;
    private final String TAG = "Home Fragment";
    ArrayList<LiveCampaignModel> liveCampaignModelList = new ArrayList<>();
    ArrayList<RecentCampaignModel> recentCampaignModelList = new ArrayList<>();
    ArrayList<BlogsModel> blogsModelArrayList = new ArrayList<>();
    View view;
    Spinner spinner;

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
        liveCampaign = view.findViewById(R.id.if_no_live);
        recentCampaign = view.findViewById(R.id.if_no_recent);
        liveCampaignProgressBar = view.findViewById(R.id.progress_live);
        recentCampaignProgressBar = view.findViewById(R.id.progress_recent);
        liveCampaignProgressBar.setVisibility(View.VISIBLE);
        recentCampaignProgressBar.setVisibility(View.VISIBLE);
        blogsRecyclerView = view.findViewById(R.id.blogs_list);
        spinner = view.findViewById(R.id.spinner);

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

        BlogsAdapter blogsAdapter = new BlogsAdapter(blogsModelArrayList, getContext());
        blogsRecyclerView.setHasFixedSize(true);
        blogsRecyclerView.setAdapter(blogsAdapter);

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
                            if (liveCampaignModelList.size() > 0) {
                                adapter.setLiveCampaignList(liveCampaignModelList);
                                liveCampaignProgressBar.setVisibility(View.GONE);
                                liveCampaign.setVisibility(View.GONE);
                            } else {
                                liveCampaign.setVisibility(View.VISIBLE);
                                liveCampaignProgressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
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
                            if (recentCampaignModelList.size() > 0) {
                                adapter1.setRecentCampaignList(recentCampaignModelList);
                                recentCampaignProgressBar.setVisibility(View.GONE);
                                recentCampaign.setVisibility(View.GONE);
                            } else {
                                recentCampaign.setVisibility(View.VISIBLE);
                                recentCampaignProgressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                    }
                });

        /**      <<<<<<<<<<<<<<<<<<<<<<<<<<get blogs somewhere here>>>>>>>>>>>>>>>>>>>    **/

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

    public void setSpinner(View view) {

        String[] accounts = new String[]{"Logout"};

        ArrayAdapter<String> accountsAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, accounts);
        accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(accountsAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    //logout
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String getEmail() {
        String emailAddress;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }

}