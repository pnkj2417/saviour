package com.example.saviour;

import android.app.Application;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MainApplication extends Application {

    public static String status = null;
    public static String contactId=null;
    public static HashMap<String, LatLng> suspectLocationMap= new HashMap<>();
    public static String myLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            FileInputStream fis = this.openFileInput("myFile");
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                stringBuilder.append(line);
                setCredentials(stringBuilder.toString());
            } catch (IOException e) {
                Log.d("error message", "// Error occurred when opening raw file for reading.");
            }
        } catch (FileNotFoundException e) {
            Log.d("error", "// " +
                    "File not found");
        }
    }

    public HashMap<String ,LatLng> getSuspectLocationMap(){
        return suspectLocationMap;
    }

    private void setCredentials(String credentials){
        int index = credentials.indexOf(":");
        status=credentials.substring(0,index);
        contactId=credentials.substring(index+1);
    }
}
