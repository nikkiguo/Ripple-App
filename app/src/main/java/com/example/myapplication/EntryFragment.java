package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class EntryFragment extends Fragment implements View.OnClickListener {

    static Integer transportation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.carbon_choices, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        CardView optionOne = view.findViewById(R.id.product_input);
        optionOne.setOnClickListener(this);
        CardView optionTwo = view.findViewById(R.id.car_input);
        optionTwo.setOnClickListener(this);
        CardView optionThree = view.findViewById(R.id.airplane_input);
        optionThree.setOnClickListener(this);
        CardView optionFour = view.findViewById(R.id.service_input);
        optionFour.setOnClickListener(this);

        Animation slideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.cardview_animation);

        optionOne.startAnimation(slideUp);
        optionTwo.startAnimation(slideUp);
        optionThree.startAnimation(slideUp);
        optionFour.startAnimation(slideUp);
    }

    public void onClick (View v) {
        Intent optionSelected = new Intent();
        switch(v.getId()){
            case R.id.product_input:
                optionSelected = new Intent (getActivity(), ProductActivity.class);
                break;
            case R.id.car_input:
                transportation = 0;
                optionSelected = new Intent (getActivity(), TransportationActivity.class);
                break;
            case R.id.airplane_input:
                transportation = 1;
                optionSelected = new Intent (getActivity(), TransportationActivity.class);
                break;
            case R.id.service_input:
                optionSelected = new Intent (getActivity(), ServiceActivity.class);
        }
        startActivity(optionSelected);
    }
}