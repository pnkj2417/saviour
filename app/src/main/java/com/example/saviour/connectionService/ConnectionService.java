package com.example.saviour.connectionService;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import com.example.saviour.staticStrings.ConnectionServiceStrings;
import com.example.saviour.staticStrings.MainActivityStaticStrings;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.Strategy;

public class ConnectionService extends IntentService {

    String message;
    Context context = this;
    Intent intent;
    private String GOOD = ConnectionServiceStrings.GOOD;
    private String NOT_GOOD = ConnectionServiceStrings.NOT_GOOD;

    void setIntent(Intent intent) {
        this.intent = intent;
    }

    private String getMessage(Intent intent) {
        return intent.getStringExtra(MainActivityStaticStrings.STATUS);
    }

    public ConnectionService() {
        super("ConnectionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        message = getMessage(intent);
        setIntent(intent);
        if (message.equals(GOOD))
            startAdvertising(GOOD);
        else if (message.equals(NOT_GOOD))
            startDiscovery(NOT_GOOD);
    }

    void startAdvertising(String myEndPoint) {
        ConnectionLifecycleCallbackImpl connectionLifecycleCallback =
                new ConnectionLifecycleCallbackImpl(this, message);
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build();
        Nearby.getConnectionsClient(context)
                .startAdvertising(
                        "myEndPoint", myEndPoint, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Log.i("rebecca", "We're advertising!");

                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.d("error", "We were unable to start advertising");
                        });
    }

    void startDiscovery(String myEndPoint) {
        EndpointDiscoveryCallbackImpl endpointDiscoveryCallback =
                new EndpointDiscoveryCallbackImpl(getApplicationContext(), message);
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build();
        Nearby.getConnectionsClient(context)
                .startDiscovery(GOOD, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Log.i("rebecca", "We're discovering!");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.d("error", "We can not start discovery!");
                        });
    }
}
