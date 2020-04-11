package com.example.saviour.mapAndLocation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.location.LocationManager;
import android.os.Looper;
import androidx.appcompat.app.AlertDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationData {
    Context context;
    FusedLocationProviderClient fusedLocationClient;
    public static int count = 0;
    private LocationCallbackImpl locationCallback;

    public LocationData(Context context) {
        this.context = context;
        if (fusedLocationClient == null)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationCallback = new LocationCallbackImpl();
        count++;
    }

    public void setLocation() {
        enableGPS(context);
        fusedLocationClient.requestLocationUpdates(
                getLocationRequest(), locationCallback,
                Looper.myLooper());
    }

    public void stopLocation() {
        if (count == 1) {
            count--;
            fusedLocationClient.removeLocationUpdates(locationCallback);
        } else if (count >= 2)
            count--;
    }

    private void enableGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(context)
                    .setTitle("GPS not found")
                    .setMessage("Please enable GPS to use this service")
                    .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        return locationRequest;
    }
}
