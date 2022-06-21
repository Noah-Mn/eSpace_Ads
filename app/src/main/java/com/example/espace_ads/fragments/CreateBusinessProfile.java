package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.espace_ads.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.makeramen.roundedimageview.RoundedImageView;

public class CreateBusinessProfile extends Fragment {

    View view;
    LinearLayout socialMediaProfiles, socialMedia;
    TextInputEditText textCompanyName, textCompanyType, textCompanyDescription, companyURL;
    MaterialCardView addProfileLinks;
    ImageView imageView;
    RoundedImageView roundedImageView, coverImage, companyLogo;

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


       listeners();
       return view;
    }

    private void listeners(){
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        companyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}