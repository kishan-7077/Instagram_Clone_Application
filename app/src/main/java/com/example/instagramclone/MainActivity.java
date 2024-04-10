package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagramclone.Fragments.HomeFragment;
import com.example.instagramclone.Fragments.NotificationFragment;
import com.example.instagramclone.Fragments.ProfileFragment;
import com.example.instagramclone.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                } else if (menuItem.getItemId() == R.id.nav_search){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SearchFragment()).commit();
                }else if (menuItem.getItemId() == R.id.nav_more){
                    startActivity(new Intent(MainActivity.this,PostActivity.class));
                }else if (menuItem.getItemId() == R.id.nav_heart){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NotificationFragment()).commit();
                }else if (menuItem.getItemId() == R.id.nav_user){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                }
                return true;
            }
        });
    }
}