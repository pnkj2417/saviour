package com.example.saviour.connectionService.receiveAndBroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.saviour.MainApplication;
import com.example.saviour.staticStrings.ConnectionServiceStrings;
import java.io.FileOutputStream;
import java.io.IOException;

public class BroadcastReceiverImp extends BroadcastReceiver {

    String NOT_GOOD=ConnectionServiceStrings.NOT_GOOD;
    @Override
    public void onReceive(Context context, Intent intent) {
       setStatus(NOT_GOOD, context);
       android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void setStatus(String status, Context context) {
        String filename = "myFile";
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write((status + ":" + MainApplication.contactId).getBytes());
        } catch (IOException e) {
            Log.d("Status writing failed", "// failed");
            e.printStackTrace();
        }
        MainApplication.status = status;
    }
}
