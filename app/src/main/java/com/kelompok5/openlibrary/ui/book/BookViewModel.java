package com.kelompok5.openlibrary.ui.book;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.kelompok5.openlibrary.data.model.Book;
import com.kelompok5.openlibrary.data.repository.BookRepository;
import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private final BookRepository repository;
    private final LiveData<List<Book>> books;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public BookViewModel(@NonNull Application app) {
        super(app);
        repository = new BookRepository(app);
        books = repository.getBooks();
    }

    public LiveData<List<Book>> getBooks() { return books; }
    public LiveData<Boolean> isLoading() { return isLoading; }
    public LiveData<String> getError() { return error; }

    public void searchBooks(String title) {
        isLoading.setValue(true);
        repository.fetchBooks(title, new BookRepository.ApiCallback() {
            @Override
            public void onSuccess() { isLoading.postValue(false); }
            @Override
            public void onError(String msg) {
                isLoading.postValue(false);
                error.postValue(msg);
            }
        });
    }
}
