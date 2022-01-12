package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class CompeteFragment extends androidx.fragment.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflates layout for this fragment
        View v = inflater.inflate(R.layout.compete_fragment, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar_compete);
        toolbar.setTitle("Compete");
        return v;
    }
}
