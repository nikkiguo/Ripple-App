package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SurveyFragment2 extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final String userID = LoginActivity.getData().get("User Id"); // is this way secure??
    private static final String SURVEYRESIDENCE_KEY = "Residence";
    private static final String PROVINCETERRITORY_KEY = "Province/Territory";

    private static final String TAG = "Residence Activity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRef = db.document("Login Data/"
            + userID +"/Carbon Tracking/Survey");

    public SurveyFragment2() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.survey2_fragment, container, false);
        String [] values = {"Select your home", "Alberta","British Columbia","Manitoba","New Brunswick"
                ,"Newfoundland and Labrador", "Nova Scotia","Northwest Territories"
                ,"Nunavut","Ontario","Prince Edward Island","Quebec","Saskatchewan","Yukon"};

        Spinner spinner = view.findViewById(R.id.canada_spinner);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>((Objects.requireNonNull(this.getActivity())),
                android.R.layout.simple_spinner_item, values);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);

        CardView cardOne = view.findViewById(R.id.r1c1card);
        CardView cardTwo = view.findViewById(R.id.r1c2card);
        CardView cardThree = view.findViewById(R.id.r2c1card);
        CardView cardFour = view.findViewById(R.id.r2c2card);

        cardOne.setOnClickListener(this);
        cardTwo.setOnClickListener(this);
        cardThree.setOnClickListener(this);
        cardFour.setOnClickListener(this);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String provinceTerritoryCarbon = parent.getItemAtPosition(position).toString();
        if(position != 0) {
            addProvinceTerritory(provinceTerritoryCarbon);
            Toast.makeText(parent.getContext(), provinceTerritoryCarbon, Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(parent.getContext(), provinceTerritoryCarbon, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        double residenceCarbon = 0.0;

        switch (view.getId()) {
            case R.id.r1c1card:
                residenceCarbon = 0.0;
                break;
            case R.id.r1c2card:
                residenceCarbon = 0.0;
                break;
            case R.id.r2c1card:
                residenceCarbon = 0.0;
                break;
            case R.id.r2c2card:
                residenceCarbon = 0.0;
                break;
        }
        addHouse(residenceCarbon);
    }


    private void addHouse(double residenceCarbon) {
        Map<String, Object> newSurveyData = new HashMap<>();
        newSurveyData.put(SURVEYRESIDENCE_KEY, residenceCarbon);
        mDocRef.set(newSurveyData, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void addProvinceTerritory(String provinceTerritoryCarbon) {
        Map<String, Object> newSurveyData = new HashMap<>();
        newSurveyData.put(PROVINCETERRITORY_KEY, provinceTerritoryCarbon);
        mDocRef.set(newSurveyData, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

