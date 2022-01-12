package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransportationActivity extends FragmentActivity implements OnMapReadyCallback {
    private final String userID = LoginActivity.getData().get("User Id");
    private static final String TAG = "Transportation";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public double dailyTransportCarbon;

    GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    Button dialogBtn;
    int locationCount;
    LatLng latlng1, latlng2;
    String flightType = "Domestic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        final Switch switchButton = findViewById(R.id.switch_flight_type);
        switchButton.setVisibility(View.INVISIBLE);

        if (EntryFragment.transportation == 1) {
            switchButton.setVisibility(View.VISIBLE);
            switchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switchButton.isChecked()) {
                        flightType = "Domestic";
                    } else {
                        flightType = "International";
                    }
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        searchView = findViewById(R.id.search_map);
        locationCount = 0;
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        displayToast("Enter Start Location");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                Geocoder geocoder = new Geocoder(TransportationActivity.this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert addressList != null;
                Address address = addressList.get(0);
                locationCount ++;
                if (locationCount == 1) {
                    latlng1 = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latlng1).title("Start Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng1, 15));
                    displayToast("Enter End Location");
                } else {
                    latlng2 = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latlng2).title("End Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng2, 15));
                }
                searchView.setQuery("", false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void transportCalc (LatLng a, LatLng b) {
        double distance = SphericalUtil.computeDistanceBetween(a,b) / 1000;
        displayToast("Distance travelled: " + distance + " km");
        double transportCO2e;
        final double AV_CAR_FUEL_CONSUMPTION_RATE = 0.089, CO2E_KG = 2.347
                ,AV_DOMESTIC_FUEL_CONSUMPTION_RATE = 0.160
                ,AV_INTL_FUEL_CONSUMPTION_RATE = 0.095;

        if (EntryFragment.transportation == 0) {
            transportCO2e = (distance * AV_CAR_FUEL_CONSUMPTION_RATE) * CO2E_KG;
        } else {
            if (flightType.equals("Domestic") && distance < 500) {
                transportCO2e = distance * AV_DOMESTIC_FUEL_CONSUMPTION_RATE;
            } else {
                transportCO2e = distance * AV_INTL_FUEL_CONSUMPTION_RATE;
            }
        }
        displayToast("Ripple Track: " + transportCO2e + " CO2e");
        addData(transportCO2e);
        dailyTransportCarbon += transportCO2e;
        addDailyTransportCarbon(dailyTransportCarbon);
    }

    private void addDailyTransportCarbon(double dailyTransportCarbon) {
        DecimalFormat df = new DecimalFormat("######.00");
        //set date format and initialize document to store
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());

        DocumentReference mDocRef = db.document("Login Data/"
                + userID +"/Carbon Tracking/Daily Totals/Transport/" + date);

        Map<String, Object> newSurveyData = new HashMap<>();
        String TRANSPORT_DAILY__KEY = date;

        newSurveyData.put(TRANSPORT_DAILY__KEY, df.format(dailyTransportCarbon));
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

    public void transportTrack (View v) {
        // On success, display toast message and clear input options
        transportCalc(latlng1, latlng2);
        displayToast("Ripple Track: " + " CO2e");
    }

    // Clears map markers
    public void onClear (View v) {
        locationCount = 0;
        mMap.clear();
    }

    public void displayToast (String message) {
        Toast toast = Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 300);
        toast.show();
    }

    private void addData (double transportCO2e) {
        DecimalFormat df = new DecimalFormat("######.00");
        //set date format and initialize document to store
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        Date time = Calendar.getInstance().getTime();

        DocumentReference mDocRef = db.document("Login Data/"
                + userID +"/Carbon Tracking/Historical/Travel/" + date);

        Map<String, Object> newSurveyData = new HashMap<>();
        String TRANSPORT_KEY = time.toString();

        newSurveyData.put(TRANSPORT_KEY, df.format(transportCO2e));
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
