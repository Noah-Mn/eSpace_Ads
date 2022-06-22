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
import com.example.espace_ads.models.ItemsModel;
import com.example.espace_ads.models.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
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
    private int PICK_ITEM_IMAGE = 3;
    private Uri itemImagePath;
    private ItemsModel itemsModel;
    ArrayList<ItemsModel> itemsModels = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private StorageTask uploadTask;
    private ProgressBar progressBar;

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
        addItemPic = view.findViewById(R.id.add_item_pic);
        itemPic = view.findViewById(R.id.item_pic);
        itemName = view.findViewById(R.id.editText_item_name);
        itemPrice = view.findViewById(R.id.editText_item_price);
        itemDesc = view.findViewById(R.id.editText_item_desc);
        addItem = view.findViewById(R.id.add_item);
        itemsModel = new ItemsModel();
        progressBar = view.findViewById(R.id.content_loading_progressbar);
        storageReference = FirebaseStorage.getInstance().getReference("Items Images").child("Noah");

        listeners();

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

        if (requestCode == PICK_ITEM_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            itemImagePath = data.getData();
            itemPic.setImageURI(itemImagePath);
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
        ContentResolver contentResolver = (getActivity()).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void setItemsToDatabase() {

        reference = FirebaseDatabase.getInstance().getReference("Store Items");
        if (itemImagePath != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(itemImagePath));

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
                                    replaceFragments(new CreateBusinessProfile());
                                }
                            }, 500);
                            Toast.makeText(getContext(), "Image Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(Objects.requireNonNull(itemName.getText()).toString().trim(), taskSnapshot.getStorage().getDownloadUrl().toString());

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
        } else {
            Toast.makeText(getContext(), "No file selected!", Toast.LENGTH_SHORT).show();
        }

        /** get all the required data at this point */

        String itemsName = Objects.requireNonNull(itemName.getText()).toString().trim();
        int itemsPrice = Integer.parseInt(Objects.requireNonNull(itemPrice.getText()).toString().trim());
        String itemsDesc = Objects.requireNonNull(itemDesc.getText()).toString().trim();

        firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> Item = new HashMap<>();

        Item.put("Item Name", itemsName);
        Item.put("Item Price", itemsPrice);
        Item.put("Item Description", itemsDesc);

        firebaseFirestore.collection("Store Items")
                .add(Item)
                .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save data", Toast.LENGTH_SHORT).show());

    }

}