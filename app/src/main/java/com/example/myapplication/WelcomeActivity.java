package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_greeting);
    }

    public void onClick (View v) {
        Intent startIntent = new Intent(this, SurveyActivity.class);
        startActivity(startIntent);
    }
}
