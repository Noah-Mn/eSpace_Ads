package com.example.espace_ads.activityClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.espace_ads.R;
import com.example.espace_ads.databinding.ActivityCreateCampaignBinding;
import com.example.espace_ads.fragments.Ad_Creative;

public class CreateCampaign extends AppCompatActivity {

    ActivityCreateCampaignBinding binding;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCampaignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragments(new Ad_Creative());
    }
    private void replaceFragments(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }
    private void listeners(){
        binding.
    }
}