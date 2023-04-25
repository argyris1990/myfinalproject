package com.example.myfinalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.example.myfinalproject.model.Pet;

public class AddPetActivity extends AppCompatActivity {

    private FirebaseUser fireuser;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ImageView imageView;
    private String imageUrl;
    private Uri uriimage;
    private DatabaseReference petsReference;
    private EditText editText4;
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet);

        // pic the date from calendar
        editText4 = findViewById(R.id.txtInput4);
        Calendar calendar = Calendar.getInstance();
        editText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;
                        String date= dayOfMonth + "/" + month + "/" + year;
                        editText4.setText(date);
                    }
                },year, month, day);
                datePickerDialog.show();

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // checks if the user is still logged in
        fireuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fireuser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        //looking for references
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child(fireuser.getUid()).child("images");

        // Press the button to add a new pet
        Button add_pet = (Button) findViewById(R.id.btnAdd);
        add_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(add()){
                    if(uriimage != null) {
                        uploadImage(uriimage);
                    }

                    Intent intent = new Intent(AddPetActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addImage();
            }
        });
    }

    public boolean add() { // Adding new pet

        EditText editText1 = (EditText) findViewById(R.id.txtInput1); // name
        String name = editText1.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.txtInput2); // species
        String species = editText2.getText().toString();
        EditText editText3 = (EditText) findViewById(R.id.txtInput3); // breed
        String breed = editText3.getText().toString();
        EditText editText4 = (EditText) findViewById(R.id.txtInput4); // date of birth
        String date_of_birth = editText4.getText().toString();

        // All fields must be filled in
        if(editText1.getText().length() == 0  || editText2.getText().length() == 0 || editText3.getText().length() == 0 || editText4.getText().length() == 0) {
            Toast toast = Toast.makeText(this, getString(R.string.fill_in_the_required_fields),Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        // When all the information is filled in, the pet is added to firebase
        else {
            String userID = fireuser.getUid();

            // creates a new unique key and returns the location
            petsReference = mDatabase.child("Pets").child(userID).child("Pets").push();
            // Pet constructor
            Pet pet = new Pet(name, date_of_birth, species, breed);

            petsReference.setValue(pet);

            Toast toast = Toast.makeText(this, getString(R.string.new_pet_added),Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
    }

    public void addImage() {
        mGetContent.launch("image/*");

    }

    public void uploadImage(Uri uri){
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        StorageReference imageUploadRefrence = storageReference.child("images/"+uri.getLastPathSegment());

        //Profile pictures then under userId/images. to edit the data, you need to change the image

        UploadTask uploadTask = imageUploadRefrence.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageUrl = uri.toString();
                        Map<String, Object> imageurl = new HashMap<>();
                        imageurl.put("photoUrl", imageUrl);
                        petsReference.updateChildren(imageurl);



                    }
                });
            }
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {

                    if (uri == null){
                        return;
                    }
                    uriimage = uri;
                    Glide.with(AddPetActivity.this).load(uri).into(imageView);

                }
            });




}
