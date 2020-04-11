package com.example.saviour.mapAndLocation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.saviour.MainApplication;
import com.example.saviour.R;
import com.example.saviour.coordinates.AddCoordinates;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    LatLng latLng;
    HashMap<String, LatLng> markerHashMap = new MainApplication().getSuspectLocationMap();
    LocationData locationData;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationData= new LocationData(getApplicationContext());
        locationData.setLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addCircle(getCircleOptions());
        googleMap.animateCamera(getCameraUpdate());
        setMarker(googleMap);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationData.stopLocation();
    }

    private CameraUpdate getCameraUpdate() {
        return CameraUpdateFactory.newLatLngZoom(latLng, 18);
    }

    private CircleOptions getCircleOptions() {
        CircleOptions circleOptions = new CircleOptions();
        AddCoordinates.getLatLng(MainApplication.myLocation);
        circleOptions.center(latLng).radius(100).strokeColor((0xffcccccc));
        return circleOptions;
    }

    private void setMarker(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        int length = markerHashMap.size();
        if (length != 0)
            for (LatLng latLng : markerHashMap.values()) {
                googleMap.addMarker(new MarkerOptions().position(latLng));
            }
    }
}
