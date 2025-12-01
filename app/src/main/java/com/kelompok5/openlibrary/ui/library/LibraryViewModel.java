package com.kelompok5.openlibrary.ui.library;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kelompok5.openlibrary.data.model.FavoriteBook;
import com.kelompok5.openlibrary.data.model.HistoryBook;
import com.kelompok5.openlibrary.data.repository.BookRepository;

import java.util.List;

public class LibraryViewModel extends AndroidViewModel {

    private final BookRepository repository;

    private final LiveData<List<FavoriteBook>> favorites;
    private final LiveData<List<HistoryBook>> history;

    public LibraryViewModel(@NonNull Application app) {
        super(app);
        repository = new BookRepository(app);

        favorites = repository.getFavorites();
        history = repository.getHistory();
    }

    public LiveData<List<FavoriteBook>> getFavorites() {
        return favorites;
    }

    public LiveData<List<HistoryBook>> getHistory() {
        return history;
    }
}
