package com.example.espace_ads.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.espace_ads.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateBusinessProfile extends Fragment {
    View view;
    LinearLayout socialMediaProfiles, socialMedia;
    TextInputEditText textCompanyName, textCompanyType, textCompanyDescription, companyURL, editTextFacebook, editTextTwitter, editTextInstagram, editTextLinkedin;
    MaterialCardView saveBusinessProfile, addProfileLinks, cardFacebook, cardInstagram, cardTwitter, cardLinkedin, editBusinessProfile;
    AppCompatImageView imageView;
    MaterialCheckBox facebook, twitter, instagram, linkedin;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser currentUser;
    private RoundedImageView roundedImageView, coverImage, companyLogoImage;
    private final int PICK_COVER_REQUEST = 1;
    private final int PICK_LOGO_REQUEST = 2;
    private ProgressBar mProgressBar;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask, uploadTask2;
    DatabaseReference reference;
    Uri coverImagePath, logoPath;
    StorageReference storageReference;


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
        companyLogoImage = view.findViewById(R.id.add_logo);
        textCompanyName = view.findViewById(R.id.text_company_name);
        textCompanyType = view.findViewById(R.id.text_company_type);
        textCompanyDescription = view.findViewById(R.id.company_description);
        companyURL = view.findViewById(R.id.company_url);
        saveBusinessProfile = view.findViewById(R.id.save_business_pro);
        imageView = view.findViewById(R.id.cover_pic);
        roundedImageView = view.findViewById(R.id.image_logo);
        facebook = view.findViewById(R.id.check_facebook);
        instagram = view.findViewById(R.id.check_instagram);
        twitter = view.findViewById(R.id.check_twitter);
        linkedin = view.findViewById(R.id.check_linkedin);
        addProfileLinks = view.findViewById(R.id.add_profile_links);
        editTextFacebook = view.findViewById(R.id.editText_facebook);
        editTextInstagram = view.findViewById(R.id.editText_instagram);
        editTextLinkedin = view.findViewById(R.id.editText_linkedin);
        editTextTwitter = view.findViewById(R.id.editText_twitter);
        cardFacebook = view.findViewById(R.id.facebook);
        cardInstagram = view.findViewById(R.id.instagram);
        cardLinkedin = view.findViewById(R.id.linkedin);
        cardTwitter = view.findViewById(R.id.twitter);
        mProgressBar = view.findViewById(R.id.progressBar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        editBusinessProfile = view.findViewById(R.id.edit_business_pro);
        storageReference = FirebaseStorage.getInstance().getReference("Business Profile Images").child(getEmail());

        listeners();

        firebaseFirestore.collection("Business Profile")
                .whereEqualTo("Email Address", getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String companyName = documentSnapshot.getString("Company Name");
                                String description = documentSnapshot.getString("Company Bio");
                                String companyType = documentSnapshot.getString("Company Type");
                                String companyUrl = documentSnapshot.getString("Company Url");
                                String companyCoverImage = documentSnapshot.getString("CoverImageUri");
                                String companyLogo = documentSnapshot.getString("LogoUri");
                                String facebookProfile = documentSnapshot.getString("Facebook Profile");
                                String linkedinProfile = documentSnapshot.getString("Linkedin Profile");
                                String instagramProfile = documentSnapshot.getString("Instagram Profile");
                                String twitterProfile = documentSnapshot.getString("Twitter Profile");

                                if (companyName != null && description != null && companyType != null && companyUrl != null && companyCoverImage != null && companyLogo != null) {

                                    try {
                                        URL coverUrl = new URL(companyCoverImage);
                                        Picasso.get().load(String.valueOf(coverUrl)).into(imageView);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        URL logoUrl = new URL(companyLogo);
                                        Picasso.get().load(String.valueOf(logoUrl)).into(roundedImageView);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    textCompanyName.setText(companyName);
                                    textCompanyType.setText(companyType);
                                    textCompanyDescription.setText(description);
                                    companyURL.setText(companyUrl);
                                    saveBusinessProfile.setVisibility(View.GONE);
                                    editBusinessProfile.setVisibility(View.VISIBLE);

                                    assert facebookProfile != null;
                                    if (!facebookProfile.matches("")) {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardFacebook.setVisibility(View.VISIBLE);
                                        facebook.setChecked(true);
                                        socialMediaProfiles.setVisibility(View.GONE);
                                    } else {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardFacebook.setVisibility(View.GONE);
                                        facebook.setChecked(false);
                                        socialMediaProfiles.setVisibility(View.GONE);
                                    }

                                    assert twitterProfile != null;
                                    if (!twitterProfile.matches("")) {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardTwitter.setVisibility(View.VISIBLE);
                                        twitter.setChecked(true);
                                        socialMediaProfiles.setVisibility(View.GONE);
                                    } else {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardTwitter.setVisibility(View.GONE);
                                        twitter.setChecked(false);
                                        socialMediaProfiles.setVisibility(View.GONE);
                                    }

                                    assert instagramProfile != null;
                                    if (!instagramProfile.matches("")) {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardInstagram.setVisibility(View.VISIBLE);
                                        instagram.setChecked(true);
                                        socialMediaProfiles.setVisibility(View.GONE);
                                    } else {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardInstagram.setVisibility(View.GONE);
                                        instagram.setChecked(false);
                                        socialMediaProfiles.setVisibility(View.GONE);
                                    }

                                    assert linkedinProfile != null;
                                    if (!linkedinProfile.matches("")) {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardLinkedin.setVisibility(View.VISIBLE);
                                        linkedin.setChecked(true);
                                        socialMediaProfiles.setVisibility(View.GONE);
                                    } else {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardLinkedin.setVisibility(View.GONE);
                                        linkedin.setChecked(false);
                                        socialMediaProfiles.setVisibility(View.GONE);
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

    private void listeners() {

        coverImage.setColorFilter(Color.WHITE);
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCoverImage();
            }
        });

        companyLogoImage.setColorFilter(Color.WHITE);
        companyLogoImage.setOnClickListener(new View.OnClickListener() {
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

        saveBusinessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemsToDatabase();
                setSocialMedia();
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
            coverImagePath = data.getData();
            imageView.setImageURI(coverImagePath);
        }

        if (requestCode == PICK_LOGO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            logoPath = data.getData();
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
        assert currentUser != null;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = (Objects.requireNonNull(getActivity())).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void setItemsToDatabase() {

        String companyName = Objects.requireNonNull(textCompanyName.getText()).toString().trim();
        String companyType = Objects.requireNonNull(textCompanyType.getText()).toString().trim();
        String companyBio = Objects.requireNonNull(textCompanyDescription.getText()).toString().trim();
        String companyUrl = Objects.requireNonNull(companyURL.getText()).toString().trim();
        String textFacebook = Objects.requireNonNull(editTextFacebook.getText()).toString().trim();
        String textInstagram = Objects.requireNonNull(editTextInstagram.getText()).toString().trim();
        String textLinkedin = Objects.requireNonNull(editTextLinkedin.getText()).toString().trim();
        String textTwitter = Objects.requireNonNull(editTextTwitter.getText()).toString().trim();


        reference = FirebaseDatabase.getInstance().getReference("Business Profile");
        if (coverImagePath != null && logoPath != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(coverImagePath));
            StorageReference fileReference2 = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(logoPath));

            uploadTask = fileReference.putFile(coverImagePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
////                                    progressBar.setProgress(0);
////                                    progressBar.setVisibility(View.GONE);
////                                    replaceFragments(new BusinessProfileStore());
//                                }
//                            }, 500);
//
//                            String uploadId = reference.push().getKey();
//                            reference.child(uploadId).setValue(upload);

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
//                            progressBar.setProgress((int) progress);
                        }
                    });

            uploadTask2 = fileReference2.putFile(logoPath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if (!task.isComplete()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task task) throws Exception {
                                if (!task.isComplete()) {
                                    throw Objects.requireNonNull(task.getException());
                                }
                                return fileReference2.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri logoDownloadUri = task.getResult();

                                    firebaseFirestore = FirebaseFirestore.getInstance();
                                    Map<String, Object> Item = new HashMap<>();

                                    Item.put("Company Name", companyName);
                                    Item.put("Company Type", companyType);
                                    Item.put("Company Bio", companyBio);
                                    Item.put("Company Url", companyUrl);
                                    Item.put("Email Address", getEmail());
                                    Item.put("CoverImageUri", downloadUri);
                                    Item.put("LogoUri", logoDownloadUri);
                                    Item.put("Facebook Profile", textFacebook);
                                    Item.put("Instagram Profile", textInstagram);
                                    Item.put("Linkedin Profile", textLinkedin);
                                    Item.put("Twitter Profile", textTwitter);
                                    Item.put("Visits", 0L);
                                    Item.put("Engagements", 0L);
                                    Item.put("Sells", 0L);

                                    firebaseFirestore.collection("Business Profile")
                                            .add(Item)
                                            .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save data", Toast.LENGTH_SHORT).show());

                                } else {
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "No file selected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setSocialMedia() {
        if (facebook.isChecked() && !Objects.requireNonNull(editTextFacebook.getText()).toString().matches("")) {
            socialMedia.setVisibility(View.VISIBLE);
            cardFacebook.setVisibility(View.VISIBLE);
            facebook.setChecked(true);
            socialMediaProfiles.setVisibility(View.GONE);
        } else {
            socialMedia.setVisibility(View.VISIBLE);
            cardFacebook.setVisibility(View.GONE);
            facebook.setChecked(false);
            socialMediaProfiles.setVisibility(View.GONE);
        }

        if (twitter.isChecked() && !Objects.requireNonNull(editTextTwitter.getText()).toString().matches("")) {
            socialMedia.setVisibility(View.VISIBLE);
            cardTwitter.setVisibility(View.VISIBLE);
            twitter.setChecked(true);
            socialMediaProfiles.setVisibility(View.GONE);
        } else {
            socialMedia.setVisibility(View.VISIBLE);
            cardTwitter.setVisibility(View.GONE);
            twitter.setChecked(false);
            socialMediaProfiles.setVisibility(View.GONE);
        }

        if (instagram.isChecked() && !Objects.requireNonNull(editTextInstagram.getText()).toString().matches("")) {
            socialMedia.setVisibility(View.VISIBLE);
            cardInstagram.setVisibility(View.VISIBLE);
            instagram.setChecked(true);
            socialMediaProfiles.setVisibility(View.GONE);
        } else {
            socialMedia.setVisibility(View.VISIBLE);
            cardInstagram.setVisibility(View.GONE);
            instagram.setChecked(false);
            socialMediaProfiles.setVisibility(View.GONE);
        }

        if (linkedin.isChecked() && !Objects.requireNonNull(editTextLinkedin.getText()).toString().matches("")) {
            socialMedia.setVisibility(View.VISIBLE);
            cardLinkedin.setVisibility(View.VISIBLE);
            linkedin.setChecked(true);
            socialMediaProfiles.setVisibility(View.GONE);
        } else {
            socialMedia.setVisibility(View.VISIBLE);
            cardLinkedin.setVisibility(View.GONE);
            linkedin.setChecked(false);
            socialMediaProfiles.setVisibility(View.GONE);
        }

    }

}