package com.kelompok5.openlibrary.data.model;

import com.google.gson.annotations.SerializedName;

public class EditionResponse {

    @SerializedName("links")
    private Links[] links;

    public String getReadUrl() {
        if (links != null && links.length > 0) {
            return links[0].url;
        }
        return null;
    }

    public static class Links {
        @SerializedName("title")
        public String title;

        @SerializedName("url")
        public String url;
    }
}
