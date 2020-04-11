package com.example.saviour.connectionService;

import com.google.android.gms.maps.model.LatLng;

public class Distance {

    public static double calculateDistance(LatLng latLng1, LatLng latLng2) {
        double lon1 = Math.toRadians(latLng1.longitude);
        double lon2 = Math.toRadians(latLng2.longitude);
        double lat1 = Math.toRadians(latLng1.latitude);
        double lat2 = Math.toRadians(latLng2.latitude);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371;
        return (c * r) * 1000;
    }
}
