package com.example.myfinalproject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import com.example.myfinalproject.Adapters.PetItemViewAdapter;

public class MainActivity extends BaseActivity {

    RecyclerView recyclerView;

    private List<String> petIdKeyList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Delete back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        DatabaseReference baseRefrence = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets");

        recyclerView = findViewById(R.id.recyclerView);

        // pet names from firebase and saving them

        PetItemViewAdapter adapter = new PetItemViewAdapter(this, petIdKeyList, R.drawable.ic_action_pets, baseRefrence);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ValueEventListener petListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> newPetIdKeyList = new ArrayList<String>();

                petIdKeyList.clear();

                for(DataSnapshot child : snapshot.getChildren()){
                    String id = child.getKey().toString();
                    newPetIdKeyList.add(id);
                }
                petIdKeyList.addAll(newPetIdKeyList);

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        baseRefrence.addValueEventListener(petListListener);

        // the add pet window
        ImageButton add_pet_activity = (ImageButton) findViewById(R.id.btnAddPet);
        add_pet_activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPetActivity.class);
                startActivity(intent);
            }
        });
    }
}