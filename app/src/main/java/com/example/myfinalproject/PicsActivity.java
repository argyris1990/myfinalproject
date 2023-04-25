package com.example.myfinalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.myfinalproject.Adapters.ImageAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PicsActivity extends BaseActivity {

    private DatabaseReference databaseReference;

    private String petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pics);

        AppData appData = AppData.getInstance();
        petId = appData.getPetId();

        //Data sets for recyclerview
        ArrayList<String> IdList = new ArrayList<>();
        LinkedHashMap<String,Object> imagesHasMAp = new LinkedHashMap<>();
        //Data to recyclerview
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("Pets").child(userID).child("Pets").child(petId).child("Images");

        //Recyclerview adapter
        ImageAdapter adapter = new ImageAdapter(this, IdList,imagesHasMAp,databaseReference);

        //Initialize recyclerview
        RecyclerView recyclerView = findViewById(R.id.images_view_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        //Button add images
        ImageButton addImageButton = (ImageButton) findViewById(R.id.image_view_add_image);
        addImageButton.setOnClickListener(view -> mGetContent.launch("image/*"));

        // Set listener
        ChildEventListener childEventListener = imagesChildEventListener(imagesHasMAp,IdList,adapter);
        databaseReference.addChildEventListener(childEventListener);

    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {

                if (uri == null){
                    return;
                }
                uploadImage(uri);
            });

    private void uploadImage(Uri uri) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReference().child(userID).child(petId).child("images");

        StorageReference imageUploadRefrence = storageReference.child("images/"+uri.getLastPathSegment());

        UploadTask uploadTask = imageUploadRefrence.putFile(uri);

        uploadTask.addOnSuccessListener(taskSnapshot -> taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri1 -> {

            String imageUrl = uri1.toString();
            Map<String, Object> imageMap = new HashMap<>();
            imageMap.put("ImageUrl", imageUrl);
            DatabaseReference databaseReferenceId = databaseReference.push();
            databaseReferenceId.setValue(imageMap);


        }));
    }
    public ChildEventListener imagesChildEventListener(LinkedHashMap<String,Object> imagesHasMAp, ArrayList<String> IdList, RecyclerView.Adapter adapter) {


        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                String key = snapshot.getKey();
                IdList.add(key);
                imagesHasMAp.put(key, map);

                int position = IdList.indexOf(key);

                adapter.notifyItemInserted(position);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey();
                int position = IdList.indexOf(key);

                adapter.notifyItemChanged(position);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getKey();
                int position = IdList.indexOf(key);
                IdList.remove(position);

                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        return childEventListener;
    }
}
