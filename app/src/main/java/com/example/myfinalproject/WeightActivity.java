package com.example.myfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.myfinalproject.Adapters.WeightAdapter;
import com.example.myfinalproject.model.Pet;

public class WeightActivity extends AppCompatActivity {

    private ListView listview1;

    ArrayList<String> arrayList;

    ArrayAdapter adapter;

    private Map<Long, String> keyList = new HashMap<>();
    private ImageButton imagebutton;
    private DatabaseReference petIdRefrence;
    private DatabaseReference mDatabase;
    private String petId;
    private FirebaseUser fireuser;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_weight);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        //Get pets id
        petId =  petId = AppData.getInstance().getPetId();

        // ImageButton to move AddWeightDataActivity
        ImageButton btnplus = (ImageButton) findViewById(R.id.btnAddData);
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeightActivity.this, AddWeightDataActivity.class);
                startActivity(intent);
            }
        });

        //Get currend user from firebase instance
        fireuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fireuser == null) {
            // Not signed in, launch the Sign In activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        List<String[]> stringArrayList = new ArrayList<String[]>();

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.weightrecyclerview);

        WeightAdapter adapter = new WeightAdapter(this, stringArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration((new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)));


        //Set refrence to pets id
        petIdRefrence = FirebaseDatabase.getInstance().getReference().child("Pets").child(fireuser.getUid()).child("Pets").child(petId);

        ValueEventListener petListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stringArrayList.clear();
                for (DataSnapshot child : snapshot.getChildren()){

                    Map<String, Object> weightmap = (Map<String, Object>) child.getValue();

                    String date = (String) weightmap.get("date");
                    String weight = (String) weightmap.get("weight");
                    String[] weightlist = new String[2];
                    weightlist[0] = date;
                    weightlist[1] = weight;

                    stringArrayList.add(weightlist);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference petsref = mDatabase.child("Pets").child(fireuser.getUid()).child("Pets").child(petId).child("Weight");
        petsref.addValueEventListener(petListener);
    }
}
