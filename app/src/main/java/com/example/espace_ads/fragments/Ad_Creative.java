package com.example.espace_ads.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.espace_ads.R;
import com.example.espace_ads.models.AdModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class Ad_Creative extends Fragment {
    MaterialCardView media, slideShow, createVideo;
    TextInputEditText primaryText, headline, description, destination;
    String encodedImage;
    String primText, hedl, descr,destn;
    MaterialRadioButton website, businessProfile,mobileApplication, socialMediaProfile;
    AdModel adModel = new AdModel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad__creative, container, false);

        media = view.findViewById(R.id.media);
        slideShow = view.findViewById(R.id.slide_show);
        createVideo = view.findViewById(R.id.create_video);
        primaryText = view.findViewById(R.id.editText_primary_text);
        headline = view.findViewById(R.id.editText_headline);
        description = view.findViewById(R.id.editText_description);
        destination = view.findViewById(R.id.editText_destination);
        website = view.findViewById(R.id.website);
        businessProfile = view.findViewById(R.id.business_profile);
        mobileApplication = view.findViewById(R.id.mobile_application);
        socialMediaProfile = view.findViewById(R.id.social_media);

        setDestinationURL();
        getInfo();
        listeners();
        return view;
    }

    private void getInfo() {
        primText = Objects.requireNonNull(primaryText.getText()).toString();
        hedl = Objects.requireNonNull(headline.getText()).toString();
        descr = Objects.requireNonNull(description.getText()).toString();
        destn = Objects.requireNonNull(destination.getText()).toString();

        adModel.setPrimaryText(primText);
        adModel.setHeadline(hedl);
        adModel.setDescription(descr);
        adModel.setDestination(destn);


    }

    private void listeners() {
        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(website.isChecked()){
                    destination.setHint("Website URL");
                }else {
                    destination.setHint("Select destination URL");
                }
            }
        });
        businessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (businessProfile.isChecked()){
                    destination.setHint("Business Profile URL");
                }else {
                    destination.setHint("Select destination URL");
                }
            }
        });
        socialMediaProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (socialMediaProfile.isChecked()){
                    destination.setHint("Social Media Profile URL");
                }else {
                    destination.setHint("Select destination URL");
                }
            }
        });
        mobileApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobileApplication.isChecked()){
                    destination.setHint("Mobile Application URL");
                }else {
                    destination.setHint("Select destination URL");
                }
            }
        });
    }

    
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {

                            InputStream inputStream = (getActivity()).getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            encodedImage = encodeImage(bitmap);
                            adModel.setEncodedImage(encodedImage);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    private void setDestinationURL(){
        if(website.isChecked()){
            destination.setHint("Website URL");
        }else if (businessProfile.isChecked()){
            destination.setHint("Business Profile URL");
        }else if (socialMediaProfile.isChecked()){
            destination.setHint("Social Media Profile URL");
        }else if (mobileApplication.isChecked()){
            destination.setHint("Mobile Application URL");
        }else {
            destination.setHint("Select destination URL");
        }
    }
}