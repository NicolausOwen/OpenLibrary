package com.kelompok5.openlibrary.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SubjectResponse {

    @SerializedName("works")
    private List<Book> works;

    public List<Book> getWorks() {
        return works;
    }
}
