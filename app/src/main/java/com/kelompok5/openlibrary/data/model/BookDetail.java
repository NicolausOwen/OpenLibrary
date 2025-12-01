package com.kelompok5.openlibrary.data.model;

import com.google.gson.annotations.SerializedName;

public class BookDetail {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private Object description;

    @SerializedName("covers")
    private int[] covers;

    @SerializedName("subjects")
    private String[] subjects;

    public String getTitle() { return title; }

    public String getDescriptionText() {
        if (description instanceof String) {
            return (String) description;
        }
        return "";
    }

    public int[] getCovers() { return covers; }

    public String[] getSubjects() { return subjects; }
}
