package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsFragment extends androidx.fragment.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar_settings);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(Color.WHITE);

        Button methodBttn = v.findViewById(R.id.methods);
        methodBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent methodIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1rkhQcfReiQWQTvDPHptNIa-E1orbqOd-TlsRUO-32hQ/edit?usp=sharing"));
                startActivity(methodIntent);
            }
        });

        Button refBttn = v.findViewById(R.id.ref);
        refBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1VUyrJjL4f2eCPCjYX4jEJNYeqCstSzmgbOjJ7CJVrzg/edit?usp=sharing"));
                startActivity(refIntent);
            }
        });


        return v;
    }

}
