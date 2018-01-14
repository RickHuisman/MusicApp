package com.rickhuisman.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rickhuisman.musicapp.authentication.StartActivity;
import com.rickhuisman.musicapp.library.LibraryFragment;
import com.rickhuisman.musicapp.uploadSong.UploadSongActivity;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportActionBar().setTitle(R.string.title_home);
                    return true;
                case R.id.navigation_explore:
                    getSupportActionBar().setTitle(R.string.title_explore);
                    return true;
                case R.id.navigation_library:
                    getSupportActionBar().setTitle(R.string.title_library);
                    transaction.replace(R.id.frame_layout_container, new LibraryFragment()).commit();
                    return true;
                case R.id.navigation_now_playing:
                    getSupportActionBar().setTitle(R.string.title_now_playing);
                    transaction.replace(R.id.frame_layout_container, new LibraryFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation_view_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set first fragment and set the title to Library
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout_container, new LibraryFragment()).commit();
        getSupportActionBar().setTitle(R.string.title_library);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    System.out.println("User logged in"); // TODO handle auth
                }
                else{
                    System.out.println("User not logged in"); // TODO handle auth
                }
            }
        };
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:

                return true;
            case R.id.action_log_out:
                mAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                return true;
            case R.id.action_add_song:
                finish();
                startActivity(new Intent(MainActivity.this, UploadSongActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
