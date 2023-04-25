package com.example.myfinalproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddFeedActivity extends AppCompatActivity {

    private FirebaseUser fireuser;
    private DatabaseReference dataReference;
    private String petId;
    private EditText edittext1;
    private EditText edittext2;
    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feed);

        edittext2 = findViewById(R.id.edit_time);
        Calendar calendar1 = Calendar.getInstance();
        edittext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours = calendar1.get(Calendar.HOUR);
                minutes = calendar1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog =new TimePickerDialog(AddFeedActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edittext2.setText(hourOfDay+":"+ minute);
                    }
                }, hours,minutes,false);
                timePickerDialog.show();

            }
        });
        // pic the date from calendar
        edittext1 = findViewById(R.id.edit_date);
        Calendar calendar2 = Calendar.getInstance();
        edittext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar2.get(Calendar.YEAR);
                month = calendar2.get(Calendar.MONTH);
                day = calendar2.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddFeedActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;
                        String date= dayOfMonth + "/" + month + "/" + year;
                        edittext1.setText(date);
                    }
                },year, month, day);
                datePickerDialog.show();

            }
        });

        dataReference = FirebaseDatabase.getInstance().getReference();

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

        // new food is added by pressing the button
        Button btn_sendfood = (Button) findViewById(R.id.btn_sendfood);
        btn_sendfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendfeeding();
            }
        });
    }


    public void sendfeeding() { // Adding new feeding

        EditText edittext1 = (EditText) findViewById(R.id.edit_date); // day
        String date = edittext1.getText().toString();
        EditText edittext2 = (EditText) findViewById(R.id.edit_time); // time
        String time = edittext2.getText().toString();
        EditText edittext3 = (EditText) findViewById(R.id.edit_food); // food
        String food = edittext3.getText().toString();

        // All fields must be filled
        if(edittext1.getText().length() == 0  || edittext2.getText().length() == 0 || edittext3.getText().length() == 0) {
            Toast toast = Toast.makeText(this, getString(R.string.fill_in_the_required_fields), Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            // to which location to push
            dataReference = dataReference.child("Pets").child(fireuser.getUid()).child("Pets").child(petId);
            dataReference = dataReference.child("Feeding");

            Map<String, Object> foodmap = new HashMap<>();
            foodmap.put("date", date);  // "dd/mm/yyyy"
            foodmap.put("time", time);
            foodmap.put("food", food);

            //push a unique id is created and its location is obtained and then the value is set
            DatabaseReference ref = dataReference.push();
            ref.setValue(foodmap);

            Toast toast = Toast.makeText(this, getString(R.string.new_feeding_added),Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }
}
