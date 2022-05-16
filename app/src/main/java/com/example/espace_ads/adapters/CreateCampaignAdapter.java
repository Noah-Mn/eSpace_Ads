package com.example.espace_ads.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.espace_ads.fragments.Ad_Creative;
import com.example.espace_ads.fragments.Budget;
import com.example.espace_ads.fragments.Target_Audience;

import java.util.ArrayList;
import java.util.List;

public class CreateCampaignAdapter extends FragmentStateAdapter {

    public CreateCampaignAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Ad_Creative();
            case 1:
                return new Target_Audience();

            case 2:
                return new Budget();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
