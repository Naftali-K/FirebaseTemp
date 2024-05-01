package com.nk.firebasetemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * https://firebase.google.com/docs/crashlytics/get-started?platform=android#groovy
 *
 * Google Authentications
 * https://youtu.be/YQ0fJUiOYbY?si=rQkQC7LPw4qaJBJp - video lesson Google LogIn
 *
 * Push Notifications
 * https://youtu.be/m8vUFO5mFIM?si=oTyZqLOQuYjyUyMh - video lesson Push Notifications
 * https://youtu.be/M7z2MFoI6MQ?si=HAhaCButWe1j0Ytj - video lesson Push Notifications
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Test_code";

    private Button googleAuthBtn, appleAuthBtn, firebaseCrashlyticsBtn;


    private final String[] PERMISSIONS_LIST = new String[] {
            Manifest.permission.INTERNET,
            Manifest.permission.POST_NOTIFICATIONS
    };
    public static final int PERMISSION_REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseApp.initializeApp(getBaseContext());
        requestPermissions(this, getBaseContext());

        setReferences();
        getToken();

        googleAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), GoogleAuthenticationActivity.class));
            }
        });

        appleAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AppleAuthenticationActivity.class));
            }
        });

        firebaseCrashlyticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), FirebaseCrashlyticsActivity.class));
            }
        });
    }

    private void setReferences() {
        googleAuthBtn = findViewById(R.id.google_auth_btn);
        appleAuthBtn = findViewById(R.id.apple_auth_btn);
        firebaseCrashlyticsBtn = findViewById(R.id.firebase_crashlytics_btn);
    }

    public void requestPermissions(Activity activity, Context context) {
        Log.d(TAG, "requestPermissions: check permissions");

        if (!hasPermissions(context, PERMISSIONS_LIST)) {
            requestPermission(activity, PERMISSIONS_LIST);
        }
    }

    public boolean hasPermissions(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission: permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        Log.d(TAG, "hasPermissions: Permissions Exist");
        return true;
    }

    public void requestPermission(Activity activity, String[] permissions) {
        Log.d(TAG, "requestPermission: Ask permission: " + permissions.toString());
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQ_CODE);
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Something wrong with get token.");
                    return;
                }

                String token = task.getResult();
                Log.d(TAG, "onComplete: Firebase Token: \n" + token);
            }
        });
    }
}