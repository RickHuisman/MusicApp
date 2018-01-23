package com.rickhuisman.musicapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rickhuisman.musicapp.R;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase realtimeDB;

    private DatabaseReference mCurrentUserRef;

    private EditText mEmail, mUsername, mPassword;

    private Button mSignUp;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.test);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mEmail = (EditText) findViewById(R.id.edit_text_email);
        mUsername = (EditText) findViewById(R.id.edit_text_user_name);
        mPassword = (EditText) findViewById(R.id.edit_text_password);
        mSignUp = (Button) findViewById(R.id.button_sign_up);

        mDialog = new ProgressDialog(this);

        realtimeDB = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null)
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        };

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    signUpUser(email, username,password);
                } else {
                    Toast.makeText(SignUpActivity.this, "Make sure to fill in all the fields.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signUpUser(final String email, final String username, String password) {
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userUID = task.getResult().getUser().getUid();

                            mCurrentUserRef = realtimeDB.getReference("Users/" + userUID);

                            HashMap<String, Object> userInfo = new HashMap<String, Object>();
                            userInfo.put("Email", email);
                            userInfo.put("Username", username);

                            mCurrentUserRef.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDialog.cancel();
                                    finish();
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.toString());
                                    mDialog.cancel();

                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Log.d(TAG, task.getException().toString());

                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                        }
                    }
                });
    }
}