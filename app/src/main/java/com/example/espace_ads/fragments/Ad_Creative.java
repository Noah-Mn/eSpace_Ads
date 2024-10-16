package com.example.espace_ads.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.espace_ads.AudioEditorClasses.RingdroidEditActivity;
import com.example.espace_ads.R;
import com.example.espace_ads.activityClasses.HomePage;
import com.example.espace_ads.algorithms.DocumentIDGenerator;
import com.example.espace_ads.audioTrimmer.AddAudio;
import com.example.espace_ads.models.Upload;
import com.example.espace_ads.models.VideoAdModel;
import com.example.espace_ads.videoTrimmer.TrimActivity;
import com.example.espace_ads.videoTrimmer.utils.CompressOption;
import com.example.espace_ads.videoTrimmer.utils.LogMessage;
import com.example.espace_ads.videoTrimmer.utils.TrimType;
import com.example.espace_ads.videoTrimmer.utils.TrimVideo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Ad_Creative extends Fragment {
    MaterialCardView media, slideShow, createVideo, saveBtn;
    TextInputEditText primaryText, headline, description, editTextWebsite, editTextBusiness_pro, editTextSocialMediaPro, editTextMobileApp;
    String encodedImage;
    String primText, hedl, descr, destn1, destn2, destn3, destn4;
    MaterialCheckBox website, businessProfile, mobileApplication, socialMediaProfile;
    FirebaseFirestore db;
    LinearLayout musicChooser;
    VideoView videoView;
    private Uri filepath, slideImagesUri, videoUri, audioUri;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int PICK_VIDEO_REQUEST = 1;
    private final int CHOOSE_SONG = 2;
    AppCompatImageView imagePreview;
    ProgressBar progressBar;
    StorageReference storageReference, StrReferenceSlideShow, StrVideoRef;
    DatabaseReference reference, dbReferenceSlideShow, dbReferenceVideo;
    private StorageTask uploadTask;
    private int uploadCount = 0;
    MediaController mediaController;
    FirebaseUser currentUser;
    ArrayList<Uri> imageList = new ArrayList<>();
    MediaPlayer mediaPlayer;
    VideoAdModel videoAdModel;
    String docID = DocumentIDGenerator.generateRandomString(20);
    private static final int STORAGE_PERMISSION_CODE = 150;
    private static final String TAG = "TrimActivity";

    final int Requestcode = 149;

    public static final String VideoUri = "VideoUri";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ActivityResultLauncher<Intent> videoTrimResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    videoUri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                    videoAdModel =new VideoAdModel(videoUri.toString());

                    Log.d(TAG, "Trimmed path:: " + videoUri);
                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(videoUri);
                    videoView.requestFocus();
                    videoView.setVisibility(View.VISIBLE);
                    videoView.start();

//                    Intent intent = new Intent(getContext(), HomePage.class);
//                    intent.putExtra("trimmedVideo", uri.toString());
//                    startActivity(intent);

//                    videoView.setOnPreparedListener(mediaPlayer -> {
//                        mediaController.setAnchorView(videoView);
//                    });
                    String filepath = String.valueOf(videoUri);
                    File file = new File(filepath);
                    long length = file.length();
                    Log.d(TAG, "Video size:: " + (length / 1024));
                } else
                    LogMessage.v("videoTrimResultLauncher data is null");
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_ad__creative, container, false);

        media = (MaterialCardView) inflatedView.findViewById(R.id.media);
        slideShow = (MaterialCardView) inflatedView.findViewById(R.id.slide_show);
        createVideo = (MaterialCardView) inflatedView.findViewById(R.id.create_video);
        primaryText = (TextInputEditText) inflatedView.findViewById(R.id.editText_primary_text);
        headline = (TextInputEditText) inflatedView.findViewById(R.id.editText_headline);
        description = (TextInputEditText) inflatedView.findViewById(R.id.editText_description);
        musicChooser = (LinearLayout) inflatedView.findViewById(R.id.music);

        editTextWebsite = (TextInputEditText) inflatedView.findViewById(R.id.editText_website);
        editTextBusiness_pro = (TextInputEditText) inflatedView.findViewById(R.id.editText_business_pro);
        editTextSocialMediaPro = (TextInputEditText) inflatedView.findViewById(R.id.editText_social_media_pro);
        editTextMobileApp = (TextInputEditText) inflatedView.findViewById(R.id.editText_mobile_app);

        website = (MaterialCheckBox) inflatedView.findViewById(R.id.website);
        businessProfile = (MaterialCheckBox) inflatedView.findViewById(R.id.business_profile);
        mobileApplication = (MaterialCheckBox) inflatedView.findViewById(R.id.mobile_application);
        socialMediaProfile = (MaterialCheckBox) inflatedView.findViewById(R.id.social_media);
        saveBtn = (MaterialCardView) inflatedView.findViewById(R.id.save_btn);
        progressBar = (ProgressBar) inflatedView.findViewById(R.id.progress_bar);
        imagePreview = inflatedView.findViewById(R.id.image_preview);
        storageReference = FirebaseStorage.getInstance().getReference("Single Image");
        StrReferenceSlideShow = FirebaseStorage.getInstance().getReference("Slide Show");
        dbReferenceSlideShow = FirebaseDatabase.getInstance().getReference("Slide Show");
        dbReferenceVideo = FirebaseDatabase.getInstance().getReference("Video");
        StrVideoRef = FirebaseStorage.getInstance().getReference("Video");
        videoView = (VideoView) inflatedView.findViewById(R.id.video_preview);
        mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        videoView.start();

