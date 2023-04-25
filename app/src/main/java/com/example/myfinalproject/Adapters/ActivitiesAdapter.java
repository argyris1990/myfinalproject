package com.example.myfinalproject.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Map;

import com.example.myfinalproject.R;


public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> dataIDs;
    private DatabaseReference databaseReference;

    public ActivitiesAdapter(Context context, ArrayList<String> dataIDs, DatabaseReference dataReference) {
        this.dataIDs = dataIDs;
        this.context = context;
        this.databaseReference = dataReference;
    }

    @NonNull
    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_activities_list, parent, false);

        return new ActivitiesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesAdapter.ViewHolder holder, int position) {

        //position = holder.getAdapterPosition();
        //From userdata id get the id referring to viewholder in this position
        DatabaseReference idReference  = databaseReference.child(dataIDs.get(holder.getAdapterPosition()));

        //Get data in reference
        idReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    //Get data in reference
                    Map<String,Object> data =  (Map<String, Object>) task.getResult().getValue();

                    String date = (String) data.get("date");
                    String type = (String) data.get("type");
                    String length = (String) data.get("length");

                    holder.textviewDate.setText(date);
                    holder.textviewType.setText(type);
                    holder.textviewLength.setText(length);


                }
            }

        });

    }

    @Override
    public int getItemCount() {
        if(dataIDs == null){
            return 0;
        }
        return dataIDs.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textviewDate;
        private TextView textviewType;
        private TextView textviewLength;

        //private TexView textField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textviewDate = itemView.findViewById(R.id.activitiesDate);
            textviewType = itemView.findViewById(R.id.activitiesType);
            textviewLength = itemView.findViewById(R.id.activitiesLength);


        }
    }
}
