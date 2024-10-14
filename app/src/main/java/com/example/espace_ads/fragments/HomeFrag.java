package com.example.espace_ads.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.espace_ads.R;
import com.example.espace_ads.activityClasses.LogIn;
import com.example.espace_ads.adapters.BlogsAdapter;
import com.example.espace_ads.adapters.LiveCampaignAdapter;
import com.example.espace_ads.adapters.RecentCampaignAdapter;
import com.example.espace_ads.models.BlogsModel;
import com.example.espace_ads.models.LiveCampaignModel;
import com.example.espace_ads.models.RecentCampaignModel;
import com.example.espace_ads.models.User;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.mig35.carousellayoutmanager.CarouselLayoutManager;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeFrag extends Fragment {

    RecyclerView liveCampaignRecycleView, recentCampaignRecyclerView, blogsRecyclerView;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    String name;
    MaterialTextView username, liveCampaign, numberVisits, numberEngagements, numberSells, recentCampaign;
    ProgressBar liveCampaignProgressBar, recentCampaignProgressBar;
    private final String TAG = "Home Fragment";
    ArrayList<LiveCampaignModel> liveCampaignModelList = new ArrayList<>();
    ArrayList<RecentCampaignModel> recentCampaignModelList = new ArrayList<>();
    ArrayList<BlogsModel> blogsModelArrayList = new ArrayList<>();
    View view;
    AppCompatImageView dropdown, coverImage;
    RoundedImageView logo;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_frag, container, false);
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
        dropdown = view.findViewById(R.id.drop_down);
        coverImage = view.findViewById(R.id.business_cover_image);
        logo = view.findViewById(R.id.profile_image);
        numberVisits = view.findViewById(R.id.text_visits_2);
        numberEngagements = view.findViewById(R.id.text_engagements_2);
        numberSells = view.findViewById(R.id.text_sells_2);
        user = new User();

        getUserData();
//        cardView.setOnClickListener(view1 -> getFragmentManager().beginTransaction().remove(HomeFrag.this).commit());

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), dropdown);
                popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.logout:

                                /**    logout to be implemented here   **/
//                                Toast.makeText(getContext(), "Logout", Toast.LENGTH_SHORT).show();
                                logout();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

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

        /**      <<<<<<<<<<<<<<<<<<<<<<<<<<get blogs from somewhere here>>>>>>>>>>>>>>>>>>>    **/


//        get business data from database

        db.collection("Business Profile")
                .whereEqualTo("Email Address", getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                int visitsNumber = Math.toIntExact(documentSnapshot.getLong("Visits"));
                                int sellsNumber = Math.toIntExact(documentSnapshot.getLong("Sells"));
                                int engagementsNumber = Math.toIntExact(documentSnapshot.getLong("Engagements"));

                                String companyCoverImage = documentSnapshot.getString("CoverImageUri");
                                String companyLogo = documentSnapshot.getString("LogoUri");

                                assert companyCoverImage != null;
                                if (!companyCoverImage.matches("")) {
                                    assert companyLogo != null;
                                    if (!companyLogo.matches("")) {

                                        try {
                                            URL coverUrl = new URL(companyCoverImage);
                                            if (getContext() != null) {
                                                Picasso.get().load(String.valueOf(coverUrl)).into(coverImage);
                                            }
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            URL logoUrl = new URL(companyLogo);
                                            if (getContext() != null) {
                                                Picasso.get().load(String.valueOf(logoUrl)).into(logo);
                                            }
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }

                                        numberVisits.setText(String.valueOf(visitsNumber));
                                        numberEngagements.setText(String.valueOf(engagementsNumber));
                                        numberSells.setText(String.valueOf(sellsNumber));

                                    }
                                }

                            }

                        } else {
                            Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                user.setFullName(name);
                                username.setText(user.getFullName());
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

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), LogIn.class));
    }

}