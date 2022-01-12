package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Survey extends AppCompatActivity {
    public boolean isClicked;
    private final String userID = LoginActivity.getData().get("User Id"); // is this way secure??
    private static final String SURVEYHOUSE_KEY = "Residence";

    private static final String TAG = "Survey Activity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRef = db.document("Login Data/" + userID +"/Carbon Tracking/Survey");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_lifestyle_home);
    }

    // Access previous screen using back button
    public void back (View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void next (View view) {
        if (!isClicked) {
            Context context = getApplicationContext();
            CharSequence text = "You did not select anything!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onClickSurveyOption (View view)
    {
        isClicked = true;
        double houseCarbon = 0.0;
        switch (view.getId()) {
            case R.id.r1c1card:
                houseCarbon = 1.0;
                break;
            case R.id.r1c2card:
                houseCarbon = 2.0;
                break;
            case R.id.r2c1card:
                houseCarbon = 3.0;
                break;
            case R.id.r2c2card:
                houseCarbon = 4.0;
                break;
            case R.id.r3c1card:
                houseCarbon = 5.0;
                break;
            case R.id.r3c2card:
                houseCarbon = 100;
                break;
            default:
                isClicked = false;
                break;
        }
        addHouse(houseCarbon);
    }

    public void addHouse(double houseCarbon) {
        Map <String, Object> newSurveyData = new HashMap<>();
        newSurveyData.put(SURVEYHOUSE_KEY, houseCarbon);
        mDocRef.set(newSurveyData).addOnCompleteListener(new OnCompleteListener<Void>() {
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