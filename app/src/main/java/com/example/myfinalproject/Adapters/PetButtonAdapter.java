package com.example.myfinalproject.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.example.myfinalproject.AppData;
import com.example.myfinalproject.R;


public class PetButtonAdapter  extends RecyclerView.Adapter<PetButtonAdapter.ViewHolder> {


    private LinkedHashMap<String,Object> buttonsMap = new LinkedHashMap<>();
    private ArrayList<String> buttonNames;
    private Context context;

    public PetButtonAdapter(Context context, LinkedHashMap<String,Object> buttonsMap ) {
        this.context = context;
        this.buttonsMap = buttonsMap;
    }

    @NonNull
    @Override
    public PetButtonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_pet_button, parent, false);

        return  new PetButtonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetButtonAdapter.ViewHolder holder, int position) {

        //Updates buttonNames list
        buttonNames = new ArrayList<>(buttonsMap.keySet());

        //Get buttons name an set name to button
        position = holder.getLayoutPosition();
        String buttonName =  buttonNames.get(position);
        Button button = (Button) holder.button;
        button.setText(buttonName);

        //Button click lissener to go to activity listed in buttonsMap

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object activity = buttonsMap.get(buttonName);

                //Sets Dataheader to match the buttons name
                AppData appData = AppData.getInstance();
                appData.setDataHeader(buttonName);
                Intent intent = new Intent(context, (Class<?>) activity);
                context.startActivity(intent);
            }
        });
        if(position > 4) {
            //Build database reference
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String petId = AppData.getInstance().getPetId();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets").child(petId).child("CustomData").child(buttonName);
            //Create dialog on long press to confirm removing the data from given position.
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.delete)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reference.removeValue();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    AlertDialog dialog = builder.create();


                    dialog.show();
                    return true;
                }

            });
        }

    }

    @Override
    public int getItemCount() {
        return buttonsMap.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            button = (Button) itemView.findViewById(R.id.petViewButton);
        }
    }

}
