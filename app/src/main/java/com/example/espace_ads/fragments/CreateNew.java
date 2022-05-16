package com.example.espace_ads.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.espace_ads.R;
import com.example.espace_ads.activityClasses.CreateCampaign;
import com.example.espace_ads.databinding.FragmentCreateNewBinding;

public class CreateNew extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentCreateNewBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateNewBinding.inflate(inflater, container, false);
        listeners();

        return binding.getRoot();
    }
    private void listeners(){
        binding.next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateCampaign.class);
                startActivity(intent);
            }
        });
        binding.next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateCampaign.class);
                startActivity(intent);
            }
        });
        binding.next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateCampaign.class);
                startActivity(intent);
            }
        });
        binding.next4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateCampaign.class);
                startActivity(intent);
            }
        });
        binding.next5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateCampaign.class);
                startActivity(intent);
            }
        });
        binding.next6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateCampaign.class);
                startActivity(intent);
            }
        });
        binding.next7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateCampaign.class);
                startActivity(intent);
            }
        });
        binding.next8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateCampaign.class);
                startActivity(intent);
            }
        });
    }

}