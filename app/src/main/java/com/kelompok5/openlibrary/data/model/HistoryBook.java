package com.kelompok5.openlibrary.data.model;

public class HistoryBook {
    private String id;        // workId
    private String title;
    private String author;
    private Integer cover;
    private long timestamp;   // supaya history urut

    public HistoryBook() {}

    public HistoryBook(String id, String title, String author, Integer cover, long timestamp) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Integer getCover() { return cover; }
    public long getTimestamp() { return timestamp; }
}
