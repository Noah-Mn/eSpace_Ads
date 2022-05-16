package com.example.espace_ads.activityClasses;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.CreateCampaignAdapter;
import com.example.espace_ads.databinding.ActivityCreateCampaignBinding;
import com.example.espace_ads.fragments.Ad_Creative;
import com.example.espace_ads.fragments.Budget;
import com.example.espace_ads.fragments.Target_Audience;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.Collections2;

import java.util.Objects;

public class CreateCampaign extends AppCompatActivity {

    ActivityCreateCampaignBinding binding;
    TabLayout tabLayout;
    TabItem tabItem;
    ViewPager2 viewPager;
    CreateCampaignAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityCreateCampaignBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_create_campaign);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);


        adapter = new CreateCampaignAdapter(this);
        viewPager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });

//        binding.tabLayout.setupWithViewPager(binding.container);
//
//        CreateCampaignAdapter createCampaignAdapter = new CreateCampaignAdapter(getSupportFragmentManager(), binding.tabLayout.getTabCount());
//        createCampaignAdapter.addFragment(new Ad_Creative(), "Add");
//        createCampaignAdapter.addFragment(new Target_Audience(), "target");
//        createCampaignAdapter.addFragment(new Budget(), "Budget");
//
//        binding.container.setAdapter(createCampaignAdapter);
//        binding.container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
//        binding.tabLayout.setOnTabSelectedListener(this);

    }
}