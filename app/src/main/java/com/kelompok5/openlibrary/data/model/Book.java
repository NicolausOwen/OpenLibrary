package com.kelompok5.openlibrary.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.kelompok5.openlibrary.utils.Converters;

import java.util.List;

@Entity(tableName = "books")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int localId;

    @SerializedName("title")
    private String title;

    @SerializedName("author_name")
    @TypeConverters(Converters.class)
    private List<String> authorName;

    @SerializedName("first_publish_year")
    private Integer firstPublishYear;

    @SerializedName("cover_i")
    private Integer coverId;

    @SerializedName("key")
    private String workKey;

    @SerializedName("edition_key")
    @TypeConverters(Converters.class)
    private List<String> editionKey;

    @SerializedName("isbn")
    @TypeConverters(Converters.class)
    private List<String> isbn;

    // ========== Added OpenLibrary preview fields ==========
    @SerializedName("ia")
    @TypeConverters(Converters.class)
    private List<String> ia;

    @SerializedName("ebook_count_i")
    private Integer ebookCount;

    @SerializedName("availability")
    private Availability availability;
    // ======================================================


    // GETTERS
    public int getLocalId() { return localId; }
    public String getTitle() { return title; }
    public List<String> getAuthorName() { return authorName; }
    public Integer getFirstPublishYear() { return firstPublishYear; }
    public Integer getCoverId() { return coverId; }
    public String getWorkKey() { return workKey; }
    public List<String> getEditionKey() { return editionKey; }
    public List<String> getIsbn() { return isbn; }
    public List<String> getIa() { return ia; }
    public Integer getEbookCount() { return ebookCount; }
    public Availability getAvailability() { return availability; }

    // Extract ID:  "/works/OL123123W" → "OL123123W"
    public String getWorkId() {
        if (workKey == null) return null;
        return workKey.replace("/works/", "");
    }

    // SETTERS
    public void setLocalId(int localId) { this.localId = localId; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthorName(List<String> authorName) { this.authorName = authorName; }
    public void setFirstPublishYear(Integer firstPublishYear) { this.firstPublishYear = firstPublishYear; }
    public void setCoverId(Integer coverId) { this.coverId = coverId; }
    public void setWorkKey(String workKey) { this.workKey = workKey; }
    public void setEditionKey(List<String> editionKey) { this.editionKey = editionKey; }
    public void setIsbn(List<String> isbn) { this.isbn = isbn; }
    public void setIa(List<String> ia) { this.ia = ia; }
    public void setEbookCount(Integer ebookCount) { this.ebookCount = ebookCount; }
    public void setAvailability(Availability availability) { this.availability = availability; }
}
