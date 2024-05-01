package com.nk.firebasetemp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirebaseCrashlyticsActivity extends AppCompatActivity {

    private Button crashBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_crashlytics);
        setReferences();

        crashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });
    }

    private void setReferences() {
        crashBtn = findViewById(R.id.crash_btn);
    }
}