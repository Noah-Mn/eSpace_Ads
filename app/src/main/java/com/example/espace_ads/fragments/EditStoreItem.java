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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.espace_ads.R;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class EditStoreItem extends Fragment {

    View view;
    private MaterialTextView changeItemPic;
    private AppCompatImageView itemPic;
    private TextInputEditText itemPrice, itemName, itemDesc;
    private MaterialCardView addItem;
    private final int PICK_ITEM_IMAGE = 3;
    private Uri itemImagePath;
    private ProgressBar progressBar;
    FirebaseUser currentUser;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    String productName;
    ItemsModel itemsModel;

    public EditStoreItem() {
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

        assert getArguments() != null;
        productName = getArguments().getString("productName");
        String description = getArguments().getString("descrip");
        String image = getArguments().getString("image");
        int price = getArguments().getInt("price");

        view = inflater.inflate(R.layout.fragment_edit_store_item, container, false);
        changeItemPic = view.findViewById(R.id.change_item_pic);
        itemPic = view.findViewById(R.id.item_pic);
        itemName = view.findViewById(R.id.editText_item_name);
        itemPrice = view.findViewById(R.id.editText_item_price);
        itemDesc = view.findViewById(R.id.editText_item_desc);
        addItem = view.findViewById(R.id.add_item);
        storageReference = FirebaseStorage.getInstance().getReference("Store Items Images").child(getEmail());
        progressBar = view.findViewById(R.id.content_loading_progressbar);
        itemsModel = new ItemsModel();

//        set items from database
        try {
            URL url = new URL(image);
            Picasso.with(getContext()).load(String.valueOf(url)).into(itemPic);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        itemName.setText(productName);
        itemPrice.setText(String.valueOf(price));
        itemDesc.setText(description);

        listeners();

        return view;
    }

    private void listeners() {
        changeItemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseItemImage();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                setItemsToDatabase();
            }
        });
    }

    private void chooseItemImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_ITEM_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
//        filePath = getRealPathFromUri(data.getData(), Objects.requireNonNull(getActivity()));

            if (requestCode == PICK_ITEM_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
                itemImagePath = data.getData();
                itemPic.setImageURI(itemImagePath);
            }
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

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = (Objects.requireNonNull(getActivity())).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void setItemsToDatabase() {

        String itemsName = Objects.requireNonNull(itemName.getText()).toString().trim();
        int itemsPrice = Integer.parseInt(Objects.requireNonNull(itemPrice.getText()).toString().trim());
        String itemsDesc = Objects.requireNonNull(itemDesc.getText()).toString().trim();

//        reference = FirebaseDatabase.getInstance().getReference("Store Items");
        if (itemImagePath != null) {
            StorageReference fileReference = storageReference.child(itemsModel.getFilePath()+".image");

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
                        assert getArguments() != null;
                        firebaseFirestore.collection("Store Items")
                                .whereEqualTo("Email Address", getEmail())
                                .whereEqualTo("Item Name", getArguments().getString("productName"))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                String docID = documentSnapshot.getId();

                                                if (!itemsName.matches("")) {
                                                    firebaseFirestore.collection("Store Items")
                                                            .document(docID)
                                                            .update(
                                                                    "Item Name", itemsName

                                                            )
                                                            .addOnSuccessListener(documentReference -> {
//                                                                Toast.makeText(getContext(), "Item name has been updated", Toast.LENGTH_SHORT).show();
//
                                                            })
                                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                                }
                                                if (!itemPrice.getText().toString().trim().matches("")) {

                                                    firebaseFirestore.collection("Store Items")
                                                            .document(docID)
                                                            .update(
                                                                    "Item Price", itemsPrice

                                                            )
                                                            .addOnSuccessListener(documentReference -> {
//                                                                Toast.makeText(getContext(), "Price has been updated", Toast.LENGTH_SHORT).show();
//
                                                            })
                                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                                }

                                                if (!downloadUri.toString().trim().matches("")) {

                                                    firebaseFirestore.collection("Store Items")
                                                            .document(docID)
                                                            .update(

                                                                    "DownloadUri", downloadUri

                                                            )
                                                            .addOnSuccessListener(documentReference -> {
//                                                                Toast.makeText(getContext(), "Data has been updated", Toast.LENGTH_SHORT).show();
//
                                                            })
                                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                                }

                                                if (!itemsDesc.trim().matches("")) {

                                                    firebaseFirestore.collection("Store Items")
                                                            .document(docID)
                                                            .update(
                                                                    "Item Description", itemsDesc

                                                            )
                                                            .addOnSuccessListener(documentReference -> {
//                                                                Toast.makeText(getContext(), "Items descr has been updated", Toast.LENGTH_SHORT).show();
//
                                                            })
                                                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                                }

                                            }
                                            Toast.makeText(getContext(), "Data has been updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Failed to update data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
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
//            Toast.makeText(getContext(), "Please select a file!", Toast.LENGTH_SHORT).show();

            firebaseFirestore = FirebaseFirestore.getInstance();
            assert getArguments() != null;
            firebaseFirestore.collection("Store Items")
                    .whereEqualTo("Email Address", getEmail())
                    .whereEqualTo("Item Name", getArguments().getString("productName"))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    String docID = documentSnapshot.getId();

                                    if (!itemsName.matches("")) {
                                        firebaseFirestore.collection("Store Items")
                                                .document(docID)
                                                .update(
                                                        "Item Name", itemsName

                                                )
                                                .addOnSuccessListener(documentReference -> {
//                                                    Toast.makeText(getContext(), "Item name has been updated", Toast.LENGTH_SHORT).show();

                                                })
                                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                    }
                                    if (!itemPrice.getText().toString().trim().matches("")) {

                                        firebaseFirestore.collection("Store Items")
                                                .document(docID)
                                                .update(
                                                        "Item Price", itemsPrice

                                                )
                                                .addOnSuccessListener(documentReference -> {
//                                                    Toast.makeText(getContext(), "Price has been updated", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                    }

                                    if (!itemsDesc.trim().matches("")) {

                                        firebaseFirestore.collection("Store Items")
                                                .document(docID)
                                                .update(
                                                        "Item Description", itemsDesc

                                                )
                                                .addOnSuccessListener(documentReference -> {
//                                                    Toast.makeText(getContext(), "Items descr has been updated", Toast.LENGTH_SHORT).show();
//
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                    }

                                }
                                Toast.makeText(getContext(), "Data has been updated", Toast.LENGTH_SHORT).show();
                                progressBar.setProgress(0);
                                progressBar.setVisibility(View.GONE);
                                replaceFragments(new BusinessProfileStore());

                            } else {
                                Toast.makeText(getContext(), "Failed to update data", Toast.LENGTH_SHORT).show();
                                progressBar.setProgress(0);
                                progressBar.setVisibility(View.GONE);
                                replaceFragments(new BusinessProfileStore());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setProgress(0);
                            progressBar.setVisibility(View.GONE);
                        }
                    });

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