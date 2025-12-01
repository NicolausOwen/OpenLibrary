package com.kelompok5.openlibrary.ui.book;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kelompok5.openlibrary.data.model.Book;
import com.kelompok5.openlibrary.data.model.BookDetail;
import com.kelompok5.openlibrary.data.model.FavoriteBook;
import com.kelompok5.openlibrary.data.model.HistoryBook;
import com.kelompok5.openlibrary.data.repository.BookRepository;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private final BookRepository repository;

    // ==========================
    // LiveData utama
    // ==========================
    private final LiveData<List<Book>> books;              // Search & trending
    private final MutableLiveData<List<Book>> categoryBooks = new MutableLiveData<>();
    private final MutableLiveData<BookDetail> bookDetail = new MutableLiveData<>();
    private final MutableLiveData<String> readLink = new MutableLiveData<>();

    private final LiveData<List<FavoriteBook>> favorites;          // From Room
    private final LiveData<List<HistoryBook>> history;            // From Room

    // UI State
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();


    public BookViewModel(@NonNull Application app) {
        super(app);
        repository = new BookRepository(app);

        // LiveData Room
        books = repository.getBooks();        // all searched / trending data
        favorites = repository.getFavorites();
        history = repository.getHistory();
    }

    // ============================================
    // GETTER UNTUK FRAGMENT
    // ============================================
    public LiveData<List<Book>> getBooks()       { return books; }
    public LiveData<List<Book>> getCategoryBooks() { return categoryBooks; }
    public LiveData<BookDetail> getBookDetail()  { return bookDetail; }
    public LiveData<String> getReadLink()        { return readLink; }

    public LiveData<List<FavoriteBook>> getFavorites()   { return favorites; }
    public LiveData<List<HistoryBook>> getHistory()     { return history; }

    public LiveData<Boolean> isLoading()         { return isLoading; }
    public LiveData<String> getError()           { return error; }

    // ============================================
    // 1. TRENDING (Home)
    // ============================================
    public void loadTrending() {
        searchBooks("trending");
    }

    // ============================================
    // 2. SEARCH
    // ============================================
    public void searchBooks(String query) {
        isLoading.setValue(true);

        repository.searchBooks(query, new BookRepository.SimpleCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
            }

            @Override
            public void onError(String msg) {
                error.postValue(msg);
                isLoading.postValue(false);
            }
        });
    }


    // ============================================
    // 3. CATEGORY RESULT (Subjects API)
    // ============================================
    public void loadCategoryBooks(String category) {
        isLoading.setValue(true);

        repository.fetchBooksByCategory(category, new BookRepository.BookListCallback() {
            @Override
            public void onSuccess(List<Book> list) {
                categoryBooks.postValue(list);
                isLoading.postValue(false);
            }

            @Override
            public void onError(String msg) {
                error.postValue(msg);
                isLoading.postValue(false);
            }
        });
    }

    // ============================================
    // 4. DETAIL PAGE
    // ============================================
    public void loadBookDetail(String workId) {
        isLoading.setValue(true);

        repository.fetchBookDetail(workId, new BookRepository.DetailCallback() {
            @Override
            public void onSuccess(BookDetail detail) {
                bookDetail.postValue(detail);
                isLoading.postValue(false);
            }

            @Override
            public void onError(String msg) {
                error.postValue(msg);
                isLoading.postValue(false);
            }
        });
    }

    // ============================================
    // 5. READ PAGE (Edition API)
    // ============================================
    public void loadReadLink(String olid) {
        isLoading.setValue(true);
        repository.fetchReadLink(olid, new BookRepository.ReadCallback() {
            @Override
            public void onSuccess(String link) {
                readLink.postValue(link);
                isLoading.postValue(false);
            }

            @Override
            public void onError(String msg) {
                error.postValue(msg);
                isLoading.postValue(false);
            }
        });
    }

    // ============================================
    // 6. FAVORITES & HISTORY (Room)
    // ============================================
    public void addToFavorites(Book book) {
        repository.insertFavorite(book);
    }

    public void addToHistory(Book book) {
        repository.insertHistory(book);
    }

    public void removeFavorite(Book book) {
        repository.deleteFavorite(book);
    }
}
