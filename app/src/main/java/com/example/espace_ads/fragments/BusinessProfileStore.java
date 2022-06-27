package com.example.espace_ads.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BusinessProfileStore extends Fragment {

    ArrayList<ItemsModel> items = new ArrayList<ItemsModel>();
    View view;
    RecyclerView gridView;
    DatabaseReference reference;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    GridAdapter gridAdapter;
    MaterialCardView addItems;

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
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new AddItemToStore());
            }
        });


        gridAdapter = new GridAdapter(items, getContext(), new GridAdapter.OnItemClickListener(){

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
                                ItemsModel itemsModel = new ItemsModel(name, price, description, downloadUri);
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
//            }



        return view;
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