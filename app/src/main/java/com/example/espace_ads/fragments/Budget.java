package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.espace_ads.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

public class Budget extends Fragment {

    MaterialSpinner frequency, amountSp, amountPerView;
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

        frequency = view.findViewById(R.id.spinner_frequency);
        amountSp = view.findViewById(R.id.spinner_amount);
        amountPerView = view.findViewById(R.id.spinner_amount_per_view);

        setAmountSpinner(view);
        return view;
    }

    public void setAmountSpinner(View view) {

        ArrayList<Long> amount = new ArrayList<>();

        amount.add(50000L);
        amount.add(80000L);
        amount.add(110000L);
        amount.add(140000L);

        ArrayAdapter<Long> amountAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, amount);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        amountSp.setAdapter(amountAdapter);
    }
}