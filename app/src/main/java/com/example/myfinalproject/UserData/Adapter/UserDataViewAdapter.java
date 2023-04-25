package com.example.myfinalproject.UserData.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.example.myfinalproject.R;

public class UserDataViewAdapter  extends RecyclerView.Adapter<UserDataViewAdapter.ViewHolder> {


    private  DatabaseReference reference;
    private  List<String> customDataIDs;
    private  Context context;

    private  List<String> fields;

    public List<String> getFields() {
        return fields;
    }

    public UserDataViewAdapter(Context context, List<String> customDataIDs, DatabaseReference reference){
        this.context = context;
        this.customDataIDs = customDataIDs;
        this.reference = reference;
        setFields();
    }

    private void setFields() {
        //set the field names which to populate later
        //fetches data on reference and loops true and saves the keys to field list
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, Object> data = new HashMap<>();
                for(DataSnapshot child : snapshot.getChildren()) {
                    DataSnapshot chid = child;

                    data = (Map<String, Object>) child.getValue();
                }
                fields = new ArrayList<>(data.keySet());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_user_view_data_list, parent, false);

        return new UserDataViewAdapter.ViewHolder(view, 5);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.linearLayout.removeAllViews();

        //From usersmdata id get the id referring to viewholder in this position
        DatabaseReference idReference  = reference.child(customDataIDs.get(holder.getAdapterPosition()));

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

                    //For how many fields create new layout custom_data_view_list_field that has two text fields for field and files data
                    //Every loo that layout file is then added to holders linearlayout.
                    for(int i = 0; i < fields.size(); i++){

                        View layout = LayoutInflater.from(holder.itemView.getContext())
                                .inflate(R.layout.activity_user_view_data_field_list, holder.linearLayout,false);

                        TextView field = layout.findViewById(R.id.textViewField);
                        TextView fieldData = (TextView) layout.findViewById(R.id.textViewFieldData);

                        fieldData.setText( data.get(fields.get(i)).toString());
                        field.setText(fields.get(i));

                        ((LinearLayout) holder.linearLayout).addView(layout);

                    }
                    //Resize layout
                    holder.linearLayout.getParent().getParent().requestLayout();

                }

            }
        });
        //Create dialog on long press to confirm removing the data from given position.
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idReference.removeValue();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();


                dialog.show();
                return true;
            };
        });

    }

    @Override
    public int getItemCount() {
        return customDataIDs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup linearLayout;
        private LinearLayout fieldLayout;

        public ViewHolder(@NonNull View itemView, int number_of_fields) {
            super(itemView);

            linearLayout =  itemView.findViewById(R.id.linearLayout);
        }

        public ViewHolder(View view) {
            super(view);
        }
    }


}
