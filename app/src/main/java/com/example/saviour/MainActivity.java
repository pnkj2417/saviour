package com.example.saviour;

import android.Manifest;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.saviour.connectionService.ConnectionService;
import com.example.saviour.connectionService.receiveAndBroadcast.BroadcastReceiverImp;
import com.example.saviour.login.LoginActivity;
import com.example.saviour.mapAndLocation.MapActivity;
import com.example.saviour.notificationService.NotificationService;
import com.example.saviour.staticStrings.ConnectionServiceStrings;
import com.example.saviour.staticStrings.MainActivityStaticStrings;
import com.example.saviour.web_view.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    IntentFilter statusIntentFilter = new IntentFilter(ConnectionServiceStrings.ACTION_CONTACTED);
    BroadcastReceiverImp broadcastReceiver = new BroadcastReceiverImp();
    private String GOOD = ConnectionServiceStrings.GOOD;
    private String NOT_GOOD = ConnectionServiceStrings.NOT_GOOD;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        if (MainApplication.status == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (MainApplication.status.equals(GOOD)) {
            setText("good to go");
            broadcastReceiver.setStatus(GOOD, this);
            Intent intent = new Intent(this, ConnectionService.class);
            intent.putExtra(MainActivityStaticStrings.STATUS, GOOD);
            startService(intent);
        } else {
            setText("you might be an infected person.");
            broadcastReceiver.setStatus(NOT_GOOD, this);
            Intent intent = new Intent(this, ConnectionService.class);
            intent.putExtra(MainActivityStaticStrings.STATUS, NOT_GOOD);
            startService(intent);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, statusIntentFilter);
        Intent notificationIntent = new Intent(this, NotificationService.class);
        startService(notificationIntent);
    }

    protected void onStart() {
        super.onStart();
        getDelegate().onStart();
        askLocationPermission();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, statusIntentFilter);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_toolbar:
                Intent infoIntent = new Intent(this, WebViewActivity.class);
                startActivity(infoIntent);
                return true;
            case R.id.mapInfo:
                Intent mapIntent = new Intent(this, MapActivity.class);
                startActivity(mapIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setText(String text) {
        TextView textView = findViewById(R.id.status);
        textView.setText(text);
    }

    public void askLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MainActivityStaticStrings.LOCATION_REQUEST_CODE);
    }

    void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setMessage("Location permission needed to show suspected near you.")
                .setTitle("Location permission")
                .setPositiveButton("OK", (dialog1, id) -> {
                    this.recreate();
                })
                .create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MainActivityStaticStrings.LOCATION_REQUEST_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("permission granted ", "///location permission");
                } else {
                    showLocationDialog();
                }
            }
        }
    }
}
