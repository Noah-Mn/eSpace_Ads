package com.example.espace_ads.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.GridAdapter;
import com.example.espace_ads.models.ItemsModel;
import com.example.espace_ads.models.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CreateBusinessProfile extends Fragment {
    View view;
    LinearLayout socialMediaProfiles, socialMedia;
    TextInputEditText textCompanyName, textCompanyType, textCompanyDescription, companyURL, editTextFacebook, editTextTwitter, editTextInstagram, editTextLinkedin;
    MaterialCardView addProfileLinks, addItems;
    ImageView imageView;
    MaterialCheckBox facebook, twitter, instagram, linkedin;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    private RoundedImageView roundedImageView, coverImage, companyLogo;
    private final int PICK_COVER_REQUEST = 1;
    private final int PICK_LOGO_REQUEST = 2;
    ArrayList<ItemsModel> items = new ArrayList<>();
    private ProgressBar mProgressBar;
    private ArrayList<Upload> mGridData;
//    private final String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";


    public CreateBusinessProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_business_profile, container, false);
        socialMedia = view.findViewById(R.id.social_media);
        socialMediaProfiles = view.findViewById(R.id.social_media_profiles);
        coverImage = view.findViewById(R.id.cover_image);
        companyLogo = view.findViewById(R.id.add_logo);
        textCompanyName = view.findViewById(R.id.text_company_name);
        textCompanyType = view.findViewById(R.id.text_company_type);
        textCompanyDescription = view.findViewById(R.id.company_description);
        companyURL = view.findViewById(R.id.company_url);
        addProfileLinks = view.findViewById(R.id.add_profile_links);
        imageView = view.findViewById(R.id.cover_pic);
        roundedImageView = view.findViewById(R.id.image_logo);
        facebook = view.findViewById(R.id.check_facebook);
        instagram = view.findViewById(R.id.check_instagram);
        twitter = view.findViewById(R.id.check_twitter);
        linkedin = view.findViewById(R.id.check_linkedin);
        editTextFacebook = view.findViewById(R.id.editText_facebook);
        editTextInstagram = view.findViewById(R.id.editText_instagram);
        editTextLinkedin = view.findViewById(R.id.editText_linkedin);
        editTextTwitter = view.findViewById(R.id.editText_twitter);
        addItems = view.findViewById(R.id.add_items);
        mProgressBar = view.findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();


        listeners();

        db.collection("Store Items")
                .whereEqualTo("Email Address", getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String name = documentSnapshot.getString("Item Name");
                                int price = Math.toIntExact(documentSnapshot.getLong("Item Price"));
                                String description = documentSnapshot.getString("Item Description");
                                String downloadUri = documentSnapshot.getString("DownloadUri");
                                ItemsModel itemsModel = new ItemsModel(name, price, description, downloadUri);
                                items.add(itemsModel);
                            }
                            if (items.size() > 0) {
//                                gridAdapter.setStoreItemsList(items);
                                mProgressBar.setVisibility(View.GONE);
//                                liveCampaign.setVisibility(View.GONE);
                            } else {
//                                liveCampaign.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.GONE);
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
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return view;
    }

    private void listeners() {
        
        coverImage.setColorFilter(Color.WHITE);
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCoverImage();
            }
        });

        companyLogo.setColorFilter(Color.WHITE);
        companyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseLogo();
            }
        });

        addProfileLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialMediaProfiles.setVisibility(View.VISIBLE);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facebook.isChecked()) {
                    editTextFacebook.setVisibility(View.VISIBLE);
                } else {
                    editTextFacebook.setVisibility(View.GONE);
                }
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instagram.isChecked()) {
                    editTextInstagram.setVisibility(View.VISIBLE);
                } else {
                    editTextInstagram.setVisibility(View.GONE);
                }
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twitter.isChecked()) {
                    editTextTwitter.setVisibility(View.VISIBLE);
                } else {
                    editTextTwitter.setVisibility(View.GONE);
                }
            }
        });

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linkedin.isChecked()) {
                    editTextLinkedin.setVisibility(View.VISIBLE);
                } else {
                    editTextLinkedin.setVisibility(View.GONE);
                }
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new AddItemToStore());
            }
        });
    }

    private void chooseLogo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_LOGO_REQUEST);
    }

    private void chooseCoverImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_COVER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_COVER_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri coverImagePath = data.getData();
            imageView.setImageURI(coverImagePath);
        }

        if (requestCode == PICK_LOGO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri logoPath = data.getData();
            roundedImageView.setImageURI(logoPath);
        }
    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack("Home");
        fragmentTransaction.commit();
    }

    public String getEmail() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String emailAddress;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }


}