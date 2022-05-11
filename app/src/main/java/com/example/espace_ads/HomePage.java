package com.example.espace_ads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
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
}