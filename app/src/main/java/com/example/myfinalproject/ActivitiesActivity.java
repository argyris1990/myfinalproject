package com.example.myfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.myfinalproject.Adapters.ActivitiesAdapter;

public class ActivitiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<String> dataIDs = new ArrayList<>();
    private DatabaseReference dataReference;

    private FirebaseUser user;

    private String petId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            Intent loginintent = new Intent(this, LoginActivity.class);
            startActivity(loginintent);
        }

        AppData appData = AppData.getInstance();
        petId = appData.getPetId();

        //Create path to custom datas reference
        dataReference = FirebaseDatabase.getInstance().getReference();
        dataReference = dataReference.child("Pets").child(user.getUid()).child("Pets").child(petId);
        dataReference = dataReference.child("Activities");

        //recyclerview adapter
        ActivitiesAdapter adapter = new ActivitiesAdapter(this, dataIDs, dataReference);

        recyclerView = findViewById(R.id.activitiesrecyclerview);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ImageButton add_new_activity = (ImageButton) findViewById(R.id.btnAddActivity);
        add_new_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitiesActivity.this, AddNewActivitiesActivity.class);
                startActivity(intent);
            }
        });


        ValueEventListener ActivitiesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataIDs.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String id = child.getKey();
                    dataIDs.add(id);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dataReference.addValueEventListener(ActivitiesListener);
    }
}
