package com.example.espace_ads.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.espace_ads.R;
import com.example.espace_ads.algorithms.DocumentIDGenerator;
import com.example.espace_ads.models.ItemsModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AddItemToStore extends Fragment {

    View view;
    private MaterialTextView addItemPic;
    private ImageView itemPic;
    private TextInputEditText itemPrice, itemName, itemDesc;
    private MaterialCardView addItem;
    private final int PICK_ITEM_IMAGE = 3;
    private Uri itemImagePath;
    private ItemsModel itemsModel;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    private ProgressBar progressBar;
    FirebaseUser currentUser;
    String docID = DocumentIDGenerator.generateRandomString(20);


    public AddItemToStore() {
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
        view = inflater.inflate(R.layout.fragment_add_item_to_store, container, false);
        addItemPic = (MaterialTextView) view.findViewById(R.id.add_item_pic);
        itemPic = (ImageView) view.findViewById(R.id.item_pic);
        itemName = (TextInputEditText) view.findViewById(R.id.editText_item_name);
        itemPrice = (TextInputEditText) view.findViewById(R.id.editText_item_price);
        itemDesc = (TextInputEditText) view.findViewById(R.id.editText_item_desc);
        addItem = (MaterialCardView) view.findViewById(R.id.add_item);
        itemsModel = new ItemsModel();
        progressBar = (ProgressBar) view.findViewById(R.id.content_loading_progressbar);
        storageReference = FirebaseStorage.getInstance().getReference("Store Items Images").child(getEmail());

        listeners();
//        configCloudinary();

        return view;
    }

    private void listeners() {
        addItemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseItemImage();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if (!Objects.requireNonNull(itemPrice.getText()).toString().trim().matches("")
                        && !Objects.requireNonNull(itemName.getText()).toString().trim().matches("")
                        && !Objects.requireNonNull(itemDesc.getText()).toString().trim().matches("")) {
                    setItemsToDatabase();
                } else {
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    addItem.setVisibility(View.VISIBLE);
                }

//                uploadToCloudinary(filePath);

            }
        });
    }

    private void chooseItemImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_ITEM_IMAGE);
    }
// not useful for now but might be used at some point

    /**
     * private void configCloudinary() {
     * config = new HashMap();
     * config.put("cloud_name", "doxgcbrlz");
     * config.put("api_key", "364564726645171");
     * config.put("api_secret", "u1mEEHzOdglDH_usbekyIT4DrjI");
     * MediaManager.init(Objects.requireNonNull(getContext()), config);
     * }
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;
//        filePath = getRealPathFromUri(data.getData(), Objects.requireNonNull(getActivity()));

        if (requestCode == PICK_ITEM_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            itemImagePath = data.getData();
            itemPic.setImageURI(itemImagePath);
        }
    }

    /**
     * private void uploadToCloudinary(String filePath) {
     * Log.d("A", "sign up uploadToCloudinary- ");
     * MediaManager.get().upload(filePath).callback(new UploadCallback() {
     *
     * @Override public void onStart(String requestId) {
     * //                mText.setText("start");
     * progressBar.setVisibility(View.VISIBLE);
     * }
     * @Override public void onProgress(String requestId, long bytes, long totalBytes) {
     * //                mText.setText("Uploading... ");
     * progressBar.setVisibility(View.VISIBLE);
     * }
     * @Override public void onSuccess(String requestId, Map resultData) {
     * //                mText.setText("image URL: " + resultData.get("url").toString());
     * progressBar.setVisibility(View.GONE);
     * }
     * @Override public void onError(String requestId, ErrorInfo error) {
     * //                mText.setText("error " + error.getDescription());
     * progressBar.setVisibility(View.GONE);
     * Toast.makeText(getContext(), "Error: "+ error.getDescription(), Toast.LENGTH_SHORT).show();
     * }
     * @Override public void onReschedule(String requestId, ErrorInfo error) {
     * //                mText.setText("Reshedule " + error.getDescription());
     * progressBar.setVisibility(View.GONE);
     * Toast.makeText(getContext(), "Error: "+error.getDescription(), Toast.LENGTH_SHORT).show();
     * }
     * }).dispatch();
     * }
     * <p>
     * private String getRealPathFromUri(Uri imageUri, Activity activity) {
     * Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);
     * <p>
     * if (cursor == null) {
     * return imageUri.getPath();
     * } else {
     * cursor.moveToFirst();
     * int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
     * return cursor.getString(idx);
     * }
     * }
     */

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack("Home");
        fragmentTransaction.commit();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = (Objects.requireNonNull(getActivity())).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void setItemsToDatabase() {

        final String itemsName = Objects.requireNonNull(itemName.getText()).toString().trim();
        final int itemsPrice = Integer.parseInt(Objects.requireNonNull(itemPrice.getText()).toString().trim());
        final String itemsDesc = Objects.requireNonNull(itemDesc.getText()).toString().trim();

        reference = FirebaseDatabase.getInstance().getReference("Store Items");
        if (itemImagePath != null) {
            final StorageReference fileReference = storageReference.child(docID+".image");

            uploadTask = fileReference.putFile(itemImagePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                    progressBar.setVisibility(View.GONE);
                                    replaceFragments(new BusinessProfileStore());
                                }
                            }, 500);
//
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

                        firebaseFirestore = FirebaseFirestore.getInstance();
                        Map<String, Object> Item = new HashMap<>();


                        Item.put("Item Name", itemsName);
                        Item.put("Item Price", itemsPrice);
                        Item.put("Item Description", itemsDesc);
                        Item.put("Email Address", getEmail());
                        Item.put("DownloadUri", downloadUri);
                        Item.put("File Path", docID);

                        firebaseFirestore.collection("Store Items")
                                .document(docID)
                                .set(Item)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void documentReference) {
//                                    Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Please select a file!", Toast.LENGTH_SHORT).show();
        }

//        String downloadUri = itemsModel.getProductImage().trim();
    }

    public String getEmail() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String emailAddress;
        assert currentUser != null;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }


}