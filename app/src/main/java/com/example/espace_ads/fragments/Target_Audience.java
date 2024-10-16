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
import com.example.espace_ads.models.AdData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Objects;

public class Target_Audience extends Fragment {
    MaterialSpinner genderSpinner, ageSpinner;
    TextInputEditText location;
    String locations, age, gender;
    FirebaseFirestore db;
    MaterialCardView save;
    private StorageReference mStorageRef;
    FirebaseUser currentUser;

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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        location = view.findViewById(R.id.editText_locations);
        genderSpinner = (MaterialSpinner) view.findViewById(R.id.spinner_gender);
        ageSpinner = (MaterialSpinner) view.findViewById(R.id.spinner_age);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        save = view.findViewById(R.id.save_btn);
        db = FirebaseFirestore.getInstance();

        setGenderSpinner(view);
        setAgeSpinner(view);
        setInfo();

        return view;

    }

    public void setGenderSpinner(View view) {

        ArrayList<String> gender = new ArrayList<>();

        gender.add("Male");
        gender.add("Female");
        gender.add("All Genders");

        ArrayAdapter<String> countAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, gender);
        countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter(countAdapter);
        genderSpinner.setFocusable(false);
    }

    public void setAgeSpinner(View view) {

        String[] age = new String[]{"10-24", "25-34", "35-45", "46+"};

        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, age);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        ageSpinner.setAdapter(ageAdapter);
        ageSpinner.setFocusable(false);
        /**
        ageSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position == 0){
                    for (long age = 10; age <= 24; age++){

                    }
                }else if (position == 1){
                    for (long age = 25; age <= 34; age++){

                    }
                }else if (position == 2){
                    for (long age = 35; age <= 45; age++){

                    }
                }else {
                    for (long age = 46; age >= 46; age++){

                    }
                }
            }
        });
         **/
    }

    private void setInfo() {

        db.collection("Advert")
                .whereEqualTo("Email Address", getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                String headline = documentSnapshot.getString("Headline");
                                AdData adData = new AdData(headline);

                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        locations = Objects.requireNonNull(location.getText()).toString();
                                        gender = genderSpinner.getText().toString();
                                        age = ageSpinner.getText().toString();

                                        if (!locations.isEmpty() && !gender.isEmpty() && !age.isEmpty()) {

                                            db.collection("Advert")
                                                    .whereEqualTo("Email Address", getEmail())
                                                    .whereEqualTo("Headline", headline)
                                                    .get()
                                                    .addOnCompleteListener(task -> {

                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                                String Ad_ID = documentSnapshot.getId();
                                                                db.collection("Advert")
                                                                        .document(Ad_ID)
                                                                        .update("Location", locations,
                                                                                "Gender", gender,
                                                                                "Age", age);
                                                                Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(getContext(), "Failed to save data", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public String getEmail(){
        String emailAddress;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }


}