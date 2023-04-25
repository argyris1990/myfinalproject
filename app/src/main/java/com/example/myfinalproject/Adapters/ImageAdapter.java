package com.example.myfinalproject.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.myfinalproject.R;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<String> IdList;
    private LinkedHashMap<String,Object> imagesHasMAp;
    private DatabaseReference databaseReference;
    private Context context;
    public ImageAdapter(Context context, ArrayList<String> idList, LinkedHashMap<String, Object> imagesHasMAp, DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
        this.IdList = idList;
        this.imagesHasMAp = imagesHasMAp;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_pics_list, parent, false);

        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        position = holder.getAdapterPosition();

        String key = IdList.get(position);


        Map<?,?> map = (Map<?, ?>) imagesHasMAp.get(key);
        if (map == null) {
            return;
        }

        String imageUrl = (String) map.get("ImageUrl");


        Glide.with(holder.itemView).load(imageUrl).into(holder.imageView);

        //Create dialog on long press to confirm removing the data from given position.
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                int position = holder.getAdapterPosition();
                                String key = IdList.get(position);


                                Map<?,?> map = (Map<?, ?>) imagesHasMAp.get(key);
                                if (map == null) {
                                    return;
                                }

                                String imageUrl = (String) map.get("ImageUrl");

                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        DatabaseReference idReference = databaseReference.child(key);
                                        idReference.removeValue();

                                    }
                                });
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
        if ( IdList == null){
            return 0;
        }
        return IdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_list_view_image);

        }
    }
}
