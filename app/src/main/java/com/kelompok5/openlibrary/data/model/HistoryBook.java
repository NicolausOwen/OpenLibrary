package com.kelompok5.openlibrary.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.kelompok5.openlibrary.utils.Converters;

import java.util.List;

@Entity(tableName = "history_books")
public class HistoryBook {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    @TypeConverters(Converters.class)
    private List<String> authorName;

    private Integer coverId;

    private String workId;

    public HistoryBook(String title, List<String> authorName, Integer coverId, String workId) {
        this.title = title;
        this.authorName = authorName;
        this.coverId = coverId;
        this.workId = workId;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public List<String> getAuthorName() { return authorName; }
    public Integer getCoverId() { return coverId; }
    public String getWorkId() { return workId; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthorName(List<String> authorName) { this.authorName = authorName; }
    public void setCoverId(Integer coverId) { this.coverId = coverId; }
    public void setWorkId(String workId) { this.workId = workId; }
}
