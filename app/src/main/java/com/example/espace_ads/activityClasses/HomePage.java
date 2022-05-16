package com.example.espace_ads.activityClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;

import com.example.espace_ads.R;
import com.example.espace_ads.databinding.ActivityHomePageBinding;
import com.example.espace_ads.fragments.Ads;
import com.example.espace_ads.fragments.HomeFlag;
import com.example.espace_ads.fragments.Menu;
import com.example.espace_ads.fragments.Notifications;
import com.example.espace_ads.fragments.Statistics;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    private ActivityHomePageBinding binding;
    private FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavView.setBackground(null);
        binding.bottomNavView.getMenu().getItem(2).setEnabled(false);
        replaceFragments(new HomeFlag());
        listeners();
    }

    private void listeners(){
        binding.bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                removeItemUnderLine(binding.bottomNavView);
                underLineMenuItem(item);

                switch (item.getItemId()){
                    case R.id.home:
                        replaceFragments(new HomeFlag());
                        break;
                    case R.id.statistics:
                        replaceFragments(new Statistics());
                        break;
                    case R.id.notifications:
                        replaceFragments(new Notifications());
                        break;
                    case R.id.menu:
                        replaceFragments(new Menu());
                        break;
                }
                return true;
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragments(new Ads());
            }
        });
    }


    private void removeItemUnderLine(BottomNavigationView bottomNavigationView){
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++){
            MenuItem item = bottomNavigationView.getMenu().getItem(i);
            item.setTitle(item.getTitle().toString());
        }
    }

    private void underLineMenuItem(MenuItem item){
        SpannableString content = new SpannableString(item.getTitle());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        item.setTitle(content);
    }
    private void replaceFragments(Fragment fragment){
        binding.container.setVisibility(View.VISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0){
            super.onBackPressed();
        }else {
            getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }
}