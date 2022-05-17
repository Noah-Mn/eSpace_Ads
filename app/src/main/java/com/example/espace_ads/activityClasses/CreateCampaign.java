package com.example.espace_ads.activityClasses;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.CreateCampaignAdapter;
import com.example.espace_ads.databinding.ActivityCreateCampaignBinding;
import com.example.espace_ads.fragments.Ad_Creative;
import com.example.espace_ads.fragments.Target_Audience;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class CreateCampaign extends AppCompatActivity implements Ad_Creative.InterFaceListener {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    CreateCampaignAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    }


    @Override
    public void passData(String primaryText, String headline, String description, String destination, String encodedImage) {
        Target_Audience target_audience = (Target_Audience) getSupportFragmentManager().findFragmentById(R.id.target_audience);
        assert target_audience != null;
        target_audience.getExtraData(primaryText, headline, description, destination, encodedImage);
    }
}