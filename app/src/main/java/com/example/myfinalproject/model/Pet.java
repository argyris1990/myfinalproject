package com.example.myfinalproject.model;

import java.util.HashMap;
import java.util.Map;

public class Pet {

    private String name;
    private String dateOfBirth; // "dd/mm/yyyy"
    private String species;
    private String breed;

    private String photoUrl;
    private String tagID;


    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private Map<String, Object> Weight = new HashMap();

    public Map<String, Object> getWeight() {
        return Weight;
    }

    public void setWeight(Map<String, Object> weight) {
        Weight = weight;
    }

    public Pet() {
    }

    public Pet(String name, String dateOfBirth, String species, String breed) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.species = species;
        this.breed = breed;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }
}
