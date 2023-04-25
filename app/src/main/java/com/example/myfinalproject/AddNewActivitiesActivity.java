package com.example.myfinalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewActivitiesActivity extends AppCompatActivity {

    private FirebaseUser fireuser;
    private DatabaseReference mDatabase;
    private String petId;
    private EditText editText1;
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_add);

        // pic the date from calendar
        editText1 = findViewById(R.id.txtDate);
        Calendar calendar = Calendar.getInstance();
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewActivitiesActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;
                        String date= dayOfMonth + "/" + month + "/" + year;
                        editText1.setText(date);
                    }
                },year, month, day);
                datePickerDialog.show();

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Get pets id from intent
        Intent intent = getIntent();
        petId = AppData.getInstance().getPetId();

        // checks if the user is still logged in
        fireuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fireuser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
        }

        // pressing the button activity and date are added
        Button add_activity= (Button) findViewById(R.id.btnAddActivity);
        add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    public void send() { // Adding new activity

        EditText edittext1 = (EditText) findViewById(R.id.txtDate); // date
        String date = edittext1.getText().toString();
        EditText edittext2 = (EditText) findViewById(R.id.txtType); // type
        String type = edittext2.getText().toString();
        EditText edittext3 = (EditText) findViewById(R.id.txtLength); // length
        String length = edittext3.getText().toString();

        // All fields must be filled in
        if(edittext1.getText().length() == 0  || edittext2.getText().length() == 0 || edittext3.getText().length() == 0) {
            Toast toast = Toast.makeText(this, getString(R.string.fill_in_the_required_fields), Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            // which location to push
            DatabaseReference petsref = mDatabase.child("Pets").child(fireuser.getUid()).child("Pets").child(petId);
            petsref = petsref.child("Activities");

            Map<String, Object> activitymap = new HashMap<>();
            activitymap.put("date", date);  // "dd.mm.yyyy"
            activitymap.put("type", type);
            activitymap.put("length", length); // Minutes

            //push a unique id is created and its location is obtained and then the value is set
            DatabaseReference ref = petsref.push();
            ref.setValue(activitymap);

            Toast toast = Toast.makeText(this, getString(R.string.new_activity_added),Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }
}