//        videoView.setOnPreparedListener(mp -> {
//            float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
//            float screenRatio = videoView.getWidth() / (float)
//                    videoView.getHeight();
//            float scaleX = videoRatio / screenRatio;
//            if (scaleX >= 1f) {
//                videoView.setScaleX(scaleX);
//            } else {
//                videoView.setScaleY(1f / scaleX);
//            }
//        });

        if (getArguments() != null) {
            String value = getArguments().getString("trimmedVid");
            videoView.setVideoURI(Uri.parse(value));
            videoView.setVisibility(View.VISIBLE);
        }

        saveBtn.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload is in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }
            uploadSlideImages();
            /** ,,,,,,,,,,,,,,,,,,,,,,,,,,,......................................... */

            if (videoAdModel.getUri() != null) {

                String value = videoAdModel.getUri();
//            videoUri = Uri.parse(value);
                StorageReference fileReference = StrVideoRef.child(docID + ".mp4");

                uploadTask = fileReference.putFile(Uri.fromFile(new File(value)))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setProgress(0);
                                        progressBar.setVisibility(View.GONE);
                                        videoView.setVisibility(View.GONE);
                                        saveBtn.setVisibility(View.VISIBLE);
                                        saveBtn.setEnabled(false);
                                        saveBtn.setClickable(false);
                                    }
                                }, 500);
                                Toast.makeText(getContext(), "Video Upload successful", Toast.LENGTH_LONG).show();
                                Upload upload = new Upload(Objects.requireNonNull(headline.getText()).toString().trim(), taskSnapshot.getStorage().getDownloadUrl().toString());

