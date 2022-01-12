package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ServiceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private final String userID = LoginActivity.getData().get("User Id");
    private static final String TAG = "Service";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public double price, multiplier;
    public double serviceCarbon;
    public double dailyServiceCarbon;
    EditText input;

    String [] SERVICE_VALUES = {"Health Care","Information and Communication"
            ,"Medical (Eye care, Dental)","Vehicle Service","Financial Services"
            ,"Household Repairs","Charity","Other Services (Education,etc)" };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Toolbar toolbar = findViewById(R.id.toolbar_service);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle(R.string.service_input);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Spinner spinner = findViewById(R.id.service_spinner);

        input = findViewById(R.id.service_edit_text);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, SERVICE_VALUES);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String serviceSelected = parent.getItemAtPosition(position).toString();
            switch (serviceSelected) {
                case "Health Care":
                    multiplier = 0.22;
                    break;
                case "Information and Communication":
                    multiplier = 0.23;
                    break;
                case "Medical (Eye care, Dental)":
                    multiplier = 0.15;
                    break;
                case "Vehicle Service":
                    multiplier = 0.32;
                    break;
                case "Financial Services":
                    multiplier = 0.14;
                    break;
                case "Household Repairs":
                    multiplier = 0.68;
                    break;
                case "Charity":
                    multiplier = 0.37;
                    break;
                case "Other Services (Education,etc)":
                    multiplier = 0.33;
                    break;
            }
        }


    public void serviceTrack (View v) {
        price = Double.parseDouble(input.getText().toString());
        serviceCarbon = price * multiplier;
        dailyServiceCarbon += serviceCarbon;
        addServiceCarbon(serviceCarbon);
        addTotalCarbon(dailyServiceCarbon);
        input.setText("");
    }

    private void addTotalCarbon(double dailyServiceCarbon) {
        DecimalFormat df = new DecimalFormat("######.00");
        //set date format and initialize document to store
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());

        DocumentReference mDocRef = db.document("Login Data/"
                + userID +"/Carbon Tracking/Daily Totals/Service/" + date);

        Map<String, Object> newSurveyData = new HashMap<>();
        String SERVICE_DAILY__KEY = date;

        newSurveyData.put(SERVICE_DAILY__KEY, df.format(dailyServiceCarbon));
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

    private void addServiceCarbon (double serviceCarbon) {
        DecimalFormat df = new DecimalFormat("######.00");
        //set date format and initialize document to store
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        Date time = Calendar.getInstance().getTime();

        DocumentReference mDocRef = db.document("Login Data/"
                + userID +"/Carbon Tracking/Historical/Service/" + date);

        Map<String, Object> newSurveyData = new HashMap<>();
        String SERVICE_KEY = time.toString();

        newSurveyData.put(SERVICE_KEY, df.format(serviceCarbon));
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
        Toast toast = Toast.makeText(getApplicationContext(),
                "Ripple Track: " + df.format(serviceCarbon) + " CO2e",
                Toast.LENGTH_SHORT);
        toast.show();
    }

}
