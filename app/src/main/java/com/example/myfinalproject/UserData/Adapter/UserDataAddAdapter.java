package com.example.myfinalproject.UserData.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.myfinalproject.R;

public class UserDataAddAdapter extends RecyclerView.Adapter<UserDataAddAdapter.ViewHolder> {


    private List<String> fields;
    private List<String> editTextFields;

    public List<String> getFields() {
        return fields;
    }
    public List<String> getEditTextFields() {
        return editTextFields;
    }


    public UserDataAddAdapter(List<String> fields) {
        this.fields = fields;
        this.editTextFields = new ArrayList<String>(fields);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText editText;
        private TextView headerTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editText = (EditText) itemView.findViewById(R.id.textInputData);
            headerTextView = (TextView) itemView.findViewById(R.id.textFieldName);
        }


    }
    @NonNull
    @Override
    public UserDataAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_add_data, parent, false);

        return new UserDataAddAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDataAddAdapter.ViewHolder holder, int position) {

        //add TextWatcher to edit text fields. All changes are then updated to editextFields array
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int position = holder.getAdapterPosition();

                editTextFields.remove(position);
                editTextFields.add(position,charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.headerTextView.setText(fields.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }
}
