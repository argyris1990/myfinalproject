package com.example.myfinalproject.UserData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myfinalproject.UserData.Adapter.UserDataAddAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.myfinalproject.AppData;
import com.example.myfinalproject.LoginActivity;
import com.example.myfinalproject.R;


public class UserDataAddActivity extends AppCompatActivity {

    private RecyclerView addDataRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_data);

        //Get data from appData singleton.
        AppData appData = AppData.getInstance();
        String dataHeader = appData.getDataHeader();
        String petId = appData.getPetId();
        ArrayList<String> fields = appData.getFields();


        //User login check
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        //Set reference to pets id
        DatabaseReference petIdReference = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets").child(petId);

        //Recyclerview initialization
        addDataRecyclerview = findViewById(R.id.custom_data_add_recyclerView);
        //adapter for recyclerview
        UserDataAddAdapter adapter = new UserDataAddAdapter(fields);
        addDataRecyclerview.setAdapter(adapter);
        addDataRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        //Set Header text
        TextView header = (TextView) findViewById(R.id.textViewHeader);
        header.setText(dataHeader);



        Button save = (Button) findViewById(R.id.buttonSave);
        //Button saves editext contens to firedatabes under UserData/Header/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Field and fielData
                List<String> data =adapter.getEditTextFields();
                List<String> fields = adapter.getFields();

                //Make map from field and field datas
                Map<String, Object> datamap = new HashMap<>();

                for (int i = 0 ; i < data.size(); i++){
                    datamap.put(fields.get(i).toString(), data.get(i).toString());
                }

                //Push and set values to database
                DatabaseReference customDataReference = petIdReference.child("CustomData").child(dataHeader).push();
                customDataReference.setValue(datamap);

                //Go to view activity
                Intent intent = new Intent(UserDataAddActivity.this, UserDataViewActivity.class);
                startActivity(intent);
            }
        });

    }

}
