package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class HomeFragment extends androidx.fragment.app.Fragment implements View.OnClickListener {

    private static final String TAG = "Home Activity";
    private static final String userID = LoginActivity.getData().get("User Id");
    public float travelValue;
    public float serviceValue;
    public float productValue;

    Boolean isStreak;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);


        //DocumentReference mDocRef = db.document("Login Data/" + MainActivity.getIdUser() + "/Carbon Tracking/Survey");

        //Initialize buttons for changing insight pie chart
        Button today = view.findViewById(R.id.insight_today);
        Button week = view.findViewById(R.id.insight_week);
        Button month = view.findViewById(R.id.insight_month);

        today.setOnClickListener(this);
        week.setOnClickListener(this);
        month.setOnClickListener(this);

        TextView textView = view.findViewById(R.id.ripple_streaks);
        TextView textView1 = view.findViewById(R.id.residence_breakdown);
        TextView textView2 = view.findViewById(R.id.food_breakdown);
        TextView textView3 = view.findViewById(R.id.commerce_breakdown);
        TextView textView4 = view.findViewById(R.id.transport_breakdown);

        textView.setText("Ripple Streaks");
        textView1.setText("From housing, you emitted a total of 1200 CO2 eq");
        textView2.setText("From food, you emitted a total of 800 CO2 eq");
        textView3.setText("From commerce, you emitted a total of 200 CO2 eq");
        textView4.setText("From transportation, you emitted a total of 1000 CO2 eq");
        return view;
    }

    //method for button action
    @Override
    public void onClick(View view) {
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String [] dailySubTopic = {
                "Product",
                "Service",
                "Travel"
        };
        final Map <String, String> myMap = new HashMap<String, String>();

        for (int s = 0; s < dailySubTopic.length; s++) {
            final String branch = dailySubTopic[s];
            final String DAILY_VAL = "0.00";
            String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
            DocumentReference dailyTotalDoc = db.document("Login Data/"
                    + userID +"/Carbon Tracking/Daily Totals/" + branch + "/" + date);

            dailyTotalDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    myMap.put(branch, documentSnapshot.getString(DAILY_VAL));
                }
            }).addOnFailureListener(
                    new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    myMap.put(branch, "0.00");
                }
            });
        }
         */

        switch (view.getId()) {
            case R.id.insight_today:
                serviceValue = 200f;
                travelValue = 300f;
                productValue = 150f;
                break;
            case R.id.insight_week:
                serviceValue = 600f;
                travelValue = 500f;
                productValue = 550f;
                break;
            case R.id.insight_month:
                serviceValue = 1200f;
                travelValue = 3200f;
                productValue = 1250f;
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView nameDisplay;

        PieChartView pieChartView = view.findViewById(R.id.chart);

        List<SliceValue> pieData = new ArrayList<SliceValue>() {
            {
                add(new SliceValue(10, R.color.bermuda));
                add(new SliceValue(50, R.color.nightSky));
                add(new SliceValue(30, R.color.springGreen));
                add(new SliceValue(10, R.color.buttonBlue));
            }
        };

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true)
                //.setCenterText1(String.valueOf(serviceValue))
                .setCenterText2("Total = #### CO2eq")
                .setCenterText1FontSize(15)
                .setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);

        //Options for generating a custom greeting for the user
        int randInt = (int)(5 * Math.random()) + 1;

        String [] greetingsList = {
                "Welcome back, ",
                "Howdy, ",
                "Hi ",
                "Hey ",
                "Hello ",
                "Greetings, "
        };

        String greeting = greetingsList[randInt];

        nameDisplay = view.findViewById(R.id.name_display);
        //nameDisplay.setText(greeting + MainActivity.getIdUser() + "!");
       // Log.d(TAG, USER_ID);
        nameDisplay.setText(greeting + LoginActivity.getData().get("Name") + "!");
    }


    /*
    static Map<String, String> getDailyData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String [] dailySubTopic = {
                "Product",
                "Service",
                "Transport"
        };

        final Map <String, String> myMap = new HashMap<String, String>();

        for (int s = 0; s < dailySubTopic.length; s++) {
            final String branch = dailySubTopic[s];
            final String DAILY_VAL = "0";
            String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
            DocumentReference dailyTotalDoc = db.document("Login Data/"
                    + userID +"/Carbon Tracking/Daily Totals/" + branch + "/" + date);
            dailyTotalDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    myMap.put(branch, documentSnapshot.getString(DAILY_VAL));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    myMap.put(branch, "0");
                }
            });

        }
        return myMap;
    }
    */

}
