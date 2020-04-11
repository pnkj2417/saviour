package com.example.saviour.coordinates;

import com.example.saviour.MainApplication;
import com.google.android.gms.maps.model.LatLng;

public class AddCoordinates {

    private MainApplication mainApplication = new MainApplication();

    public void addCoordinate(String coordinateString) {
        if (coordinateString.contains("nope")) {
            return;
        }
        int contactIndex = coordinateString.indexOf(":");
        String contact = coordinateString.substring(contactIndex + 1);
        mainApplication.getSuspectLocationMap().put(contact, getLatLng(coordinateString));
    }

    public static LatLng getLatLng(String coordinateString) {
        int contactIndex = coordinateString.indexOf(":");
        int index = coordinateString.indexOf("/");
        double longitude = Double.parseDouble(coordinateString.substring(0, index));
        double latitude = Double.parseDouble(coordinateString.substring(index + 1, contactIndex));
        return new LatLng(latitude, longitude);
    }

}
