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
import android.widget.FrameLayout;

import com.example.espace_ads.R;
import com.example.espace_ads.databinding.ActivityHomePageBinding;
import com.example.espace_ads.flagments.HomeFlag;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

//    private ActivityHomePageBinding binding;
    private FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;
    BottomNavigationView bottomNavigationView;
    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

//        layout = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

//        underline the home
//        underLineMenuItem(binding.bottomNavView.getMenu().getItem(0));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                removeItemUnderLine(bottomNavigationView);
                underLineMenuItem(item);

                switch (item.getItemId()){
                    case R.id.home:
//                        replaceFragments(new HomeFlag());
                        break;
                    case R.id.statistics:
                        break;
                    case R.id.notifications:
                        break;
                    case R.id.menu:
                        break;
                }
                return true;
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
//    private void replaceFragments(Fragment fragment){
//        layout.setVisibility(View.VISIBLE);
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
//        fragmentTransaction.replace(R.id.fragment_container, fragment);
//        fragmentTransaction.commit();
//
//    }
}