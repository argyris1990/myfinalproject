package com.example.myfinalproject.UserData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myfinalproject.UserData.Adapter.UserDataCreateAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.myfinalproject.AppData;
import com.example.myfinalproject.R;


public class UserDataCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_data);

        //Set up views
        RecyclerView customRecyclerView = findViewById(R.id.customDataRecyclerView);
        EditText textInputHeader = (EditText) findViewById(R.id.textInputHeader);

        //create empty arraylist for the first item
        List<String> contents = new ArrayList<>();
        contents.add(0,"");

        //Initialize adapter and set it to recyclerview
        UserDataCreateAdapter adapter = new UserDataCreateAdapter(contents);
        customRecyclerView.setAdapter(adapter);
        customRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set button click listener to add another field
        Button buttonAddField = (Button) findViewById(R.id.buttonAddField);
        buttonAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add row to contents
                contents.add("");
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
            }
        });

        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save Header
                String dataHeader = textInputHeader.getText().toString();

                //Get all fields data and sort them alphabetically
                List<String> saveContents = adapter.getContents().stream().sorted().collect(Collectors.toList());
                //Go to add activity
                Intent intent = new Intent(UserDataCreateActivity.this, UserDataAddActivity.class);
                //Set appDate fields
                AppData appData = AppData.getInstance();
                appData.setFields((ArrayList<String>) saveContents);
                appData.setDataHeader(dataHeader);

                startActivity(intent);
            }
        });
    }
}
