package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class FunFragment extends androidx.fragment.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fun_fragment, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar_fun);
        toolbar.setTitle("Fun");
        toolbar.setTitleTextColor(Color.WHITE);
        return v;
    }
}
