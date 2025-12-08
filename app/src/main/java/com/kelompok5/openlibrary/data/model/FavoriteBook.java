package com.kelompok5.openlibrary.data.model;

public class FavoriteBook {
    private String id;        // workId
    private String title;
    private String author;
    private Integer cover;

    public FavoriteBook() {} // Firestore needs empty constructor

    public FavoriteBook(String id, String title, String author, Integer cover) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Integer getCover() { return cover; }
}
