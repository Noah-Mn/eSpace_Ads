package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.GridAdapter;
import com.example.espace_ads.models.ItemsModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class BusinessProfileStore extends Fragment {

    ArrayList<ItemsModel> items = new ArrayList<ItemsModel>();
    View view;
    GridView gridView;
    DatabaseReference databaseReference;

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
//        items = getItemsList()

//        databaseReference = FirebaseDatabase.getInstance().getReference().child();

//        gridView.setAdapter(new GridAdapter(getContext(), items));
        
        return view;
    }
}