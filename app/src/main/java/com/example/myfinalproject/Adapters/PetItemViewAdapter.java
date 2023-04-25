package com.example.myfinalproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import com.example.myfinalproject.AppData;
import com.example.myfinalproject.PetView;
import com.example.myfinalproject.R;
import com.example.myfinalproject.model.Pet;

public class PetItemViewAdapter extends RecyclerView.Adapter<PetItemViewAdapter.MyViewHolder> {

    int defaultImage;
    private Context context;

    //Array list to contain all pet id's
    private List<String> petIdKeyList = new ArrayList<String>();


    private DatabaseReference baseRefrence;

    public PetItemViewAdapter(Context ct, List<String> petIdKeyList, int img, DatabaseReference petRefrence) {
        this.petIdKeyList = petIdKeyList;
        //data1 = s1;
        defaultImage = img;
        context = ct;
        baseRefrence = petRefrence;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_button, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String petId = petIdKeyList.get(position);

        //set click listener on views image and go to petView activity.
        holder.myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getLayoutPosition();

                String petId = petIdKeyList.get(position);
                Context context = view.getContext();
                Intent intent = new Intent(context, PetView.class);
                //sets pet id to clicked pets id
                AppData.getInstance().setPetId(petId);
                context.startActivity(intent);
            }
        });

        baseRefrence.child(petId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {
                    Pet pet = task.getResult().getValue(Pet.class);

                    String name = pet.getName();
                    holder.myText1.setText(name);

                    String imageUrl = pet.getPhotoUrl();
                    if (imageUrl != null) {
                        Glide.with(holder.itemView).load(imageUrl).into(holder.myImage);
                    }
                    else {

                        holder.myImage.setImageResource(defaultImage);
                    }


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return petIdKeyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myText1;
        ImageButton myImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            myText1 = itemView.findViewById(R.id.txt1);
            myImage = itemView.findViewById(R.id.imageButton1);

        }
    }
}
