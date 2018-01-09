package com.rickhuisman.musicapp.authentication;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rickhuisman.musicapp.MainActivity;
import com.rickhuisman.musicapp.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseFirestore firestoreDB;

    private DocumentReference mCurrentUserRef;

    private EditText mEmail;

    private EditText mUsername;

    private EditText mPassword;

    private Button mRegister;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mEmail = (EditText) findViewById(R.id.edit_text_email);
        mUsername = (EditText) findViewById(R.id.edit_text_user_name);
        mPassword = (EditText) findViewById(R.id.edit_text_password);
        mRegister = (Button) findViewById(R.id.button_register);

        mDialog = new ProgressDialog(this);

        firestoreDB = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null)
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        };

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    registerUser(email, username,password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Make sure to fill in all the fields.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registerUser(final String email, final String username, String password) {
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userUID = task.getResult().getUser().getUid();

                            mCurrentUserRef = firestoreDB.document("Users/" + userUID);

                            HashMap<String, Object> userInfo = new HashMap<String, Object>();
                            userInfo.put("Email", email);
                            userInfo.put("Username", username);

                            mCurrentUserRef.set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDialog.cancel();
                                    finish();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.toString());
                                    mDialog.cancel();

                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Log.d(TAG, task.getException().toString());

                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                        }
                    }
                });
    }
}