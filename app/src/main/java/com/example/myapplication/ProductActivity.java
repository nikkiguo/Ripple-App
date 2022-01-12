package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Objects;

public class ProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final String userID = LoginActivity.getData().get("User Id");
    private static final String TAG = "Product";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public double price, multiplier;
    public double productCarbon;
    public double dailyProductCarbon;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = findViewById(R.id.toolbar_product);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle(R.string.product_input);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        input = findViewById(R.id.product_edit_text);
        Spinner spinner = findViewById(R.id.product_spinner);
        String [] values = {"Household Furniture/Equipment","Clothing","Entertainment","Stationary, Paper, Books"
                ,"Personal Care, Cleaning","Vehicle Parts","Medical"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, values);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String productSelected = parent.getItemAtPosition(position).toString();
        switch (productSelected) {
            case "Household Furniture/Equipment":
                multiplier = 0.41;
                break;
            case "Clothing":
                multiplier = 0.42;
                break;
            case "Entertainment":
                multiplier = 0.42;
                break;
            case "Stationary, Paper, Books":
                multiplier = 0.26;
                break;
            case "Personal Care, Cleaning":
                multiplier = 0.49;
                break;
            case "Vehicle Parts":
                multiplier = 0.53;
                break;
            case "Medical":
                multiplier = 0.29;
                break;
        }
    }

    public void productTrack (View v) {
        price = Double.parseDouble(input.getText().toString());
        productCarbon = price * multiplier;
        dailyProductCarbon += productCarbon;
        addTotalCarbon(dailyProductCarbon);
        addProductCarbon(productCarbon);
        input.setText("");
    }

    private void addTotalCarbon (double dailyProductCarbon) {
        DecimalFormat df = new DecimalFormat("######.00");
        //set date format and initialize document to store
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());

        DocumentReference mDocRef = db.document("Login Data/"
                + userID +"/Carbon Tracking/Daily Totals/Product/" + date);

        Map<String, Object> newSurveyData = new HashMap<>();
        String PRODUCT_DAILY__KEY = date;

        newSurveyData.put(PRODUCT_DAILY__KEY, df.format(dailyProductCarbon));
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

    private void addProductCarbon (double productCarbon) {
        DecimalFormat df = new DecimalFormat("######.00");
        //set date format and initialize document to store
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        Date time = Calendar.getInstance().getTime();

        DocumentReference mDocRef = db.document("Login Data/"
                + userID +"/Carbon Tracking/Historical/Product/" + date);

        Map<String, Object> newSurveyData = new HashMap<>();
        String PRODUCT_KEY = time.toString();

        newSurveyData.put(PRODUCT_KEY, df.format(productCarbon));
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
                "Ripple Track: " + df.format(productCarbon) + " CO2e",
                Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}