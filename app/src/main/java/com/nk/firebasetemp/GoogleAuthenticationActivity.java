package com.nk.firebasetemp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * https://youtu.be/YQ0fJUiOYbY?si=rQkQC7LPw4qaJBJp - video lesson
 */

public class GoogleAuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "Test_code";

    private TextView userIdTv, userEmailTv, userNameTv;
    private SignInButton googleAuthBtn;
    private Button logOutBtn;

    private ActivityResultLauncher googleSignInResultLauncher;

    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_authentication);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        setReferences();
        setResultLaunchers();
        userIsCurrentlySigned();


        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.DEFAULT_WEB_CLIENT_ID))
                .requestEmail()
                .requestId()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getBaseContext(), options);
        auth = FirebaseAuth.getInstance();

        googleAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                googleSignInResultLauncher.launch(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    private void setReferences() {
        userIdTv = findViewById(R.id.user_id_tv);
        userEmailTv = findViewById(R.id.user_email_tv);
        userNameTv = findViewById(R.id.user_name_tv);
        googleAuthBtn = findViewById(R.id.google_auth_btn);
        logOutBtn = findViewById(R.id.log_out_btn);
    }

    private void setResultLaunchers() {
        googleSignInResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() != RESULT_OK) {
                    return;
                }

                if (o.getData() == null) {
                    return;
                }

                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(o.getData());

                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = auth.getCurrentUser();
                                setUserValues();
                            } else {
                                Log.d(TAG, "onComplete: Something wrong.");
                            }
                        }
                    });
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void userIsCurrentlySigned() {
        user = auth.getCurrentUser();
        if (user != null) {
            setUserValues();
            Log.d(TAG, "onStart: User already login.");
        }
    }

    private void setUserValues() {
        String userId = user.getUid();
        String userEmail = user.getEmail();
        String userName = user.getDisplayName();

        userIdTv.setText(userId);
        userEmailTv.setText(userEmail);
        userNameTv.setText(userName);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(getBaseContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();

        userIdTv.setText(null);
        userEmailTv.setText(null);
        userNameTv.setText(null);
    }
}