//                            String uploadId = reference.push().getKey();
//                            reference.child(Objects.requireNonNull(uploadId)).setValue(upload);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                progressBar.setProgress((int) progress);
                            }
                        });
            } else {
                Toast.makeText(getContext(), "No file found!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            primText = Objects.requireNonNull(primaryText.getText()).toString();
            hedl = Objects.requireNonNull(headline.getText()).toString();
            descr = Objects.requireNonNull(description.getText()).toString();
            destn1 = Objects.requireNonNull(editTextWebsite.getText()).toString();
            destn2 = Objects.requireNonNull(editTextBusiness_pro.getText()).toString();
            destn3 = Objects.requireNonNull(editTextSocialMediaPro.getText()).toString();
            destn4 = Objects.requireNonNull(editTextMobileApp.getText()).toString();

            db = FirebaseFirestore.getInstance();
            Map<String, Object> Ad = new HashMap<>();

            Ad.put("Headline", hedl);
            Ad.put("Email Address", getEmail());
            Ad.put("Description", descr);
            Ad.put("Destination1 Website", destn1);
            Ad.put("Destination2 Business Pro", destn2);
            Ad.put("Destination3 Social Media Pro", destn3);
            Ad.put("Destination4 Mobile App", destn4);
            Ad.put("Primary Text", primText);
            Ad.put("Status", "Live");
            Ad.put("Start Date", "null");
            Ad.put("Start Time", "null");
            Ad.put("End Date", "null");
            Ad.put("End Time", "null");
            Ad.put("Location", "null");
            Ad.put("Gender", "null");
            Ad.put("Age", "null");
            Ad.put("Payment State", "Not Paid");

            db.collection("Advert")
                    .add(Ad)
                    .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save data", Toast.LENGTH_SHORT).show());
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // will edit later
            }
        });

        listeners();

        return inflatedView;
    }

    public void listeners() {


        media.setOnClickListener(view -> chooseImage());

        slideShow.setOnClickListener(view -> chooseMultipleImages());

        createVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), TrimActivity.class);
//                startActivity(intent);
                onMinToMaxTrimClicked();
            }
        });

        website.setOnClickListener(view -> {
            if (website.isChecked()) {
                editTextWebsite.setHint("Website URL");
                editTextWebsite.setVisibility(View.VISIBLE);
            } else {
                editTextWebsite.setVisibility(View.GONE);
            }
        });
        businessProfile.setOnClickListener(view -> {
            if (businessProfile.isChecked()) {
                editTextBusiness_pro.setHint("Business Profile URL");
                editTextBusiness_pro.setVisibility(View.VISIBLE);
            } else {
                editTextBusiness_pro.setVisibility(View.GONE);
            }
        });
        socialMediaProfile.setOnClickListener(view -> {
            if (socialMediaProfile.isChecked()) {
                editTextSocialMediaPro.setHint("Social Media Profile URL");
                editTextSocialMediaPro.setVisibility(View.VISIBLE);
            } else {
                editTextSocialMediaPro.setVisibility(View.GONE);
            }
        });

        musicChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                chooseSong();

                startActivity(new Intent(getContext(), AddAudio.class));
            }
        });

        mobileApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobileApplication.isChecked()) {
                    editTextMobileApp.setHint("Mobile Application URL");
                    editTextMobileApp.setVisibility(View.VISIBLE);
                } else {
                    editTextMobileApp.setVisibility(View.GONE);
                }
            }
        });

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("Please give permissions to see video,upload and download videos")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {

                            InputStream inputStream = (Objects.requireNonNull(getActivity())).getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            encodedImage = encodeImage(bitmap);

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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void chooseSong() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_SONG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            imagePreview.setVisibility(View.VISIBLE);
            musicChooser.setVisibility(View.VISIBLE);
            imagePreview.setImageURI(filepath);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getClipData() != null) {
            int countClipData = data.getClipData().getItemCount();
            int currentImageSelect = 0;
            while (currentImageSelect < countClipData) {

                slideImagesUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                imageList.add(slideImagesUri);
                currentImageSelect = currentImageSelect + 1;
            }
        }
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
//            String VideoUri = videoUri.getPath();
//            videoView.setVisibility(View.VISIBLE);
//            videoView.setVideoURI(videoUri);

