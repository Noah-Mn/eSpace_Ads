package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.espace_ads.R;
import com.example.espace_ads.databinding.FragmentAdsBinding;


public class Ads extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAdsBinding binding;
        // Inflate the layout for this fragment
        binding = getLayoutInflater().inflate(get)
//        return inflater.inflate(R.layout.fragment_ads, container, false);
    }
    private void replaceFragments(Fragment fragment){
        binding.container.setVisibility(View.VISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }
}