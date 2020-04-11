package com.example.saviour.login;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.saviour.staticStrings.LoginActivityStaticResource;
import com.example.saviour.staticStrings.URLStrings;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class PostUserDataService extends IntentService {

    public PostUserDataService() {
        super("PostUserDataService");
    }

    private Context getContext() {
        return getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(intent.getStringArrayListExtra(LoginActivityStaticResource.userValues));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String userDetailUrl = URLStrings.postUserDetailsURL;
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, userDetailUrl,
                jsonObject,
                (JSONObject response) -> {
                    try {
                        Log.i("//////", response.getString("key"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestQueue.stop();
                },
                error -> {
                    Log.d("http Error", "///" + error.getMessage());
                });
        requestQueue.add(jsonObjectRequest);
    }

    private JSONObject getJsonObject(ArrayList<String> userValues) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        String[] keys = LoginActivityStaticResource.userKeys;
        int length = LoginActivityStaticResource.userKeys.length;
        for (int i = 0; i < length; i++) {

            jsonObject.put(keys[i], userValues.get(i));
        }
        return jsonObject;
    }

}
