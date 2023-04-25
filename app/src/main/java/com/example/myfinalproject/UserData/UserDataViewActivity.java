package com.example.myfinalproject.UserData;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfinalproject.UserData.Adapter.UserDataViewAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import com.example.myfinalproject.AppData;
import com.example.myfinalproject.BaseActivity;
import com.example.myfinalproject.PetView;
import com.example.myfinalproject.R;

public class UserDataViewActivity extends BaseActivity {


    private String header;
    private List<String> fields = new ArrayList<>();
    // fields is searched from the adapter

    private List<String> customDataIDs = new ArrayList<>();
    private FirebaseUser user;
    private DatabaseReference customDataRefrence;
    private String petId;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_data);


        AppData appData = AppData.getInstance();
        header = appData.getDataHeader();
        petId = appData.getPetId();

        //Create path to custom datas reference
        customDataRefrence = FirebaseDatabase.getInstance().getReference();
        customDataRefrence = customDataRefrence.child("Pets").child(userID).child("Pets").child(petId);
        customDataRefrence = customDataRefrence.child("CustomData").child(header);

        //recyclerview adapter
        UserDataViewAdapter adapter = new UserDataViewAdapter(this, customDataIDs, customDataRefrence);

        recyclerView = findViewById(R.id.custom_data_view_recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ValueEventListener customDataSetListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customDataIDs.clear();

                for(DataSnapshot child : snapshot.getChildren()){
                    String id = child.getKey();
                    customDataIDs.add(id);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        customDataRefrence.addValueEventListener(customDataSetListener);


        ImageButton addDataButton = (ImageButton) findViewById(R.id.buttonAddCustomData);
        addDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get and set required fields
                ArrayList<String> fields = (ArrayList<String>) adapter.getFields();
                AppData appData = AppData.getInstance();
                appData.setFields(fields);
                appData.setDataHeader(header);

                //Go to add activity
                Intent intent = new Intent(UserDataViewActivity.this, UserDataAddActivity.class);
                startActivity(intent);
            }
        });


        TextView textView = (TextView) findViewById(R.id.textViewCustomView);
        textView.setText(header);

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(UserDataViewActivity.this, PetView.class);
        startActivity(intent);
    }


}
