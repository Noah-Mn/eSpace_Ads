package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.espace_ads.R;
import com.google.android.material.textview.MaterialTextView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

public class Target_Audience extends Fragment {
    MaterialSpinner genderSpinner, ageSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_target__audience, container, false);

        setGenderSpinner(view);
        setAgeSpinner(view);
        return view;
    }
    public void setGenderSpinner(View view){
        genderSpinner = (MaterialSpinner)view.findViewById(R.id.spinner_gender);
        ArrayList<String> gender = new ArrayList<>();

        gender.add("Female");
        gender.add("Male");
        gender.add("All Genders");

        ArrayAdapter<String> countAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,gender);
        countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter(countAdapter);
    }

    public void setAgeSpinner(View view){
        ageSpinner = (MaterialSpinner)view.findViewById(R.id.spinner_age);
        ArrayList<String> age = new ArrayList<>();

        age.add("18-25");
        age.add("26-33");
        age.add("34-41");
        age.add("42+");

        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,age);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        ageSpinner.setAdapter(ageAdapter);
    }

}