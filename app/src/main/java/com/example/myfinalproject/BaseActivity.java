package com.example.myfinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class BaseActivity extends AppCompatActivity {

    protected FirebaseUser user;
    protected String petId;
    protected String userID;


    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Initialize Firebase and check if the user is signed in
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        else{
            userID = user.getUid();
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.app_name));

        //Add back button
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
