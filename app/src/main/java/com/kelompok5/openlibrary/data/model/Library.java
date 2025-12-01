package com.kelompok5.openlibrary.data.model;

public class Library {
    private final String name;
    private final int iconResId;

    public Library(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }
}