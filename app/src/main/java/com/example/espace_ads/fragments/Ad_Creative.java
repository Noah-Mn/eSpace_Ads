package com.example.espace_ads.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.espace_ads.R;
import com.example.espace_ads.models.AdModel;
import com.example.espace_ads.models.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ad_Creative extends Fragment {
    MaterialCardView media, slideShow, createVideo, saveBtn;
    TextInputEditText primaryText, headline, description, destination;
    String encodedImage;
    String primText, hedl, descr, destn;
    MaterialRadioButton website, businessProfile, mobileApplication, socialMediaProfile;
    FirebaseFirestore db;
    AdModel adModel;
    private Uri filepath;
    private final int PICK_IMAGE_REQUEST = 22;
    AppCompatImageView imagePreview;
    ProgressBar progressBar;
    StorageReference storageReference;
    DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad__creative, container, false);

        media = (MaterialCardView) view.findViewById(R.id.media);
        slideShow = (MaterialCardView) view.findViewById(R.id.slide_show);
        createVideo = (MaterialCardView) view.findViewById(R.id.create_video);
        primaryText = (TextInputEditText) view.findViewById(R.id.editText_primary_text);
        headline = (TextInputEditText) view.findViewById(R.id.editText_headline);
        description = (TextInputEditText) view.findViewById(R.id.editText_description);
        destination = (TextInputEditText) view.findViewById(R.id.editText_destination);
        website = (MaterialRadioButton) view.findViewById(R.id.website);
        businessProfile = (MaterialRadioButton) view.findViewById(R.id.business_profile);
        mobileApplication = (MaterialRadioButton) view.findViewById(R.id.mobile_application);
        socialMediaProfile = (MaterialRadioButton) view.findViewById(R.id.social_media);
        saveBtn = (MaterialCardView) view.findViewById(R.id.save_btn);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        reference = FirebaseDatabase.getInstance().getReference("Single Image");
        imagePreview = view.findViewById(R.id.image_preview);
        storageReference = FirebaseStorage.getInstance().getReference("Single Image");

        adModel = new AdModel();
        setDestinationURL();
        listener();

        return view;
    }

    public void listener(){

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();

                primText = Objects.requireNonNull(primaryText.getText()).toString();
                hedl = Objects.requireNonNull(headline.getText()).toString();
                descr = Objects.requireNonNull(description.getText()).toString();
                destn = Objects.requireNonNull(destination.getText()).toString();
                adModel.setHeadline(hedl);

                db = FirebaseFirestore.getInstance();
                Map<String, Object> Ad = new HashMap<>();

                Ad.put("Headline", hedl);
                Ad.put("Description", descr);
                Ad.put("Destination", destn);
                Ad.put("Primary Text", primText);
                Ad.put("Status", "Live");
                Ad.put("Start Date", "null");
                Ad.put("Start Time", "null");
                Ad.put("End Date", "null");
                Ad.put("End Time", "null");
                Ad.put("Location", "null");
                Ad.put("Gender", "null");
                Ad.put("Age", "null");

                db.collection("Advert")
                        .add(Ad)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to save data", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                pickImage.launch(intent);
                chooseImage();
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (website.isChecked()) {
                    destination.setHint("Website URL");
                } else {
                    destination.setHint("Select destination URL");
                }
            }
        });
        businessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (businessProfile.isChecked()) {
                    destination.setHint("Business Profile URL");
                } else {
                    destination.setHint("Select destination URL");
                }
            }
        });
        socialMediaProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (socialMediaProfile.isChecked()) {
                    destination.setHint("Social Media Profile URL");
                } else {
                    destination.setHint("Select destination URL");
                }
            }
        });
        mobileApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobileApplication.isChecked()) {
                    destination.setHint("Mobile Application URL");
                } else {
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

    private void setDestinationURL() {
        if (website.isChecked()) {
            destination.setHint("Website URL");
        } else if (businessProfile.isChecked()) {
            destination.setHint("Business Profile URL");
        } else if (socialMediaProfile.isChecked()) {
            destination.setHint("Social Media Profile URL");
        } else if (mobileApplication.isChecked()) {
            destination.setHint("Mobile Application URL");
        } else {
            destination.setHint("Select destination URL");
        }
    }
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            imagePreview.setVisibility(View.VISIBLE);
            imagePreview.setImageURI(filepath);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = (getActivity()).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(){
        if (filepath != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(filepath));

            fileReference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                    progressBar.setVisibility(View.GONE);
                                    imagePreview.setVisibility(View.GONE);
                                }
                            }, 500);
                            Toast.makeText(getContext(), "Image Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(headline.getText().toString().trim(), taskSnapshot.getStorage().getDownloadUrl().toString());

                            String uploadId = reference.push().getKey();
                            reference.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        }else {
            Toast.makeText(getContext(), "No file selected!", Toast.LENGTH_SHORT).show();
        }
    }
}