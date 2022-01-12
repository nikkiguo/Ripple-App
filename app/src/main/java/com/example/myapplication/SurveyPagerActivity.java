package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class SurveyPagerActivity extends FragmentStateAdapter {
    public SurveyPagerActivity(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new SurveyFragment1();
            case 1:
                return new SurveyFragment2();
            case 2:
                return new SurveyFragment3();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
