package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static String USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize bottom nav bar and listener
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.getMenu().findItem(R.id.action_home).setChecked(true);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);

        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(getApplicationContext());
        USER_ID = sharedPreferences.getString("userID", "text");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new HomeFragment()).commit();
        }

    }

    // Navigation to respective layout and class if item is selected
    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragmentSelect = null;
            switch(item.getItemId()) {
                case R.id.action_home:
                    fragmentSelect = new HomeFragment();
                    break;
                case R.id.action_compete:
                    fragmentSelect = new CompeteFragment();
                    break;
                case R.id.action_fun:
                    fragmentSelect = new FunFragment();
                    break;
                case R.id.action_settings:
                    fragmentSelect = new SettingsFragment();
                    break;
            }
            assert fragmentSelect != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                    fragmentSelect).commit();
            return true;
        }
    };

    public void fabClick (View view) {
        Fragment fragmentSelect = new EntryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragmentSelect).commit();
    }

    static String getIdUser(){
        return USER_ID;
    }



}

