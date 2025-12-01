package com.kelompok5.openlibrary.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kelompok5.openlibrary.data.model.HistoryBook;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(HistoryBook book);

    @Query("SELECT * FROM history_books")
    LiveData<List<HistoryBook>> getHistory();
}

