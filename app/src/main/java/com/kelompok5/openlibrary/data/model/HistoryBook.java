package com.kelompok5.openlibrary.data.model;

public class HistoryBook {
    private String id;        // workId
    private String title;
    private String author;
    private String description; // <--- TAMBAHAN BARU
    private Integer coverId;
    private long timestamp;

    // Constructor Kosong (Wajib untuk Firestore)
    public HistoryBook() {}

    // Constructor Baru dengan 6 Parameter
    public HistoryBook(String id, String title, String author, String description, Integer coverId, long timestamp) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description; // <--- Disimpan di sini
        this.coverId = coverId;
        this.timestamp = timestamp;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; } // <--- Getter Baru
    public Integer getCoverId() { return coverId; }
    public long getTimestamp() { return timestamp; }
}