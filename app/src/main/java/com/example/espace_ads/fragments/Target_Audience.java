package com.example.espace_ads.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.espace_ads.R;
import com.example.espace_ads.models.AdModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Target_Audience extends Fragment {
    MaterialSpinner genderSpinner, ageSpinner;
    TextInputEditText location;
    String locations, age, gender;
    FirebaseFirestore db;
    MaterialCardView save;
    private StorageReference mStorageRef;

    public Target_Audience() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_target__audience, container, false);

        location = view.findViewById(R.id.editText_locations);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        save = view.findViewById(R.id.save_btn);

        setGenderSpinner(view);
        setAgeSpinner(view);
        getInfo();



        return view;

    }

    public void setGenderSpinner(View view) {
        genderSpinner = (MaterialSpinner) view.findViewById(R.id.spinner_gender);
        ArrayList<String> gender = new ArrayList<>();

        gender.add("Male");
        gender.add("Female");
        gender.add("All Genders");

        ArrayAdapter<String> countAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, gender);
        countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter(countAdapter);
    }

    public void setAgeSpinner(View view) {
        ageSpinner = (MaterialSpinner) view.findViewById(R.id.spinner_age);
        ArrayList<String> age = new ArrayList<>();

        age.add("10-24");
        age.add("25-34");
        age.add("35-64");
        age.add("46+");

        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, age);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        ageSpinner.setAdapter(ageAdapter);
    }

    private void getInfo() {
        locations = location.getText().toString();
        gender = genderSpinner.getText().toString();
        age = ageSpinner.getText().toString();

    }

    @Override
    public void onStart() {
        super.onStart();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                Map<String, Object> Ad = new HashMap<>();

                Ad.put("Location", location.getText().toString());
                Ad.put("Gender", genderSpinner.getText().toString());
                Ad.put("Age", ageSpinner.getText().toString());

                db.collection("Advert")
                        .add(Ad)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }
}




