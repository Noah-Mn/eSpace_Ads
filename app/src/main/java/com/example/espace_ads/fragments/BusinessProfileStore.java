package com.example.espace_ads.fragments;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.GridAdapter;
import com.example.espace_ads.models.ItemsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class BusinessProfileStore extends Fragment {

    ArrayList<ItemsModel> items = new ArrayList<ItemsModel>();
    View view;
    RecyclerView gridView;
    DatabaseReference reference;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    GridAdapter gridAdapter;
    MaterialCardView addItems, cardFacebook, cardTwitter, cardLinkedin, cardInstagram;
    MaterialTextView textCompanyName, textCompanyType, textCompanyDescription, companyURL;
    AppCompatImageView imageView;
    RoundedImageView roundedImageView;
    LinearLayout socialMedia;
    StorageReference storageReference;


    public BusinessProfileStore() {
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
        view = inflater.inflate(R.layout.fragment_business_profile_store, container, false);
        gridView = view.findViewById(R.id.stores_list);
        addItems = view.findViewById(R.id.add_items);
        db = FirebaseFirestore.getInstance();
        textCompanyName = view.findViewById(R.id.text_business_name);
        textCompanyType = view.findViewById(R.id.company_type);
        textCompanyDescription = view.findViewById(R.id.company_description);
        companyURL = view.findViewById(R.id.company_url);
        imageView = view.findViewById(R.id.cover_image);
        roundedImageView = view.findViewById(R.id.profile_image);
        socialMedia = view.findViewById(R.id.social_media);
        cardFacebook = view.findViewById(R.id.facebook);
        cardInstagram = view.findViewById(R.id.instagram);
        cardLinkedin = view.findViewById(R.id.linkedin);
        cardTwitter = view.findViewById(R.id.twitter);
        storageReference = FirebaseStorage.getInstance().getReference("Store Items Images").child(getEmail());

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new AddItemToStore());
            }
        });


        gridAdapter = new GridAdapter(items, getContext(), new GridAdapter.OnItemClickListener() {

            @Override
            public void OnItemClick(ItemsModel item) {
                BusinessProfileBuy businessProfileBuy = new BusinessProfileBuy();
                Bundle bundle = new Bundle();
                bundle.putString("image", item.getProductImage());
                bundle.putString("descrip", item.getDescription());
                bundle.putInt("price", item.getPrices());
                bundle.putString("productName", item.getName());
                businessProfileBuy.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                assert getFragmentManager() != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, businessProfileBuy);
                fragmentTransaction.addToBackStack("Home");
                fragmentTransaction.commit();
            }
        }, new GridAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick(ItemsModel itemsModel) {
                PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), gridView);
                popupMenu.getMenuInflater().inflate(R.menu.grid_item_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit:
//                                Toast.makeText(getContext(), "Edit clicked", Toast.LENGTH_SHORT).show();

                                EditStoreItem editStoreItem = new EditStoreItem();
                                Bundle bundle = new Bundle();
                                bundle.putString("image", itemsModel.getProductImage());
                                bundle.putString("descrip", itemsModel.getDescription());
                                bundle.putInt("price", itemsModel.getPrices());
                                bundle.putString("productName", itemsModel.getName());
                                editStoreItem.setArguments(bundle);
                                FragmentManager fragmentManager = getFragmentManager();
                                assert getFragmentManager() != null;
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container, editStoreItem);
                                fragmentTransaction.addToBackStack("Home");
                                fragmentTransaction.commit();
                                break;

                            case R.id.delete:
                                StorageReference reference = storageReference.child(itemsModel.getFilePath()+".image");
                                reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Error: failed to delete item", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                db.collection("Store Items").document(itemsModel.getFilePath()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Item deleted from database", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
//                                Toast.makeText(getContext(), "Delete clicked", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();

                return true;
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Store Items");
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        gridView.setAdapter(gridAdapter);
//        gridView.addOnItemTouchListener(new RecyclerI);

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
                                String filePath = documentSnapshot.getString("File Path");
                                ItemsModel itemsModel = new ItemsModel(name, price, description, downloadUri, filePath);
                                items.add(itemsModel);
                            }
                            if (items.size() > 0) {
                                gridAdapter.setStoreItemsList(items);
//                                mProgressBar.setVisibility(View.GONE);
//                                liveCampaign.setVisibility(View.GONE);
                            } else {
//                                liveCampaign.setVisibility(View.VISIBLE);
//                                mProgressBar.setVisibility(View.GONE);
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


//        get business profile data from database

        db.collection("Business Profile")
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

                                    assert facebookProfile != null;
                                    if (!facebookProfile.matches("")) {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardFacebook.setVisibility(View.VISIBLE);
                                    } else {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardFacebook.setVisibility(View.GONE);
                                    }

                                    assert twitterProfile != null;
                                    if (!twitterProfile.matches("")) {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardTwitter.setVisibility(View.VISIBLE);
                                    } else {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardTwitter.setVisibility(View.GONE);
                                    }

                                    assert instagramProfile != null;
                                    if (!instagramProfile.matches("")) {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardInstagram.setVisibility(View.VISIBLE);
                                    } else {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardInstagram.setVisibility(View.GONE);
                                    }

                                    assert linkedinProfile != null;
                                    if (!linkedinProfile.matches("")) {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardLinkedin.setVisibility(View.VISIBLE);
                                    } else {
                                        socialMedia.setVisibility(View.VISIBLE);
                                        cardLinkedin.setVisibility(View.GONE);
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

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = (Objects.requireNonNull(getActivity())).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public String getEmail() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String emailAddress;
        assert currentUser != null;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack("Home");
        fragmentTransaction.commit();
    }
}