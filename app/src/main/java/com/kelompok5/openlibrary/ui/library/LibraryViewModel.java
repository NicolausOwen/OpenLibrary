package com.kelompok5.openlibrary.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kelompok5.openlibrary.data.model.FavoriteBook;
import com.kelompok5.openlibrary.data.model.HistoryBook;
import com.kelompok5.openlibrary.data.repository.FirestoreRepository;

import java.util.List;

public class LibraryViewModel extends ViewModel {

    private final FirestoreRepository repo = new FirestoreRepository();

    public LiveData<List<FavoriteBook>> getFavorites() {
        return repo.getFavorites();
    }

    public LiveData<List<HistoryBook>> getHistory() {
        return repo.getHistory();
    }
}
