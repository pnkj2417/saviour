package com.example.saviour.mapAndLocation;

import android.util.Log;
import com.example.saviour.MainApplication;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

public class LocationCallbackImpl extends LocationCallback {
    @Override
    public void onLocationResult(LocationResult locationResult) {
        double latitude = locationResult.getLastLocation().getLatitude();
        double longitude = locationResult.getLastLocation().getLongitude();
        MainApplication.myLocation = longitude + "/" + latitude + ":" + MainApplication.contactId;
        Log.i("//location", latitude + "");
    }
}
