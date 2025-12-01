package com.kelompok5.openlibrary.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kelompok5.openlibrary.data.model.Book;
import com.kelompok5.openlibrary.data.model.FavoriteBook;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(FavoriteBook book);

    @Delete
    void deleteFavorite(FavoriteBook book);

    @Query("SELECT * FROM favorite_books")
    LiveData<List<FavoriteBook>> getFavorites();
}

