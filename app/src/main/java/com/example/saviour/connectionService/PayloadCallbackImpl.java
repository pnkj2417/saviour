package com.example.saviour.connectionService;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.saviour.MainApplication;
import com.example.saviour.connectionService.receiveAndBroadcast.BroadcastSender;
import com.example.saviour.coordinates.AddCoordinates;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

public class PayloadCallbackImpl extends PayloadCallback {

    private Context context;
    private AddCoordinates addCoordinates = new AddCoordinates();

    PayloadCallbackImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
        byte[] receivedBytes = payload.asBytes();
        String location = new String(receivedBytes);
        addCoordinates.addCoordinate(location);
        LatLng latLng1 = AddCoordinates.getLatLng(location);
        LatLng latLng2 = AddCoordinates.getLatLng(MainApplication.myLocation);
        if (Distance.calculateDistance(latLng1, latLng2) <= 10)
            BroadcastSender.broadcastSender(context);
        Log.i("// received data", s);
    }

    @Override
    public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate update) {
        Log.i("//sent data", update.toString());
    }

}