//            Intent intent = new Intent(getContext(), TrimVideoActivty.class);
//            intent.putExtra(VideoUri, videoUri.toString());
//            startActivity(intent);
        }
        if (requestCode == CHOOSE_SONG && resultCode == RESULT_OK && data != null && data.getData() != null) {
            audioUri = data.getData();
            Intent intent = new Intent(getContext(), RingdroidEditActivity.class);
            intent.putExtra("audioUri", audioUri.toString());
            startActivity(intent);

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = (Objects.requireNonNull(getActivity())).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        reference = FirebaseDatabase.getInstance().getReference("Data");
        if (filepath != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(filepath));

            uploadTask = fileReference.putFile(filepath)
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
                            Upload upload = new Upload(Objects.requireNonNull(headline.getText()).toString().trim(), taskSnapshot.getStorage().getDownloadUrl().toString());

                            String uploadId = reference.push().getKey();
                            reference.child(Objects.requireNonNull(uploadId)).setValue(upload);
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
        } else {
            Toast.makeText(getContext(), "No file selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseMultipleImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadSlideImages() {
        for (uploadCount = 0; uploadCount < imageList.size(); uploadCount++) {

            Uri individualImage = imageList.get(uploadCount);
            StorageReference slideImages = StrReferenceSlideShow.child("Image" + individualImage.getLastPathSegment());

            slideImages.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    slideImages.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String UrI = String.valueOf(uri);
                            storeLink(UrI);
                        }
                    });
                }
            }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            });
            imageList.clear();
        }
    }

    private void storeLink(String urI) {
        DatabaseReference slideshowRef = dbReferenceSlideShow.child("slides");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ImgLink", urI);
        slideshowRef.push().setValue(hashMap);
//        Toast.makeText(getContext(), "Images uploaded successfully", Toast.LENGTH_SHORT).show();
    }

    private String getVideoExtension(Uri uri) {
        ContentResolver contentResolver = (Objects.requireNonNull(getActivity())).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public String getEmail() {
        String emailAddress;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }

    /** here goes the video trimming function*/


    ActivityResultLauncher<Intent> takeOrSelectVideoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Intent data = result.getData();
                    //check video duration if needed
           /*        if (TrimmerUtils.getDuration(this,data.getData())<=30){
                    Toast.makeText(this,"Video should be larger than 30 sec",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                    if (data.getData() != null) {
                        LogMessage.v("Video path:: " + data.getData());
                        openTrimActivity(String.valueOf(data.getData()));
                    } else {
                        Toast.makeText(getContext(), "video uri is null", Toast.LENGTH_SHORT).show();
                    }
                } else
                    LogMessage.v("takeVideoResultLauncher data is null");
            });

    private void openTrimActivity(String data) {

            TrimVideo.activity(data)
                    .setTrimType(TrimType.MIN_MAX_DURATION)
                    .setLocal("ar")
                    .setMinToMax(20, 30)
                    .start(this, videoTrimResultLauncher);

    }
    private void onMinToMaxTrimClicked() {
        showVideoOptions();
    }

    public void showVideoOptions() {
        try {
            BottomSheet.Builder builder = getBottomSheet();
            builder.sheet(R.menu.menu_video);
            builder.listener(item -> {
                if (R.id.action_take == item.getItemId())
                    captureVideo();
                else
                    openVideo();
                return false;
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BottomSheet.Builder getBottomSheet() {
        return new BottomSheet.Builder(Objects.requireNonNull(getActivity())).title(R.string.txt_option);
    }

    public void captureVideo() {
        try {
            Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
            intent.putExtra("android.intent.extra.durationLimit", 30);
            takeOrSelectVideoResultLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openVideo() {
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            takeOrSelectVideoResultLauncher.launch(Intent.createChooser(intent, "Select Video"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (isPermissionOk(grantResults))
            showVideoOptions();
    }

    private boolean checkCamStoragePer() {
        return checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    private boolean checkPermission(String... permissions) {
        boolean allPermitted = false;
        for (String permission : permissions) {
            allPermitted = (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), permission)
                    == PackageManager.PERMISSION_GRANTED);
            if (!allPermitted)
                break;
        }
        if (allPermitted)
            return true;
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), permissions,
                220);
        return false;
    }

    private boolean isPermissionOk(int... results) {
        boolean isAllGranted = true;
        for (int result : results) {
            if (PackageManager.PERMISSION_GRANTED != result) {
                isAllGranted = false;
                break;
            }
        }
        return isAllGranted;
    }



}