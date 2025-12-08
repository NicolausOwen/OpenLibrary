package com.kelompok5.openlibrary.ui.book;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kelompok5.openlibrary.data.model.Book;
import com.kelompok5.openlibrary.data.model.BookDetail;
import com.kelompok5.openlibrary.data.model.BookResponse;
import com.kelompok5.openlibrary.data.model.EditionResponse;
import com.kelompok5.openlibrary.data.model.FavoriteBook;
import com.kelompok5.openlibrary.data.model.HistoryBook;
import com.kelompok5.openlibrary.data.model.SubjectResponse;
import com.kelompok5.openlibrary.data.repository.BookApiRepository;
import com.kelompok5.openlibrary.data.repository.FirestoreRepository;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    // API + Firestore
    private final BookApiRepository apiRepository;
    private final FirestoreRepository firestoreRepository;

    // LiveData API
    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();
    private final MutableLiveData<List<Book>> categoryBooks = new MutableLiveData<>();
    private final MutableLiveData<BookDetail> bookDetail = new MutableLiveData<>();
    private final MutableLiveData<String> readLink = new MutableLiveData<>();

    // Firestore LiveData
    private final LiveData<List<FavoriteBook>> favorites;
    private final LiveData<List<HistoryBook>> history;

    // UI State
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();


    public BookViewModel(@NonNull Application app) {
        super(app);

        apiRepository = new BookApiRepository();
        firestoreRepository = new FirestoreRepository();

        favorites = firestoreRepository.getFavorites();
        history = firestoreRepository.getHistory();
    }

    // Getters
    public LiveData<List<Book>> getBooks() { return books; }
    public LiveData<List<Book>> getCategoryBooks() { return categoryBooks; }
    public LiveData<BookDetail> getBookDetail() { return bookDetail; }
    public LiveData<String> getReadLink() { return readLink; }

    public LiveData<List<FavoriteBook>> getFavorites() { return favorites; }
    public LiveData<List<HistoryBook>> getHistory() { return history; }

    public LiveData<Boolean> isLoading() { return isLoading; }
    public LiveData<String> getError() { return error; }


    // ============================================
    // 1. SEARCH BOOKS
    // ============================================
    public void searchBooks(String query) {
        isLoading.setValue(true);

        apiRepository.search(query, new BookApiRepository.Result<BookResponse>() {
            @Override
            public void success(BookResponse data) {
                books.postValue(data.getDocs());
                isLoading.postValue(false);
            }

            @Override
            public void error(String msg) {
                error.postValue(msg);
                isLoading.postValue(false);
            }
        });
    }

    // ============================================
    // 2. CATEGORY BOOKS
    // ============================================
    public void loadCategoryBooks(String category) {
        isLoading.setValue(true);

        apiRepository.search(category, new BookApiRepository.Result<BookResponse>() {
            @Override
            public void success(BookResponse data) {
                categoryBooks.postValue(data.getDocs());
                isLoading.postValue(false);
            }

            @Override
            public void error(String msg) {
                error.postValue(msg);
                isLoading.postValue(false);
            }
        });
    }

    // ============================================
    // 3. DETAIL PAGE
    // ============================================
    public void loadBookDetail(String workId) {
        isLoading.setValue(true);

        apiRepository.detail(workId, new BookApiRepository.Result<BookDetail>() {
            @Override
            public void success(BookDetail data) {
                bookDetail.postValue(data);
                isLoading.postValue(false);
            }

            @Override
            public void error(String msg) {
                error.postValue(msg);
                isLoading.postValue(false);
            }
        });
    }

    // ============================================
    // 4. READ PAGE (EDITION API)
    // ============================================
    public void loadReadLink(String olid) {
        isLoading.setValue(true);

        apiRepository.read(olid, new BookApiRepository.Result<EditionResponse>() {
            @Override
            public void success(EditionResponse data) {
                readLink.postValue(data.getReadUrl());
                isLoading.postValue(false);
            }

            @Override
            public void error(String msg) {
                error.postValue(msg);
                isLoading.postValue(false);
            }
        });
    }

    // ============================================
    // 5. FIRESTORE FAVORITES + HISTORY
    // ============================================
    public void addToFavorites(Book book) {
        String author = "";
        if (book.getAuthorName() != null && !book.getAuthorName().isEmpty()) {
            author = book.getAuthorName().get(0);   // ambil author pertama
        }

        firestoreRepository.addFavorite(
                new FavoriteBook(
                        book.getWorkId(),
                        book.getTitle(),
                        author,
                        book.getCoverId()
                )
        );
    }

    public void removeFavorite(String workId) {
        firestoreRepository.removeFavorite(workId);
    }

    public void addToHistory(Book book) {
        String author = "";
        if (book.getAuthorName() != null && !book.getAuthorName().isEmpty()) {
            author = book.getAuthorName().get(0);
        }

        firestoreRepository.addHistory(
                new HistoryBook(
                        book.getWorkId(),
                        book.getTitle(),
                        author,
                        book.getCoverId(),
                        System.currentTimeMillis()
                )
        );
    }
}
