package com.kelompok5.openlibrary.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.kelompok5.openlibrary.utils.Converters;

import java.util.List;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    @TypeConverters(Converters.class)
    private List<String> author_name;
    private int first_publish_year;
    private int cover_i;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public List<String> getAuthor_name() { return author_name; }
    public int getFirst_publish_year() { return first_publish_year; }
    public int getCover_i() { return cover_i; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor_name(List<String> author_name) { this.author_name = author_name; }
    public void setFirst_publish_year(int first_publish_year) { this.first_publish_year = first_publish_year; }
    public void setCover_i(int cover_i) { this.cover_i = cover_i; }
}
