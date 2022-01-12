package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SurveyFragment1 extends Fragment implements View.OnClickListener {
    private final String userID = LoginActivity.getData().get("User Id");
    private static final String SURVEYDIET_KEY = "Diet";

    private static final String TAG = "Diet Activity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRef = db.document("Login Data/"
            + userID +"/Carbon Tracking/Survey");


    public SurveyFragment1() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.survey1_fragment, container, false);

        CardView cardOne = view.findViewById(R.id.r1c1card);
        CardView cardTwo = view.findViewById(R.id.r1c2card);
        CardView cardThree = view.findViewById(R.id.r2c1card);
        CardView cardFour = view.findViewById(R.id.r2c2card);
        CardView cardFive = view.findViewById(R.id.r3c1card);
        CardView cardSix = view.findViewById(R.id.r3c2card);

        cardOne.setOnClickListener(this);
        cardTwo.setOnClickListener(this);
        cardThree.setOnClickListener(this);
        cardFour.setOnClickListener(this);
        cardFive.setOnClickListener(this);
        cardSix.setOnClickListener(this);
        return view;
    }

    public void onClick (View view)
    {
        double dietCarbon = 0.0;

        switch (view.getId()) {
            case R.id.r1c1card:
                dietCarbon = 2734;
                break;
            case R.id.r1c2card:
                dietCarbon = 1015;
                break;
            case R.id.r2c1card:
                dietCarbon = 587;
                break;
            case R.id.r2c2card:
                dietCarbon = 1274;
                break;
            case R.id.r3c1card:
                dietCarbon = 3225;
                break;
            case R.id.r3c2card:
                dietCarbon = 1140;
                break;
        }
        addHouse(dietCarbon);
    }

    private void addHouse(double dietCarbon) {
        Map<String, Object> newSurveyData = new HashMap<>();
        newSurveyData.put(SURVEYDIET_KEY, dietCarbon);
        mDocRef.set(newSurveyData, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Task success");
                } else {
                    Log.d(TAG, "Doc failed!");
                }
            }
        });

        //Initialize Daily Carbon Tracking Totals
        String [] dailySubTopic = {
                "Product",
                "Service",
                "Travel"
        };

        for (String s : dailySubTopic) {
            String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
            String DAILY_KEY = date;
            float DAILY_VAL = 0;
            DocumentReference setDailyDocument = db.document("Login Data/"
                    + userID +"/Carbon Tracking/Daily Totals/" + s + "/" + date);
            Map<String, Object> dailyDataSet = new HashMap<>();
            dailyDataSet.put(DAILY_KEY, DAILY_VAL);

            setDailyDocument.set(dailyDataSet, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Doc has been shared!");

                    } else {
                        Log.d(TAG, "Doc failed!");
                    }
                }
            });
        }
    }

}
