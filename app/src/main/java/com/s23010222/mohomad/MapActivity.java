package com.s23010222.mohomad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText addressInput;
    private Button btnSearch, btnZoomIn, btnZoomOut, btnMyLocation, btnToSensor;
    private FusedLocationProviderClient fusedLocationClient;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        addressInput = findViewById(R.id.addressInput);
        btnSearch = findViewById(R.id.btnSearch);
        btnZoomIn = findViewById(R.id.btnZoomIn);
        btnZoomOut = findViewById(R.id.btnZoomOut);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        btnToSensor = findViewById(R.id.btnToSensor);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnSearch.setOnClickListener(v -> {
            String address = addressInput.getText().toString();
            if (!address.isEmpty()) {
                Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    if (!addresses.isEmpty()) {
                        Address loc = addresses.get(0);
                        double lat = loc.getLatitude();
                        double lng = loc.getLongitude();

                        LatLng latLng = new LatLng(lat, lng);
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Searched Location"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                        Log.d("Geocoding", "Lat: " + lat + ", Lng: " + lng);
                    } else {
                        Toast.makeText(MapActivity.this, "Location not found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MapActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MapActivity.this, "Enter an address", Toast.LENGTH_SHORT).show();
            }
        });

        btnZoomIn.setOnClickListener(v -> mMap.animateCamera(CameraUpdateFactory.zoomIn()));
        btnZoomOut.setOnClickListener(v -> mMap.animateCamera(CameraUpdateFactory.zoomOut()));

        btnMyLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
                    } else {
                        Toast.makeText(this, "Couldn't get location", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
        });

        btnToSensor.setOnClickListener(v -> {
            progressDialog = new ProgressDialog(MapActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new Handler().postDelayed(() -> {
                progressDialog.dismiss();
                startActivity(new Intent(MapActivity.this, SensorActivity.class));
            }, 3000);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }
}
