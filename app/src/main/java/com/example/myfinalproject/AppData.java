package com.example.myfinalproject;

import java.util.ArrayList;

public class AppData {

    private static AppData INSTANCE = null;

    private AppData(){};

    public static AppData getInstance(){
        if (INSTANCE == null){
            INSTANCE = new AppData();
        }
        return INSTANCE;
    }

    //data fields
    private String petId;
    private String dataHeader;
    private ArrayList<String> fields;


    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getDataHeader() {
        return dataHeader;
    }

    public void setDataHeader(String dataHeader) {
        this.dataHeader = dataHeader;
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public void setFields(ArrayList<String> fields) {
        this.fields = fields;
    }
}