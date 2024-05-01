package com.nk.firebasetemp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * https://qiita.com/yuskey/items/e98c12735fb8806f54aa - instructions how set right Apple account for Firebase
 * https://support.aiir.com/article/953-locating-apple-team-details - where takes Apple team Id
 * https://stackoverflow.com/questions/66829637/firebase-auth-apple-sign-in-for-web-invalid-client-error - where from takes which keys
 *
 * https://developer.apple.com/account/resources/identifiers/list
 */
public class AppleAuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apple_authentication);
    }
}