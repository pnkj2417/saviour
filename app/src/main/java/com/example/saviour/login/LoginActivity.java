package com.example.saviour.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import com.example.saviour.MainActivity;
import com.example.saviour.MainApplication;
import com.example.saviour.R;
import com.example.saviour.staticStrings.LoginActivityStaticResource;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MainApplication.status == null)
            android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void onSubmit(View view) {
        TextView textView = findViewById(R.id.contactId);
        MainApplication.contactId=textView.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        if (textView.getText().toString().equals("")) {
            showAlert();
        } else if (MainApplication.status == null) {
            Switch aSwitch = findViewById(R.id.status);
            if (aSwitch.isChecked()) {
                MainApplication.status = "good";
            }
            else {
                MainApplication.status = "notGood";
            }
            Intent serviceIntent = new Intent(this, PostUserDataService.class);
            serviceIntent.putStringArrayListExtra(LoginActivityStaticResource.userValues, getUserValues());
            startService(serviceIntent);
            startActivity(intent);
            finish();
        }
    }

    private ArrayList<String> getUserValues() {
        int noUserValues = LoginActivityStaticResource.userIdValues.length;
        ArrayList<String> userValues = new ArrayList<>(noUserValues);
        for (int i = 0; i < noUserValues; i++) {
            TextView textView = findViewById(LoginActivityStaticResource.userIdValues[i]);
            userValues.add(textView.getText().toString());
        }
        return userValues;
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setMessage("app won't work without contact data")
                .setTitle("Contact  missing")
                .setPositiveButton("OK", (dialog1, id) -> {
                })
                .create();
        dialog.show();
    }
}
