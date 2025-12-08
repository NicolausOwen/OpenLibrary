package com.kelompok5.openlibrary.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BookDetail {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private Object description;

    @SerializedName("covers")
    private List<Integer> covers;

    @SerializedName("authors")
    private List<DetailAuthor> authors;

    public String getTitle() {
        return title;
    }

    // Kembalikan string deskripsi
    public String getDescriptionText() {
        if (description == null) return "No description available.";

        if (description instanceof String) return (String) description;

        if (description instanceof com.google.gson.internal.LinkedTreeMap) {
            Object value = ((com.google.gson.internal.LinkedTreeMap) description).get("value");
            if (value != null) return value.toString();
        }

        return "No description available.";
    }

    public List<Integer> getCovers() {
        return covers;
    }

    public List<DetailAuthor> getAuthors() {
        return authors;
    }

    public String getFirstAuthor() {
        if (authors != null && !authors.isEmpty()) {
            return authors.get(0).getName();
        }
        return "Unknown";
    }

    // Mapping author
    public static class DetailAuthor {
        @SerializedName("author")
        private AuthorKey author;

        public String getName() {
            return author != null ? author.name : null;
        }
    }

    public static class AuthorKey {
        @SerializedName("key")
        public String key;

        @SerializedName("name")
        public String name;
    }
}
