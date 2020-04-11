package com.example.saviour.connectionService;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;

public class EndpointDiscoveryCallbackImpl extends EndpointDiscoveryCallback {
    private Context context;
    private String message;

    EndpointDiscoveryCallbackImpl(Context context, String message) {
        this.context = context;
        this.message = message;
    }

    @Override
    public void onEndpointFound(@NonNull String endPoint, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
        ConnectionLifecycleCallbackImpl connectionLifecycleCallback =
                new ConnectionLifecycleCallbackImpl(context, message);
        Log.i("Endpoint found", "// will request a connection");
        Nearby.getConnectionsClient(context)
                .requestConnection("getUserNickname", endPoint, connectionLifecycleCallback)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Log.i("mssage", " We successfully requested a connection. Now both side must accept before the connection is established.");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.d("message", "// Nearby Connections failed to request the connection.");
                        });
    }

    @Override
    public void onEndpointLost(@NonNull String s) {
        Log.d("Error", "End point lost!");
    }
}
