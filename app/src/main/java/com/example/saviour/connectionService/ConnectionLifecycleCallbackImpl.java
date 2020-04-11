package com.example.saviour.connectionService;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.saviour.MainApplication;
import com.example.saviour.coordinates.AddCoordinates;
import com.example.saviour.mapAndLocation.LocationData;
import com.example.saviour.staticStrings.ConnectionServiceStrings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Payload;

import java.nio.charset.StandardCharsets;

public class ConnectionLifecycleCallbackImpl extends ConnectionLifecycleCallback {

    private Context context;
    private String status;
    private String NOT_GOOD = ConnectionServiceStrings.NOT_GOOD;
    private LocationData locationData;


    public ConnectionLifecycleCallbackImpl(Context context, String status) {
        this.context = context;
        this.status = status;
        locationData = new LocationData(context);
    }

    @Override
    public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
        PayloadCallbackImpl payloadCallback = new PayloadCallbackImpl(context);
        Log.i("onConnectionInitiated", "ConnectionInitiated....will send payload");
        Nearby.getConnectionsClient(context).acceptConnection(s, payloadCallback);
        locationData.setLocation();
    }

    @Override
    public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution result) {
        switch (result.getStatus().getStatusCode()) {
            case ConnectionsStatusCodes.STATUS_OK:
                Log.i(endpointId, "// We're connected! Can now start sending and receiving data.");
                LatLng latLng = AddCoordinates.getLatLng(MainApplication.myLocation);

                if (status.equals(NOT_GOOD)) {
                    while(true)
                    try {
                        Payload bytesPayload = Payload.fromBytes(MainApplication.myLocation.getBytes(StandardCharsets.UTF_8));
                        Nearby.getConnectionsClient(context).sendPayload(endpointId, bytesPayload);
                        Log.d("// got location data", latLng.latitude + "");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                Log.d("message", "// The connection was rejected by one or both sides.");
                break;
            case ConnectionsStatusCodes.STATUS_ERROR:
                Log.d("message", "The connection broke before it was able to be accepted");
                break;
            default:
                Log.d("message", "unknown result");
        }
    }

    @Override
    public void onDisconnected(@NonNull String s) {
        Log.i("////////////////////////////////onDisconnected", s);
        locationData.stopLocation();
    }
}
