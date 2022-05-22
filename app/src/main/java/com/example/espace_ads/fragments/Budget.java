package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.espace_ads.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Objects;

public class Budget extends Fragment {

    MaterialSpinner frequencySp, amountSp, amountPerViewSp;
    TextInputEditText customAmount;
    MaterialCardView custom;

    public Budget() {
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
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        frequencySp = view.findViewById(R.id.spinner_frequency);
        amountSp = view.findViewById(R.id.spinner_amount);
        amountPerViewSp = view.findViewById(R.id.spinner_amount_per_view);
        customAmount = view.findViewById(R.id.custom_amount);
        custom = view.findViewById(R.id.custom);

        setAmountSpinner(view);
        setFrequencySpinner(view);
        setAmountPerViewSpinner(view);
        return view;
    }

    public void setAmountSpinner(View view) {

        String[] displayAmount = new String[]{"20,000", "50,000", "80,000", "110,000", "140,000", "Custom"};
        long[] amountValues = new long[]{20000L, 50000L, 80000L, 110000L, 140000L, 0L};

        ArrayAdapter<String> amountAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, displayAmount);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        amountSp.setAdapter(amountAdapter);

        amountSp.setOnItemSelectedListener((view1, position, id, item) -> {

                if (position == amountValues.length - 1) {
                    custom.setVisibility(View.VISIBLE);
                   long value = Integer.parseInt(Objects.requireNonNull(customAmount.getText()).toString());

//                   will set value here
                } else {
                    custom.setVisibility(View.GONE);
                   long value = amountValues[position];

//                   else another value here
                }

        });
    }

    public void setFrequencySpinner(View view) {

        ArrayList<String> frequency = new ArrayList<>();

        frequency.add("Daily");
        frequency.add("Weekly");
        frequency.add("Monthly");

        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, frequency);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        frequencySp.setAdapter(frequencyAdapter);
    }

    public void setAmountPerViewSpinner(View view) {

        ArrayList<String> amountPerView = new ArrayList<>();

        amountPerView.add("Views- MKW5/view");
        amountPerView.add("Targeted Views- MKW10/view");
        amountPerView.add("Interaction- MKW15/view");
        amountPerView.add("Targeted Interaction- MKW20/view");

        ArrayAdapter<String> amountPerViewAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, amountPerView);
        amountPerViewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        amountPerViewSp.setAdapter(amountPerViewAdapter);
    }
}