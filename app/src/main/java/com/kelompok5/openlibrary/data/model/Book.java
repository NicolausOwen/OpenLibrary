package com.kelompok5.openlibrary.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.kelompok5.openlibrary.utils.Converters;

import java.util.List;
import java.util.Map;

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

    public int getLocalId() {
        return localId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthorName() {
        return authorName;
    }

    public Integer getFirstPublishYear() {
        return firstPublishYear;
    }

    public Integer getCoverId() {
        return coverId;
    }

    public String getWorkKey() {
        return workKey;
    }

    public String getWorkId() {
        if (workKey == null) return null;
        return workKey.replace("/works/", "");
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthorName(List<String> authorName) {
        this.authorName = authorName;
    }

    public void setFirstPublishYear(Integer firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    public void setCoverId(Integer coverId) {
        this.coverId = coverId;
    }

    public void setWorkKey(String workKey) {
        this.workKey = workKey;
    }

}
