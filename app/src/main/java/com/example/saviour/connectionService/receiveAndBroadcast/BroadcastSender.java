package com.example.saviour.connectionService.receiveAndBroadcast;

import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.saviour.staticStrings.ConnectionServiceStrings;

public class BroadcastSender {

    public static void broadcastSender(Context context) {
        Intent localIntent =
                new Intent(ConnectionServiceStrings.ACTION_CONTACTED);
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
    }
}
