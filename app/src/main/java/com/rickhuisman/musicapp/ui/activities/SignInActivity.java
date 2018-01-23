package com.rickhuisman.musicapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rickhuisman.musicapp.R;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView mRegisterText;

    private EditText mEmail, mPassword;

    private Button mSignIn;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.test);
        setSupportActionBar(toolbar);

        mRegisterText = (TextView) findViewById(R.id.text_view_sign_up);
        mEmail = (EditText) findViewById(R.id.edit_text_email);
        mPassword = (EditText) findViewById(R.id.edit_text_password);
        mSignIn = (Button) findViewById(R.id.button_sign_in);

        mDialog = new ProgressDialog(this);

        SpannableString ss = new SpannableString("No account yet? create one");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 16, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mRegisterText.setText(ss);
        mRegisterText.setMovementMethod(LinkMovementMethod.getInstance());
        mRegisterText.setHighlightColor(Color.TRANSPARENT);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null)
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        };

        mSignIn.setOnClickListener(loginButton());

        mRegisterText.setOnClickListener(registerTextButton());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public View.OnClickListener loginButton() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    signInUser(email, password);
                } else {
                    Toast.makeText(SignInActivity.this, "Make sure to fill in all the fields.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        return onClickListener;
    }

    public View.OnClickListener registerTextButton() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        };
        return onClickListener;
    }

    public void signInUser(String email, String password) {
        mDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mDialog.cancel();
                            finish();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                        }
                    }
                });
    }
}
